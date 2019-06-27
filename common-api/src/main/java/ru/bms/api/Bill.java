package ru.bms.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill extends ApiParameter {
    private BigDecimal sum;

    public static Bill fromJson(String json) throws IOException {
        Bill bill = objectMapper.readValue(json, Bill.class);
        return bill;
    }

    @Override
    public String toString() {
        return super.toString();
    }
//    @Override
//    public String toString() {
//        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
//        return builder.build();
//    }
}
