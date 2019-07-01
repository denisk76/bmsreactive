package ru.bms.webservice.service.simple;

import ru.bms.api.Bill;
import ru.bms.api.IClient;
import ru.bms.api.ITerminal;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.bpsapi.IOperationData;
import ru.bms.bpsapi.InputParamType;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;
import ru.bms.webservice.service.PaymentMapper;

import static ru.bms.bpsapi.InputParamType.CLIENT;

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
        BPSPaymentOperation operation = new BPSPaymentOperation();
        operation.add(InputParamType.CLIENT, IClient.builder().cardNum(request.getCardNum()).build());
        operation.add(InputParamType.TERMINAL, ITerminal.builder().code(request.getTerminalCode()).build());
        operation.add(InputParamType.OPERATION, IOperationData.builder().bill(Bill.builder().sum(request.getBill().getSum()).build()).build());
        return operation;
    }
}
