package ru.bms.webservice.pilot;

import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.internal.mapping.ObjectMapperSerializationContextImpl;
import io.restassured.mapper.factory.DefaultJackson2ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bms.api.Bill;
import ru.bms.api.HelloResponse;
import ru.bms.webservice.BPSConfig;
import ru.bms.webservice.WebServiceApplication;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {WebServiceApplication.class, BPSConfig.class})
@Slf4j
//@Testcontainers
public class WebServiceIntegrationTest {

    public static final PutPaymentRequest REQUEST = PutPaymentRequest.builder()
            .cardNum("1234")
            .terminalCode("345t")
            .bill(Bill.builder().sum(BigDecimal.TEN).build())
            .build();
    public static final PutPaymentResponse RESPONSE = PutPaymentResponse.builder()
            .amount(BigDecimal.TEN)
            .earn(BigDecimal.ONE)
            .spend(BigDecimal.ONE)
            .build();

//    @ClassRule
//    public static DockerComposeContainer webServiceContainer =
//            new DockerComposeContainer(new File("src/test/resources/web-compose.yml"))
//                    .withLocalCompose(true)
//                    .withPull(false)
//            .withExposedService("web-service_1", 80, Wait.forListeningPort());

    public static final String WEB_SERVICE = "web-service_1";
    public static final String PAYMENT_SERVICE = "payment-service_1";
    public static final String TERMINAL_SERVICE = "terminal-service_1";
    public static final String HANDLER_SERVICE = "handler-service_1";
    public static final int WEB_SERVICE_PORT = 8080;
    public static final int PAYMENT_SERVICE_PORT = 8080;
    public static final int TERMINAL_SERVICE_PORT = 8080;
    public static final int HANDLER_SERVICE_PORT = 8080;



//    @Container
//    public static DockerComposeContainer compose =
//            new DockerComposeContainer(
//                    new File("src/test/resources/simple-compose.yml")
//            )
//                    .withPull(false)
//                    .withLogConsumer(WEB_SERVICE, new Slf4jLogConsumer(log).withPrefix(WEB_SERVICE))
//                    .withExposedService(WEB_SERVICE, WEB_SERVICE_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(40)))
//                    .withExposedService(HANDLER_SERVICE, HANDLER_SERVICE_PORT)
//                    .withExposedService(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT)
//                    .withExposedService(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT)
//            ;

//    @Test
//    public void testPayment() throws Exception {
//        String address = getContainerUrl(WEB_SERVICE, WEB_SERVICE_PORT);
//        address += "/payment";
//        Jackson2Mapper jackson2Mapper = new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory());
//        ObjectMapperSerializationContextImpl context = new ObjectMapperSerializationContextImpl();
//        context.setObject(REQUEST);
//        String serialize = jackson2Mapper.serialize(context);
//        System.out.println("serialize = " + serialize);
//        given().body(REQUEST, new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory()))
//                .contentType("application/json")
//                .post(address)
//                .then().log().all()
//                .statusCode(200)
//                .assertThat().log().body()
//                .body("amount", equalTo("10"));
//    }
//
//    @Test
//    public void testHello() throws Exception {
//        String address = getContainerUrl(WEB_SERVICE, WEB_SERVICE_PORT);
//        address = address + "/hello";
//        getContainerUrl(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
//        getContainerUrl(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
//        given().get(address).then().statusCode(200).assertThat().log()
//                .body()
//                .body("message",equalTo("Hello, my friend! I`m BPS Controller."));
//    }
//
//    @NotNull
//    private String getContainerUrl(String webService, int webServicePort) {
//        String address = "http://" + compose.getServiceHost(webService, webServicePort) + ":" + compose.getServicePort(webService, webServicePort);
//        System.out.println(webService + " address: " + address);
//        return address;
//    }
//
//
//    @Test
//    public void simpleTestHello() throws Exception {
//        String address = "http://" + compose.getServiceHost(WEB_SERVICE, WEB_SERVICE_PORT) + ":"+compose.getServicePort(WEB_SERVICE, WEB_SERVICE_PORT);
//        address = address + "/hello";
//        String response = simpleGetRequest(address);
//        System.out.println("response = " + response);
//        assertEquals(response, "{\"message\":\"Hello, my friend! I`m BPS Controller.\"}");
//    }

    private String simpleGetRequest(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

//    @Test
    public void testPayment2() throws Exception {
        String host_ip = System.getenv("HOST_IP");
        given().body(REQUEST, new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory()))
                .post("http://" + host_ip + "/payment")
                .then()
                .statusCode(200)
                .assertThat().log().body()
                .body("amount", equalTo("10"));
    }

}

