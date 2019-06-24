package ru.bms.clientservice.service.pilot;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bms.clientservice.dao.AccountDataRepository;
import ru.bms.clientservice.data.AccountData;
import ru.bms.clientservice.service.ClientService;

import java.math.BigDecimal;

@Service
@Log
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

    @Override
    public AccountData findByCardNum(String cardNum) {
        return accountRepository.findByCardNum(cardNum);
    }

    @Override
    public void add(String cardNum, BigDecimal amount) {
        AccountData accountData = new AccountData();
        accountData.setAmount(amount);
        accountData.setCardNum(cardNum);
        log.info("save account with cardNum = " + cardNum);
        accountRepository.save(accountData);
    }
}
