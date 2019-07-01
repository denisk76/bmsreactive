package ru.bms.paymentapi;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bms.api.*;

import java.io.IOException;

@Data
public class PaymentRequest extends ApiRequest {
    public PaymentRequest() {
        super();
    }

    public String get(ApiParamType type) {
        return get(type.name());
    }

    public void add(ApiParamType type, String parameter) {
        add(type.name(), parameter);
    }

    public Bill getBill() {
        try {
            return Bill.fromJson(get(ApiParamType.BILL));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account getAccount() {
        try {
            return Account.fromJson(get(ApiParamType.ACCOUNT));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RuleUnit getRuleUnit() {
        try {
            return RuleUnit.fromJson(get(ApiParamType.RULE_UNIT));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }
//    public enum ParamType {
//        ACCOUNT,
//        BILL,
//        RULE_UNIT
//    }
}
