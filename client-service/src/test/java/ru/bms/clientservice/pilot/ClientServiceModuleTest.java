package ru.bms.clientservice.pilot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bms.ClientRequest;
import ru.bms.ClientResponse;
import ru.bms.api.Account;
import ru.bms.api.BPSClient;
import ru.bms.api.HelloResponse;
import ru.bms.clientservice.ClientServiceApplication;

import java.math.BigDecimal;

import static ru.bms.PostgresConfig.*;

@Testcontainers
@Slf4j
@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ClientServiceApplication.class, ModuleTestConfig.class})
public class ClientServiceModuleTest {
    @Container
    static GenericContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName(POSTGRES_DATABASE_NAME)
            .withUsername(POSTGRES_USERNAME)
            .withPassword(POSTGRES_PASSWORD)
            .withFileSystemBind("../postgres/init", "/docker-entrypoint-initdb.d", BindMode.READ_WRITE)
            .withNetworkAliases("postgres")
            .withLogConsumer(getConsumer("postgres"))
            .withExposedPorts(POSTGRES_PORT);

    private static Slf4jLogConsumer getConsumer(String webService) {
        return new Slf4jLogConsumer(log).withPrefix(webService);
    }

    @Autowired
    WebTestClient webClient;


    @Test
    @Transactional
    public void helloTest() {
        webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloResponse.class)
                .isEqualTo(HelloResponse.builder().message("Hello, my friend! I`m Client Controller.").build());
    }

    @Test
    public void getClientTest() {
        webClient.post().uri("/getClient").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                        ClientRequest.builder()
                                .client(BPSClient.builder().cardNum("00000800012345678").build())
                                .build()
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponse.class)
                .isEqualTo(ClientResponse.builder().account(Account.builder().amount(BigDecimal.TEN).build()).build());
    }
}
