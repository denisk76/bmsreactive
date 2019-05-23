package ru.bms.clientservice.service.pilot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bms.clientservice.dao.AccountDataRepository;
import ru.bms.clientservice.data.AccountData;
import ru.bms.clientservice.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private AccountDataRepository accountRepository;

//    @Autowired
//    public ClientServiceImpl(AccountDataRepository accountRepository) {
//        this.accountRepository = accountRepository;
//    }

    @Override
    public AccountData findById(long id) {
        return accountRepository.findById(id).orElseThrow(() -> new IllegalStateException("the account is not there"));
    }
}
