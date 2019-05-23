package ru.bms.clientservice.service.simple;

import ru.bms.clientservice.data.AccountData;
import ru.bms.clientservice.service.ClientService;

import java.math.BigDecimal;

public class ClientServiceSimple implements ClientService {
    @Override
    public AccountData findById(long id) {
        return AccountData.builder().id(1).amount(BigDecimal.TEN).build();
    }
}
