package ru.bms.handlerservice;

import lombok.Builder;
import lombok.Data;
import ru.bms.paymentapi.PaymentRequest;

@Data
@Builder
public class ConfigLine {
    private PaymentRequest.ParamType paramType;
    private String url;
    private String method;
}
