package ru.bms.clientservice.service;

import ru.bms.clientservice.data.AccountData;

import java.math.BigDecimal;

public interface ClientService {
    AccountData findById(long id);

    AccountData findByCardNum(String cardNum);

    void add(String cardNum, BigDecimal amount);
}
