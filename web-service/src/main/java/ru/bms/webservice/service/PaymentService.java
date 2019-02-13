package ru.bms.webservice.service;

import reactor.core.publisher.Mono;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;

/* Платёж. Обращаемся к модулю Handler*/
public interface PaymentService {
    /*Проводим Платёж*/
    Mono<BPSPaymentResponse> payment(BPSPaymentOperation request);
}
