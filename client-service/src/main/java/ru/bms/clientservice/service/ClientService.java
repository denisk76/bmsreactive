package ru.bms.clientservice.service;

import ru.bms.clientservice.data.AccountData;

public interface ClientService {
    AccountData findById(long id);
}
