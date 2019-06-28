package ru.bms.bpsapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bms.api.Bill;
import ru.bms.api.InputParameter;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IOperationData extends InputParameter {
    private Bill bill;

    public static IOperationData fromJson(String json) throws IOException {
        return objectMapper.readValue(json, IOperationData.class);
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }
}
