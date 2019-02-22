package ru.bms.paymentservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.bms.api.HelloResponse;

@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = PaymentServiceApplication.class)
public class PaymentControllerTest {

    @Autowired
    WebTestClient webClient;

    @Test
    public void hello() {
        webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloResponse.class)
                .isEqualTo(HelloResponse.builder().message("Hello").build());
    }

}