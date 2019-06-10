package ru.bms.webservice.pilot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class ContainerIntegrationTest {



@Container
public static GenericContainer compose
        = new GenericContainer("alpine:3.2")
        .withExposedPorts(80)
        .withCommand("/bin/sh", "-c", "while true; do echo "
                + "\"HTTP/1.1 200 OK\n\nHello World!\" | nc -l -p 80; done");
    @Test
    @DisplayName("Alpine Test")
    void test() {
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
