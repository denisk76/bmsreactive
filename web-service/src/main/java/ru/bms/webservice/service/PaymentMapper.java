package ru.bms.webservice.service;

import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;

/* Преобразование форматов*/
public interface PaymentMapper {
    /*Формат ответа*/
    PutPaymentResponse mapResponse(BPSPaymentResponse response);

    /*Формат запроса*/
    BPSPaymentOperation mapRequest(PutPaymentRequest request);
}
