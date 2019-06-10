package ru.bms.bpsapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bms.api.BPSClient;
import ru.bms.api.Terminal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BPSPaymentOperation {
    private BPSClient client;
    private Terminal terminal;
    private BPSPaymentData data;
    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }
}
