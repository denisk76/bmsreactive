package ru.bms.paymentapi;

import lombok.Data;
import ru.bms.api.*;

import java.io.IOException;
import java.util.HashMap;

@Data
public class PaymentResponse extends ApiResponse {
    public PaymentResponse() {
        super(new HashMap<>());
    }

//    public PaymentResponse(Map<String, String> params) {
//        super(params);
//    }

    public Bill getBill() {
        try {
            return Bill.fromJson(get(ParamType.BILL.name()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void add(ParamType type, ApiParameter parameter) {
        add(type.name(), parameter);
    }

    public String get(ParamType type) {
        return get(type.name());
    }

    public Account getAccount() {
        try {
            return Account.fromJson(get(ParamType.ACCOUNT.name()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum ParamType {
        ACCOUNT,
        BILL,
        DELTA
    }

    public Delta getDelta() {
        try {
            return Delta.fromJson(get(ParamType.DELTA.name()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
