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
        return PaymentRequest.builder()
                .ruleUnit(RuleUnit.builder().percent(BigDecimal.valueOf(20)).build())
                .bill(operation.getData().getBill())
                .account(Account.builder().amount(BigDecimal.TEN).build())
                .build();
    }

    @Override
    public BPSPaymentResponse mapResponse(PaymentResponse response) {
        return BPSPaymentResponse.builder()
                .amount(response.getAccount().getAmount())
                .earn(response.getEarn())
                .spend(response.getSpend())
                .build();
    }
}
