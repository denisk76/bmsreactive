package ru.bms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.BPSClient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequest {
    private BPSClient client;
}
