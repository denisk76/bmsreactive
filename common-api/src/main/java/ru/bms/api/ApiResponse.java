package ru.bms.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class ApiResponse {
    @Singular Map<String, String> params;

    public ApiResponse() {
        params = new HashMap<>();
    }

    public ApiResponse(Map<String, String> params) {
        this.params = params;
    }

//    public ApiResponse(Map<String, ApiParameter> params) {
//        this.params = params.entrySet().stream()
//                .collect(Collectors.toMap(
//                        e -> e.getKey(),
//                        e -> e.getValue().toString()
//                ));
//    }

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
