package ru.bms.handlerservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.bms.api.Bill;
import ru.bms.api.HelloResponse;
import ru.bms.api.IClient;
import ru.bms.api.ITerminal;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.bpsapi.IOperationData;
import ru.bms.bpsapi.InputParamType;
import ru.bms.handlerservice.service.ParamService;

import java.math.BigDecimal;

import static ru.bms.bpsapi.InputParamType.OPERATION;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {HandlerServiceApplication.class, HandlerTestConfig.class})
public class HandlerControllerTest extends BaseTest {

    //    public static final BPSPaymentOperation OPERATION = BPSPaymentOperation.builder()
//            .param(IOperationData.builder().bill(Bill.builder().sum(BigDecimal.TEN).build()).build())
//            .terminal(ITerminal.builder().code("123").build())
//            .client(IClient.builder().cardNum("0000080012345678").build())
//            .build();
    public static final BPSPaymentResponse RESPONSE = BPSPaymentResponse.builder()
            .amount(BigDecimal.TEN)
            .earn(BigDecimal.valueOf(5))
            .spend(BigDecimal.ZERO)
            .build();
    @Autowired
    private ParamService paramService;
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
        BPSPaymentOperation operation = new BPSPaymentOperation();
        operation.add(InputParamType.OPERATION, IOperationData.builder().bill(Bill.builder().sum(BigDecimal.TEN).build()).build());
        operation.add(InputParamType.TERMINAL, ITerminal.builder().code("123").build());
        operation.add(InputParamType.CLIENT, IClient.builder().cardNum("0000080012345678").build());
        webClient.post().uri("/payment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(operation))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BPSPaymentResponse.class)
                .isEqualTo(RESPONSE);
    }

    @Test
    public void paramTest() {
        Mono<String> mono = paramService.getParam("http://localhost:8000", "/getTerminal", "{\"terminal\": {\"code\":\"10\"}}");
        System.out.println("terminalJson = " + mono.block());
    }

}