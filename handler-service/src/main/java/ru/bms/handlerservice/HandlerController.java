package ru.bms.handlerservice;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.api.HelloResponse;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;

import java.math.BigDecimal;

@RestController
@Log
public class HandlerController {
    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello!").build());
    }


    @PostMapping("/payment")
    public Mono<BPSPaymentResponse> payment(@RequestBody BPSPaymentOperation request) {
        log.info("post /payment");
        log.info(request.toString());
        return Mono.just(BPSPaymentResponse.builder()
                .amount(BigDecimal.TEN)
                .build());
    }
}