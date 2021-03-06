package ru.bms.webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.bms.api.Bill;
import ru.bms.api.HelloResponse;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {WebServiceApplication.class, BPSTestConfig.class})
public class WebServiceApplicationTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void JsonTest() {
        String s = "[{" +
                "\"terminalCode\":\"10\"," +
                "\"percent\":10" +
                "}," +
                "{" +
                "\"terminalCode\":\"20\"," +
                "\"percent\":20" +
                "}]";
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHello() throws Exception {
        webTestClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloResponse.class)
                .isEqualTo(HelloResponse.builder().message("Hello, my friend! I`m BPS Controller.").build());
    }

    @Test
    public void testPayment() throws Exception {
        PutPaymentRequest body = PutPaymentRequest.builder()
                .cardNum("1234")
                .terminalCode("345t")
                .bill(Bill.builder().sum(BigDecimal.TEN).build())
                .build();
        System.out.println("body = " + body);
        webTestClient.post().uri("/payment").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(
                        body.toString()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PutPaymentResponse.class)
                .isEqualTo(PutPaymentResponse.builder()
                        .amount(BigDecimal.TEN)
                        .earn(BigDecimal.valueOf(3))
                        .spend(BigDecimal.valueOf(4))
                        .build());
    }

}

