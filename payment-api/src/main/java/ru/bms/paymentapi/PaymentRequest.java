package ru.bms.paymentapi;

import lombok.Data;
import ru.bms.api.*;

import java.io.IOException;

@Data
public class PaymentRequest extends ApiRequest {
    public PaymentRequest() {
        super();
    }

    public String get(ParamType type) {
        return get(type.name());
    }

    public void add(ParamType type, ApiParameter parameter) {
        add(type.name(), parameter);
    }

    public Bill getBill() {
        try {
            return Bill.fromJson(get(ParamType.BILL));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account getAccount() {
        try {
            return Account.fromJson(get(ParamType.ACCOUNT));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RuleUnit getRuleUnit() {
        try {
            return RuleUnit.fromJson(get(ParamType.RULE_UNIT));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum ParamType {
        ACCOUNT,
        BILL,
        RULE_UNIT
    }
}
