package ru.bms.paymentapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bms.api.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Data
public class PaymentRequest extends ApiRequest {
    public PaymentRequest() {
        super();
    }

    public PaymentRequest(Map<String, String> params) {
        super(params);
    }

    public enum ParamType {
        ACCOUNT,
        BILL,
        RULE_UNIT;

    }
    public void add(ParamType type, ApiParameter parameter) {
        add(type.name(), parameter);
    }

    public void get(ParamType type) {
        get(type.name());
    }
    public Bill getBill() {
        try {
            return Bill.fromJson(params.get(ParamType.BILL.name()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Account getAccount() {
        try {
            return Account.fromJson( params.get(ParamType.ACCOUNT.name()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public RuleUnit getRuleUnit() {
        try {
            return RuleUnit.fromJson( params.get(ParamType.RULE_UNIT.name()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
