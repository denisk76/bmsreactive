package ru.bms.bpsapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
