package ru.bms.handlerservice.service;

import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

public interface PaymentMapper {
    PaymentRequest mapRequest(BPSPaymentOperation operation);

    BPSPaymentResponse mapResponse(PaymentResponse response);
}
