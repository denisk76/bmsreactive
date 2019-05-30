package ru.bms.paymentapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.bms.api.AbstractDto;
import ru.bms.api.Account;
import ru.bms.api.Bill;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse{
    private Account account;
    private Bill bill;
    @Override
    public String toString() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE);
        return builder.build();
    }
}
