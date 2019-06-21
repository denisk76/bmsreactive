package ru.bms.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ApiParameter {
    protected static ObjectMapper objectMapper = new ObjectMapper();

    public ApiParameter() {
        objectMapper = new ObjectMapper();
    }
}
