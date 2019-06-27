package ru.bms.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class InputParameter {
    protected static ObjectMapper objectMapper;

    public InputParameter() {
        objectMapper = new ObjectMapper();
    }
}
