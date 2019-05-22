package ru.bms.webservice.pilot;

import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.internal.mapping.ObjectMapperSerializationContextImpl;
import io.restassured.mapper.factory.DefaultJackson2ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.*;
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
public class PaymentIntegrationTest {

    public static final String WEB_SERVICE = "web-service";
    public static final String PAYMENT_SERVICE = "payment-service";
    public static final String TERMINAL_SERVICE = "terminal-service";
    public static final String HANDLER_SERVICE = "handler-service";
    public static final String CLIENT_SERVICE = "client-service";
    public static final String BMSGROUP = "bmsgroup/";
    public static final int WEB_SERVICE_PORT = 8080;
    public static final int PAYMENT_SERVICE_PORT = 8080;
    public static final int TERMINAL_SERVICE_PORT = 8080;
    public static final int HANDLER_SERVICE_PORT = 8080;
    public static final int CLIENT_SERVICE_PORT = 8080;

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

    @ClassRule
    static Network network = Network.newNetwork();

    private static Slf4jLogConsumer getConsumer(String webService) {
        return new Slf4jLogConsumer(log).withPrefix(webService);
    }

    private static String getPathToConfig(String serviceName) {
        return "./src/test/resources/docker/"+serviceName+"/config";
    }

//    @Container
//    static GenericContainer webContainer = new GenericContainer(BMSGROUP+WEB_SERVICE)
//            .withNetwork(network)
//            .withNetworkAliases(WEB_SERVICE)
//            .withFileSystemBind("./docker/config", "/config", BindMode.READ_WRITE)
//            .withLogConsumer(getConsumer(WEB_SERVICE))
//            .withExposedPorts(WEB_SERVICE_PORT);

    @Container
    static GenericContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("questionmarks")
            .withUsername("postgres")
            .withPassword("mysecretpassword")
            .withNetwork(network)
            .withNetworkAliases("postgres")
            .withLogConsumer(getConsumer("postgres"))
            .withExposedPorts(5432)
            ;


    private static GenericContainer getGenericContainer(String serviceName, int servicePort) {
        return new GenericContainer(BMSGROUP + serviceName)
                .withNetwork(network)
                .withNetworkAliases(serviceName)

                .withFileSystemBind(getPathToConfig(serviceName), "/config", BindMode.READ_WRITE)
                .withLogConsumer(getConsumer(serviceName))
                .withExposedPorts(servicePort);
    }

    @Container
    static GenericContainer webContainer = getGenericContainer(WEB_SERVICE, WEB_SERVICE_PORT);
    @Container
    static GenericContainer handlerContainer = getGenericContainer(HANDLER_SERVICE, HANDLER_SERVICE_PORT);
    @Container
    static GenericContainer terminalContainer = getGenericContainer(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
    @Container
    static GenericContainer paymentContainer = getGenericContainer(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT);

//    @Container
//    static GenericContainer clientContainer = getGenericContainer(CLIENT_SERVICE, CLIENT_SERVICE_PORT,30);

    private void logContainer(GenericContainer container, String serviceName, int port) {
        log.info(serviceName+" ip Addr = " + container.getContainerIpAddress());
        log.info(serviceName+ " network = " + container.getNetwork());
        container.getNetworkAliases().forEach(s -> log.info(serviceName+" alias: " + s));
        container.getExposedPorts().forEach(i -> log.info(serviceName+" port: " + i));
        container.getPortBindings().forEach(i -> log.info(serviceName+" bind port: " + i));
        log.info(serviceName+" "+port+" mapped " + container.getMappedPort(port));
    }

    @Nested
    class NestedTest {

        @Container
        private final GenericContainer clientContainer = new GenericContainer(BMSGROUP+CLIENT_SERVICE)
                .withNetwork(network)
                .withNetworkAliases(CLIENT_SERVICE)
                .withFileSystemBind(getPathToConfig(CLIENT_SERVICE), "/config", BindMode.READ_WRITE)
                .withLogConsumer(getConsumer(CLIENT_SERVICE))
                .withEnv("WAIT_HOSTS", "postgres:5432")

                .withExposedPorts(CLIENT_SERVICE_PORT);

        @Test
        void nestedTest() {
        logContainer(clientContainer, CLIENT_SERVICE, CLIENT_SERVICE_PORT);
            String address = getIpAddr(webContainer, WEB_SERVICE_PORT)+ "/payment";
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
    }

    @DisplayName("Test web service")
    @Test
    void test() {
        logContainer(postgreSQLContainer, "postgres", 5432);
        logContainer(webContainer, WEB_SERVICE, WEB_SERVICE_PORT);
        logContainer(handlerContainer, HANDLER_SERVICE, HANDLER_SERVICE_PORT);
        logContainer(terminalContainer, TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
        logContainer(paymentContainer, PAYMENT_SERVICE, PAYMENT_SERVICE_PORT);
//        logContainer(clientContainer, CLIENT_SERVICE, CLIENT_SERVICE_PORT);
        String address = getIpAddr(webContainer, WEB_SERVICE_PORT)+ "/payment";
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

    @DisplayName("Test web service 2")
    @Test
    void test2() {
        logContainer(webContainer, WEB_SERVICE, WEB_SERVICE_PORT);
        logContainer(handlerContainer, HANDLER_SERVICE, HANDLER_SERVICE_PORT);
        logContainer(terminalContainer, TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
        logContainer(paymentContainer, PAYMENT_SERVICE, PAYMENT_SERVICE_PORT);
//        logContainer(clientContainer, CLIENT_SERVICE, CLIENT_SERVICE_PORT);
        String address = getIpAddr(webContainer, WEB_SERVICE_PORT)+ "/payment";
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

    @NotNull
    private String getIpAddr(GenericContainer container, int port) {
        return "http://"
                    + container.getContainerIpAddress()
                    + ":" + container.getMappedPort(port) ;
    }

}
