package ru.bms.clientservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.bms.ClientResponse;
import ru.bms.api.Account;
import ru.bms.api.HelloResponse;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = ClientServiceApplication.class)
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
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponse.class)
                .isEqualTo(ClientResponse.builder().account(Account.builder().amount(BigDecimal.TEN).build()).build());
    }

}
