package ru.bms.paymentapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.Account;
import ru.bms.api.Bill;
import ru.bms.api.RuleUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Account account;
    private RuleUnit ruleUnit;
    private Bill bill;
}
