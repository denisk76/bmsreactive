package ru.bms.bpsapi;

import lombok.Data;
import ru.bms.api.ApiRequest;
import ru.bms.api.IClient;
import ru.bms.api.ITerminal;
import ru.bms.api.InputParameter;

import java.io.IOException;

@Data
public class BPSPaymentOperation extends ApiRequest {

    public void add(ParamType type, InputParameter parameter) {
        add(type.name(), parameter);
    }

    public String get(ParamType type) {
        return get(type.name());
    }

    public IClient getClient() {
        try {
            return IClient.fromJson(get(ParamType.CLIENT));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ITerminal getTerminal() {
        try {
            return ITerminal.fromJson(get(ParamType.TERMINAL));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IOperationData getOperation() {
        try {
            return IOperationData.fromJson(get(ParamType.OPERATION));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum ParamType {
        CLIENT,
        TERMINAL,
        OPERATION
    }
//    private IClient client;
//    private ITerminal terminal;
//    private IOperationData data;
}
