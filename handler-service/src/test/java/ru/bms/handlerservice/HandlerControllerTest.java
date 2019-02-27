package ru.bms.handlerservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bms.api.BPSClient;
import ru.bms.api.Bill;
import ru.bms.api.HelloResponse;
import ru.bms.api.Terminal;
import ru.bms.bpsapi.BPSPaymentData;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@WebFluxTest
@ContextConfiguration(classes = {HandlerServiceApplication.class, HandlerTestConfig.class})
public class HandlerControllerTest {

    public static final BPSPaymentOperation OPERATION = BPSPaymentOperation.builder()
            .data(BPSPaymentData.builder().bill(Bill.builder().sum(BigDecimal.TEN).build()).build())
            .terminal(Terminal.builder().code("123").build())
            .client(BPSClient.builder().cardNum("0000080012345678").build())
            .build();
    public static final BPSPaymentResponse RESPONSE = BPSPaymentResponse.builder().amount(BigDecimal.TEN).build();
    @Autowired
    WebTestClient webClient;

    @Test
    public void helloTest() {
        webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloResponse.class)
                .isEqualTo(HelloResponse.builder().message("Hello!").build());
    }

    @Test
    public void paymentTest() {
        webClient.post().uri("/payment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(OPERATION))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BPSPaymentResponse.class)
                .isEqualTo(RESPONSE);
    }

}