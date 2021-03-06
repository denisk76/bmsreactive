package ru.bms.webservice.pilot;

import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.internal.mapping.ObjectMapperSerializationContextImpl;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bms.PaymentIntegrationConst;
import ru.bms.UpdateConfigRequest;
import ru.bms.bpsapi.InputParamType;
import ru.bms.service.ClientService;
import ru.bms.service.HandlerService;
import ru.bms.service.TerminalService;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.bms.PaymentIntegrationConst.REQUEST;
import static ru.bms.PaymentIntegrationConst.RESPONSE;
import static ru.bms.PostgresConfig.*;
import static ru.bms.webservice.IsEqualJSON.equalToJson;

@Testcontainers
@Slf4j
@WebFluxTest
@ContextConfiguration(classes = {PilotTestConfig.class})
public class PaymentIntegrationTest {

    public static final String TEST_RESOURCES_DOCKER = "./src/test/resources/docker/";
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

    @Autowired
    TerminalService terminalService;
    @Autowired
    ClientService clientService;
    @Autowired
    HandlerService handlerService;

    @ClassRule
    static Network network = Network.newNetwork();


    private static Slf4jLogConsumer getConsumer(String webService) {
        return new Slf4jLogConsumer(log).withPrefix(webService);
    }

    private static String getPathToConfig(String serviceName) {
        return TEST_RESOURCES_DOCKER + serviceName + "/config";
    }

    @Container
    static GenericContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName(POSTGRES_DATABASE_NAME)
            .withUsername(POSTGRES_USERNAME)
            .withPassword(POSTGRES_PASSWORD)
            .withNetwork(network)
            .withFileSystemBind(TEST_RESOURCES_DOCKER + "/postgreSQL/init", "/docker-entrypoint-initdb.d", BindMode.READ_WRITE)
            .withNetworkAliases("postgres")
            .withLogConsumer(getConsumer("postgres"))
            .withExposedPorts(5432);


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


    private void logContainer(GenericContainer container, String serviceName, int port) {
        log.info(serviceName + " ip Addr = " + container.getContainerIpAddress());
        log.info(serviceName + " network = " + container.getNetwork());
        container.getNetworkAliases().forEach(s -> log.info(serviceName + " alias: " + s));
        container.getExposedPorts().forEach(i -> log.info(serviceName + " port: " + i));
        container.getPortBindings().forEach(i -> log.info(serviceName + " bind port: " + i));
        log.info(serviceName + " " + port + " mapped " + container.getMappedPort(port));
    }

    private String convertObjectToString(Object o) {
        Jackson2Mapper jackson2Mapper = new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory());
        ObjectMapperSerializationContextImpl context = new ObjectMapperSerializationContextImpl();
        context.setObject(o);
        String serialize = jackson2Mapper.serialize(context);
        System.out.println("serialize = " + serialize);
        return serialize;
    }

    @Nested
    class NestedTest {

        @Container
        private final GenericContainer clientContainer = getGenericContainer(CLIENT_SERVICE, CLIENT_SERVICE_PORT)
                .withEnv("WAIT_HOSTS", "postgres:5432");

        @DisplayName("Test web service")
        @Test
        void nestedTest() {
            logContainer(postgreSQLContainer, "postgres", 5432);
            logContainer(webContainer, WEB_SERVICE, WEB_SERVICE_PORT);
            logContainer(handlerContainer, HANDLER_SERVICE, HANDLER_SERVICE_PORT);
            logContainer(terminalContainer, TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
            logContainer(paymentContainer, PAYMENT_SERVICE, PAYMENT_SERVICE_PORT);
            logContainer(clientContainer, CLIENT_SERVICE, CLIENT_SERVICE_PORT);

            terminalService.setIpAddr(getIpAddr(terminalContainer, TERMINAL_SERVICE_PORT));
            clientService.setIpAddr(getIpAddr(clientContainer, CLIENT_SERVICE_PORT));
            handlerService.setIpAddr(getIpAddr(handlerContainer, HANDLER_SERVICE_PORT));

            terminalService.addTerminals(PaymentIntegrationConst.CLUB_DATA);
            clientService.addClients(PaymentIntegrationConst.CLIENTS_DATA);
            handlerService.updateConfig(UpdateConfigRequest.builder()
                    .inputParamType(InputParamType.CLIENT.toString())
                    .url("http://client-service:8080")
//                    .url(getIpAddr(clientContainer, CLIENT_SERVICE_PORT))
                    .build());
            handlerService.updateConfig(UpdateConfigRequest.builder()
                    .inputParamType(InputParamType.TERMINAL.toString())
                    .url("http://terminal-service:8080")
//                    .url(getIpAddr(terminalContainer, TERMINAL_SERVICE_PORT))
                    .build());

            try {
                JSONArray requests = new JSONArray(REQUEST);
                JSONArray responses = new JSONArray(RESPONSE);
                for (int i = 0; i < requests.length(); i++) {
                    given().body(requests.get(i).toString())
                            .contentType("application/json")
                            .post(getIpAddr(webContainer, WEB_SERVICE_PORT) + "/payment")
                            .then().log().all()
                            .statusCode(200)
                            .assertThat()
                            .log().body()
                            .body("", equalToJson(responses.get(i).toString()))
                    ;
                }
            } catch (JSONException e) {
                assertTrue(false, "Data not parse to Json.");
            }
        }
    }

    @NotNull
    private String getIpAddr(GenericContainer container, int port) {
        return "http://"
                + container.getContainerIpAddress()
                + ":" + container.getMappedPort(port);
    }

}
