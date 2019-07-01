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
import ru.bms.api.*;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

import static ru.bms.DecimalUtils.d;

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
        PaymentRequest request = new PaymentRequest();
        request.add(ApiParamType.ACCOUNT, Account.builder().amount(d(10)).build().toString());
        request.add(ApiParamType.BILL, Bill.builder().sum(d(25)).build().toString());
        request.add(ApiParamType.RULE_UNIT, RuleUnit.builder().percent(d(20)).build().toString());

        PaymentResponse response = new PaymentResponse();
        response.add(PaymentResponse.ParamType.BILL, Bill.builder().sum(BigDecimal.valueOf(25)).build());
        response.add(PaymentResponse.ParamType.ACCOUNT, Account.builder().amount(d(15)).build());
        response.add(PaymentResponse.ParamType.DELTA, Delta.builder().spend(BigDecimal.ZERO).earn(BigDecimal.valueOf(5)).build());

        webClient.post().uri("/getPayment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                        request
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentResponse.class)
                .isEqualTo(response);
    }

}