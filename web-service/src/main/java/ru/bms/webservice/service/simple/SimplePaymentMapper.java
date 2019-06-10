package ru.bms.webservice.service.simple;

import ru.bms.api.BPSClient;
import ru.bms.api.Bill;
import ru.bms.api.Terminal;
import ru.bms.bpsapi.BPSPaymentData;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;
import ru.bms.webservice.service.PaymentMapper;

public class SimplePaymentMapper implements PaymentMapper {
    @Override
    public PutPaymentResponse mapResponse(BPSPaymentResponse response) {
        return PutPaymentResponse.builder()
                .amount(response.getAmount())
                .spend(response.getSpend())
                .earn(response.getEarn())
                .build();
    }

    @Override
    public BPSPaymentOperation mapRequest(PutPaymentRequest request) {
        return BPSPaymentOperation.builder()
                .client(BPSClient.builder().cardNum(request.getCardNum()).build())
                .terminal(Terminal.builder().code(request.getTerminalCode()).build())
                .data(BPSPaymentData.builder().bill(Bill.builder().sum(request.getBill().getSum()).build()).build())
                .build();
    }
}
