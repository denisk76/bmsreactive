package ru.bms.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.extern.java.Log;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@Log
public class ApiRequest {
    @Singular
    public Map<String, String> params;

    public ApiRequest() {
        params = new HashMap<>();
    }

//    public ApiRequest(Map<String, ApiParameter> params) {
//        this.params = params.entrySet().stream()
//                .collect(Collectors.toMap(
//                        e -> e.getKey(),
//                        e -> e.getValue().toString()
//                ));
//    }

//    public ApiRequest(Map<String, String> params) {
//        this.params = params;
//    }

    public void add(String name, String parameter) {
        params.put(name, parameter);
    }

    public String get(String name) {
        log.info("get param by name: " + name);
        return params.get(name);
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }

}
