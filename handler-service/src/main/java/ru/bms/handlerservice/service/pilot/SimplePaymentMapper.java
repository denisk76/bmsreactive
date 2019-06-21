package ru.bms.handlerservice.service.pilot;

import ru.bms.api.Account;
import ru.bms.api.RuleUnit;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

import java.math.BigDecimal;

public class SimplePaymentMapper implements PaymentMapper {
    @Override
    public PaymentRequest mapRequest(BPSPaymentOperation operation) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.add(PaymentRequest.ParamType.RULE_UNIT, RuleUnit.builder().percent(BigDecimal.valueOf(20)).build());
        paymentRequest.add(PaymentRequest.ParamType.BILL, operation.getData().getBill());
        paymentRequest.add(PaymentRequest.ParamType.ACCOUNT, Account.builder().amount(BigDecimal.TEN).build());
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
