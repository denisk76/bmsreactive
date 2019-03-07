package ru.bms.paymentservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bms.api.Account;
import ru.bms.api.Bill;
import ru.bms.api.HelloResponse;
import ru.bms.api.RuleUnit;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

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

    @Test
    public void paymentTest() {
        webClient.post().uri("/getPayment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                        PaymentRequest.builder()
                                .account(Account.builder().amount(BigDecimal.TEN).build())
                                .bill(Bill.builder().sum(BigDecimal.TEN).build())
                                .ruleUnit(RuleUnit.builder().percent(BigDecimal.valueOf(20)).build())
                                .build()
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponse.class)
                .isEqualTo(PaymentResponse.builder()
                        .bill(Bill.builder().sum(BigDecimal.TEN).build())
                        .account(Account.builder().amount(BigDecimal.TEN).build())
                        .build());
    }

}