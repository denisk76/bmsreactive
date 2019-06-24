package ru.bms.clientservice.service.simple;

import lombok.extern.java.Log;
import ru.bms.clientservice.data.AccountData;
import ru.bms.clientservice.service.ClientService;

import java.math.BigDecimal;

@Log
public class ClientServiceSimple implements ClientService {
    @Override
    public AccountData findById(long id) {
        return AccountData.builder().id(1).amount(BigDecimal.TEN).build();
    }

    @Override
    public AccountData findByCardNum(String cardNum) {
        return AccountData.builder().id(1).amount(BigDecimal.TEN).build();
    }

    @Override
    public void add(String cardNum, BigDecimal amount) {
        log.info("Simple add cardNum=" + cardNum);
        return;
    }
}
