package ru.bms.webservice.pilot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

//@Testcontainers
@Slf4j
public class ContainerIntegrationTest {



    public static final String WEB_SERVICE = "web-service_1";
    public static final String PAYMENT_SERVICE = "payment-service_1";
    public static final String TERMINAL_SERVICE = "terminal-service_1";
    public static final String HANDLER_SERVICE = "handler-service_1";
    public static final int WEB_SERVICE_PORT = 8080;
    public static final int PAYMENT_SERVICE_PORT = 8080;
    public static final int TERMINAL_SERVICE_PORT = 8080;
    public static final int HANDLER_SERVICE_PORT = 8080;

//    @Container
//    static DockerComposeContainer compose =
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
//@Container
public static GenericContainer compose
        = new GenericContainer("alpine:3.2")
        .withExposedPorts(80)
        .withCommand("/bin/sh", "-c", "while true; do echo "
                + "\"HTTP/1.1 200 OK\n\nHello World!\" | nc -l -p 80; done");
//    @Test
//    @DisplayName("Test 1 2 3")
    void test() {
//        log.debug(compose.getServiceHost(WEB_SERVICE, WEB_SERVICE_PORT));
        log.info("container id: "+compose.getContainerId());
        log.info("container name: "+compose.getContainerName());
        log.info("container image name: "+compose.getDockerImageName());
        log.info("container ipAddr: "+compose.getContainerIpAddress());
        compose.getPortBindings().forEach(s -> log.info("binding port: "+s));
        compose.getExposedPorts().forEach(s -> log.info("exposed port: "+s));
        compose.getEnvMap().entrySet().forEach(s -> log.info("env: "+s));
        log.info("container network mode: "+compose.getNetworkMode());
        log.info("container network: "+compose.getNetwork());
    }
}
