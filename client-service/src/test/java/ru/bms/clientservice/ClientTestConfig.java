package ru.bms.clientservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bms.clientservice.service.ClientService;
import ru.bms.clientservice.service.simple.ClientServiceSimple;

@Configuration
public class ClientTestConfig {
    @Bean
    public ClientService clientService() {
        return new ClientServiceSimple();
    }
}
