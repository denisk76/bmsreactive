package ru.bms.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ApiParameter {
    protected static ObjectMapper objectMapper;

    public ApiParameter() {
        objectMapper = new ObjectMapper();
    }
}
