package ru.bms.webservice.pilot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

//@Testcontainers
@Slf4j
public class ComposerIntegrationTest {



    public static final String WEB_SERVICE = "web-service_1";
    public static final String PAYMENT_SERVICE = "payment-service_1";
    public static final String TERMINAL_SERVICE = "terminal-service_1";
    public static final String HANDLER_SERVICE = "handler-service_1";
    public static final int WEB_SERVICE_PORT = 8080;
    public static final int PAYMENT_SERVICE_PORT = 8080;
    public static final int TERMINAL_SERVICE_PORT = 8080;
    public static final int HANDLER_SERVICE_PORT = 8080;

//    @Container
    static DockerComposeContainer compose =
            new DockerComposeContainer(
                    new File("src/test/resources/simple-compose.yml")
            )
                    .withPull(false)
                    .withLogConsumer(WEB_SERVICE, new Slf4jLogConsumer(log).withPrefix(WEB_SERVICE))
                    .withExposedService(WEB_SERVICE, WEB_SERVICE_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(40)))
                    .withExposedService(HANDLER_SERVICE, HANDLER_SERVICE_PORT)
                    .withExposedService(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT)
                    .withExposedService(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT)
            ;

//    @Test
    @DisplayName("Test 1 2 3")
    void test() {
        logInfo(WEB_SERVICE, WEB_SERVICE_PORT);
        logInfo(HANDLER_SERVICE, HANDLER_SERVICE_PORT);
        logInfo(PAYMENT_SERVICE, PAYMENT_SERVICE_PORT);
        logInfo(TERMINAL_SERVICE, TERMINAL_SERVICE_PORT);
    }

    private void logInfo(String name, int port) {
        log.info(name+" host: "+compose.getServiceHost(name, port));
        log.info(name+" port: "+compose.getServicePort(name, port).toString());
    }
}
