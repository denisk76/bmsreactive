package ru.bms.webservice.pilot;

import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.internal.mapping.ObjectMapperSerializationContextImpl;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bms.AddTerminalRequest;
import ru.bms.api.Bill;
import ru.bms.service.TerminalService;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@Testcontainers
@Slf4j
@WebFluxTest
@ContextConfiguration(classes = {PilotTestConfig.class})
public class ComposerIntegrationTest {

    @Autowired
    private TerminalService terminalService;


    private static final String WEB_SERVICE = "web-service_1";
    private static final String PAYMENT_SERVICE = "payment-service_1";
    private static final String TERMINAL_SERVICE = "terminal-service_1";
    private static final String HANDLER_SERVICE = "handler-service_1";
    private static final String CLIENT_SERVICE = "client-service_1";
    private static final int WEB_SERVICE_PORT = 8080;
    private static final int PAYMENT_SERVICE_PORT = 8080;
    private static final int TERMINAL_SERVICE_PORT = 8080;
    private static final int HANDLER_SERVICE_PORT = 8080;
    private static final int CLIENT_SERVICE_PORT = 8080;
    private static final PutPaymentRequest REQUEST = PutPaymentRequest.builder()
            .cardNum("1234")
            .terminalCode("10")
            .bill(Bill.builder().sum(BigDecimal.valueOf(120)).build())
            .build();
    private static final PutPaymentResponse RESPONSE = PutPaymentResponse.builder()
            .amount(BigDecimal.valueOf(23))
            .earn(BigDecimal.valueOf(12))
            .spend(BigDecimal.ZERO)
            .build();

    @Container
    private static DockerComposeContainer compose =
            new DockerComposeContainer(
                    new File("src/test/resources/simple-compose.yml")
            )
                    .withPull(false)
                    .withLocalCompose(true)
                    .withLogConsumer(WEB_SERVICE, getConsumer(WEB_SERVICE))
                    .withExposedService(WEB_SERVICE, WEB_SERVICE_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(600)))
                    .withLogConsumer(HANDLER_SERVICE, getConsumer(HANDLER_SERVICE))
                    .withExposedService(HANDLER_SERVICE, HANDLER_SERVICE_PORT,Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(600)))
                    .withLogConsumer(PAYMENT_SERVICE, getConsumer(PAYMENT_SERVICE))
                    .withExposedService(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT,Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(600)))
                    .withLogConsumer(TERMINAL_SERVICE, getConsumer(TERMINAL_SERVICE))
                    .withExposedService(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT,Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(600)))
                    .withLogConsumer(CLIENT_SERVICE, getConsumer(CLIENT_SERVICE))
                    .withExposedService(CLIENT_SERVICE, CLIENT_SERVICE_PORT,Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(600)));

    private static Slf4jLogConsumer getConsumer(String serviceName) {
        return new Slf4jLogConsumer(log).withPrefix(serviceName);
    }

    @Test
    @DisplayName("Web-service hello test")
    void helloTest() {
        logInfo(WEB_SERVICE, WEB_SERVICE_PORT);
        logInfo(HANDLER_SERVICE, HANDLER_SERVICE_PORT);
        logInfo(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT);
        logInfo(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
        logInfo(CLIENT_SERVICE, CLIENT_SERVICE_PORT);
        String address = getIpAddr(compose, WEB_SERVICE, WEB_SERVICE_PORT) + "/hello";
        given().get(address)
                .then().statusCode(200).assertThat()
                .body("message",equalTo("Hello, my friend! I`m BPS Controller."));
    }

    @Test
    @DisplayName("Payment test")
    void paymentTest() {

        logInfo(WEB_SERVICE, WEB_SERVICE_PORT);
        logInfo(HANDLER_SERVICE, HANDLER_SERVICE_PORT);
        logInfo(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT);
        logInfo(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
        logInfo(CLIENT_SERVICE, CLIENT_SERVICE_PORT);
        String address = getIpAddr(compose, WEB_SERVICE, WEB_SERVICE_PORT) + "/payment";
        Jackson2Mapper jackson2Mapper = new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory());
        ObjectMapperSerializationContextImpl context = new ObjectMapperSerializationContextImpl();
        context.setObject(REQUEST);
        String serialize = jackson2Mapper.serialize(context);
        System.out.println("serialize = " + serialize);
        terminalService.setIpAddr(getIpAddr(compose, TERMINAL_SERVICE, TERMINAL_SERVICE_PORT));
        terminalService.addTerminal(AddTerminalRequest.builder().terminalCode("10").percent(BigDecimal.valueOf(10)).build()).block();
        terminalService.addTerminal(AddTerminalRequest.builder().terminalCode("20").percent(BigDecimal.valueOf(20)).build()).block();
        given().body(REQUEST, new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory()))
                .contentType("application/json")
                .post(address)
                .then().log().all()
                .statusCode(200)
                .assertThat().log().body()
                .body("amount", equalTo(RESPONSE.getAmount().intValue()))
                .body("earn", equalTo(RESPONSE.getEarn().intValue()))
                .body("spend", equalTo(RESPONSE.getSpend().intValue()))
        ;
    }

    private void logInfo(String name, int port) {
        log.info(name + " host: " + compose.getServiceHost(name, port));
        log.info(name + " port: " + compose.getServicePort(name, port).toString());
    }

    @NotNull
    private String getIpAddr(DockerComposeContainer container, String serviceName, int port) {
        return "http://"
                + container.getServiceHost(serviceName, port)
                + ":" + container.getServicePort(serviceName, port);
    }
}
