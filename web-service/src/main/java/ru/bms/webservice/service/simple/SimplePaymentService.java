package ru.bms.webservice.service.simple;

import reactor.core.publisher.Mono;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.webservice.service.PaymentService;

import java.math.BigDecimal;

public class SimplePaymentService implements PaymentService {
    public Mono<BPSPaymentResponse> payment(BPSPaymentOperation request) {
        return Mono.just(BPSPaymentResponse.builder()
                .amount(BigDecimal.TEN)
                .build());
    }
}
