package ru.bms.handlerservice.service.pilot;

import lombok.extern.java.Log;
import ru.bms.api.Account;
import ru.bms.api.ApiParamType;
import ru.bms.api.RuleUnit;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

@Log
public class SimplePaymentMapper implements PaymentMapper {
    @Override
    public PaymentRequest mapRequest(BPSPaymentOperation operation) {
        if(operation == null) {
            log.info("operation is null !!!");
        } else {
            log.info(operation.toString());
        }
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.add(ApiParamType.RULE_UNIT, "");
        paymentRequest.add(ApiParamType.BILL, operation.getOperation().getBill().toString());
        paymentRequest.add(ApiParamType.ACCOUNT, "");
        log.info("map request success");
        return paymentRequest;
    }

    @Override
    public BPSPaymentResponse mapResponse(PaymentResponse response) {
        return BPSPaymentResponse.builder()
                .amount(response.getAccount().getAmount())
                .earn(response.getDelta().getEarn())
                .spend(response.getDelta().getSpend())
                .build();
    }
}
