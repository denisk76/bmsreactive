package ru.bms.handlerservice.service.simple;

import reactor.core.publisher.Mono;
import ru.bms.api.Account;
import ru.bms.api.Bill;
import ru.bms.handlerservice.service.PaymentService;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

public class SimplePaymentService implements PaymentService {
    @Override
    public Mono<PaymentResponse> payment(PaymentRequest request) {
        return Mono.just(PaymentResponse.builder()
                .account(Account.builder().amount(BigDecimal.TEN).build())
                .bill(Bill.builder().sum(BigDecimal.TEN).build())
                .build());
    }
}
