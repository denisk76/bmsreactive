package ru.bms.clientservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bms.clientservice.dao.AccountDataRepository;
import ru.bms.clientservice.data.AccountData;

@Service
public class ClientService {
    private final AccountDataRepository accountRepository;

    @Autowired
    public ClientService(AccountDataRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountData findById(long id) {
        return accountRepository.findById(id).orElseThrow(() -> new IllegalStateException("the account is not there"));
    }
}
