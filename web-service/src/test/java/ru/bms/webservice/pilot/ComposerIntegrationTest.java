package ru.bms.webservice.pilot;

import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.internal.mapping.ObjectMapperSerializationContextImpl;
import io.restassured.mapper.factory.DefaultJackson2ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bms.api.Bill;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@Testcontainers
@Slf4j
public class ComposerIntegrationTest {


    public static final String WEB_SERVICE = "web-service_1";
    public static final String PAYMENT_SERVICE = "payment-service_1";
    public static final String TERMINAL_SERVICE = "terminal-service_1";
    public static final String HANDLER_SERVICE = "handler-service_1";
    public static final String CLIENT_SERVICE = "client-service_1";
    public static final int WEB_SERVICE_PORT = 8080;
    public static final int PAYMENT_SERVICE_PORT = 8080;
    public static final int TERMINAL_SERVICE_PORT = 8080;
    public static final int HANDLER_SERVICE_PORT = 8080;
    public static final int CLIENT_SERVICE_PORT = 8080;
    public static final PutPaymentRequest REQUEST = PutPaymentRequest.builder()
            .cardNum("1234")
            .terminalCode("10")
            .bill(Bill.builder().sum(BigDecimal.valueOf(120)).build())
            .build();
    public static final PutPaymentResponse RESPONSE = PutPaymentResponse.builder()
            .amount(BigDecimal.valueOf(23))
            .earn(BigDecimal.valueOf(12))
            .spend(BigDecimal.ZERO)
            .build();

    @Container
    static DockerComposeContainer compose =
            new DockerComposeContainer(
                    new File("src/test/resources/simple-compose.yml")
            )
                    .withPull(false)
                    .withLocalCompose(true)
                    .withLogConsumer(WEB_SERVICE, getConsumer(WEB_SERVICE))
                    .withExposedService(WEB_SERVICE, WEB_SERVICE_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(40)))
                    .withLogConsumer(HANDLER_SERVICE, getConsumer(HANDLER_SERVICE))
                    .withExposedService(HANDLER_SERVICE, HANDLER_SERVICE_PORT)
                    .withLogConsumer(PAYMENT_SERVICE, getConsumer(PAYMENT_SERVICE))
                    .withExposedService(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT)
                    .withLogConsumer(TERMINAL_SERVICE, getConsumer(TERMINAL_SERVICE))
                    .withExposedService(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT)
                    .withLogConsumer(CLIENT_SERVICE, getConsumer(CLIENT_SERVICE))
                    .withExposedService(CLIENT_SERVICE, CLIENT_SERVICE_PORT);

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
