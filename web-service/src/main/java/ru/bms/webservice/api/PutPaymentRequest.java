package ru.bms.webservice.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bms.api.Bill;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutPaymentRequest {
    /*номер карты клиента*/
    private String cardNum;
    /*код терминала по которому проходит операция*/
    private String terminalCode;
    /*чек с перечнем товаров*/
    private Bill bill;
}
