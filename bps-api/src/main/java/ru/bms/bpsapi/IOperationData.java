package ru.bms.bpsapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bms.api.Bill;
import ru.bms.api.InputParameter;

import java.io.IOException;

@Log
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IOperationData extends InputParameter {
    private Bill bill;

    public static IOperationData fromJson(String json) throws IOException {
        log.info("convert json to IOperationData: "+json);
        IOperationData iOperationData = objectMapper.readValue(json, IOperationData.class);
        log.info("convert success.");
        return iOperationData;
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }
}
