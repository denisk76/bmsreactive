package ru.bms.webservice.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutPaymentResponse {
    /*текущий баланс клиента*/
    private BigDecimal amount;
    /*накоплено в результате операции*/
    private BigDecimal earn;
    /*потрачено в результате операции*/
    private BigDecimal spend;
}
