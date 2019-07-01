package ru.bms.bpsapi;

import lombok.Data;
import lombok.extern.java.Log;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bms.api.ApiRequest;
import ru.bms.api.IClient;
import ru.bms.api.ITerminal;
import ru.bms.api.InputParameter;

import java.io.IOException;

@Data
@Log
public class BPSPaymentOperation extends ApiRequest {

    public void add(InputParamType type, String parameter) {
        add(type.name(), parameter);
    }

    public void add(InputParamType type, InputParameter parameter) {
        add(type.name(), parameter.toString());
    }

    public String get(InputParamType type) {
        return get(type.name());
    }

    public IClient getClient() {
        try {
            return IClient.fromJson(get(InputParamType.CLIENT));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ITerminal getTerminal() {
        try {
            return ITerminal.fromJson(get(InputParamType.TERMINAL));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IOperationData getOperation() {
        try {
            String json = get(InputParamType.OPERATION);
            log.info("json = "+json);
            return IOperationData.fromJson(json);
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
//        CLIENT,
//        TERMINAL,
//        OPERATION
//    }
//    private IClient client;
//    private ITerminal terminal;
//    private IOperationData data;
}
