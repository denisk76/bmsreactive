package ru.bms.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bms.api.Account;
import ru.bms.api.HelloResponse;
import ru.bms.api.IClient;

import java.math.BigDecimal;

@WebFluxTest
@ContextConfiguration(classes = {ClientServiceApplication.class, ClientTestConfig.class})
public class ClientServiceApplicationTests {

    @Autowired
    WebTestClient webClient;

    @Test
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
                        IClient.builder().cardNum("00000800012345678").build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
                .isEqualTo(Account.builder().amount(BigDecimal.TEN).build());
    }

}
