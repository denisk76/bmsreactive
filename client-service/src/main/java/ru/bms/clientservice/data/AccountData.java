package ru.bms.clientservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountData {
    @Id
    private long id;
    private String cardNum;
    private BigDecimal amount;
}
