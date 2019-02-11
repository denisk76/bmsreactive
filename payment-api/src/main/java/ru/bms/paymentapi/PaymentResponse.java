package ru.bms.paymentapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.Account;
import ru.bms.api.Bill;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Account account;
    private Bill bill;
}
