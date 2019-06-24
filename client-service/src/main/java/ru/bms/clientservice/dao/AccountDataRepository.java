package ru.bms.clientservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.bms.clientservice.data.AccountData;


@Repository
@Transactional
public interface AccountDataRepository extends CrudRepository<AccountData, Long> {
    AccountData findByCardNum(String cardNum);
}
