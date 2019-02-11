package ru.bms.bpsapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.Bill;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BPSPaymentData {
    private Bill bill;
}
