package ru.bms.webservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bms.api.Bill;
import ru.bms.api.HelloResponse;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = {WebServiceApplication.class, BPSTestConfig.class})
public class WebServiceApplicationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testHello() throws Exception {
        webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloResponse.class)
                .isEqualTo(HelloResponse.builder().message("Hello, my friend! I`m BPS Controller.").build());
    }

    @Test
    public void testPayment() throws Exception {
        webClient.post().uri("/payment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                        PutPaymentRequest.builder()
                                .cardNum("1234")
                                .terminalCode("345t")
                                .bill(Bill.builder().sum(BigDecimal.TEN).build())
                                .build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PutPaymentResponse.class)
                .isEqualTo(PutPaymentResponse.builder()
                        .amount(BigDecimal.TEN)
                        .earn(BigDecimal.ONE)
                        .spend(BigDecimal.ONE)
                        .build());
    }

}

