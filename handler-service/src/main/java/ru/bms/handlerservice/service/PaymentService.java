package ru.bms.handlerservice.service;

import reactor.core.publisher.Mono;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

public interface PaymentService {
    Mono<PaymentResponse> payment(PaymentRequest request);
}
