package ru.bms.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IClient extends InputParameter {
    private String cardNum;

    public static IClient fromJson(String json) throws IOException {
        return objectMapper.readValue(json, IClient.class);
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }
}
