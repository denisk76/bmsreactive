package ru.bms.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class ApiRequest {
    @Singular public Map<String, String> params;

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

    public ApiRequest(Map<String, String> params) {
        this.params = params;
    }

    public void add(String name, ApiParameter parameter) {
        params.put(name, parameter.toString());
    }

    public String get(String name) {
        return params.get(name);
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }

}
