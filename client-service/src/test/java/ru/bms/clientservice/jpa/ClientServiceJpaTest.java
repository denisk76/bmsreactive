package ru.bms.clientservice.jpa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bms.clientservice.ClientServiceApplication;
import ru.bms.clientservice.dao.AccountDataRepository;
import ru.bms.clientservice.data.AccountData;
import ru.bms.clientservice.service.ClientService;

import java.math.BigDecimal;

import static ru.bms.PostgresConfig.*;

@Testcontainers
@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {ClientServiceApplication.class, JpaTestConfig.class})
public class ClientServiceJpaTest {
    @Container
    static GenericContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName(POSTGRES_DATABASE_NAME)
            .withUsername(POSTGRES_USERNAME)
            .withPassword(POSTGRES_PASSWORD)
            .withFileSystemBind("../postgres/init", "/docker-entrypoint-initdb.d", BindMode.READ_WRITE)
            .withNetworkAliases("postgres")
            .withLogConsumer(getConsumer("postgres"))
            .withExposedPorts(POSTGRES_PORT);

    private static Slf4jLogConsumer getConsumer(String webService) {
        return new Slf4jLogConsumer(log).withPrefix(webService);
    }

    @Autowired
    private AccountDataRepository accountDataRepository;

    @Autowired
    private ClientService clientService;

    @Test
    public void helloTest() {
        clientService.add("0000080012341234", BigDecimal.TEN);
        long count = accountDataRepository.count();
        log.info("accounts count = " + count);
        AccountData accountData = clientService.findByCardNum("0000080012341234");
        log.info("amount for account with id = 1: " + accountData.getAmount());
    }
}
