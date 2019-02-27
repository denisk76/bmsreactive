package ru.bms.handlerservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bms.handlerservice.service.ClientService;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.handlerservice.service.PaymentService;
import ru.bms.handlerservice.service.TerminalService;
import ru.bms.handlerservice.service.simple.SimpleClientService;
import ru.bms.handlerservice.service.simple.SimplePaymentMapper;
import ru.bms.handlerservice.service.simple.SimplePaymentService;
import ru.bms.handlerservice.service.simple.SimpleTerminalService;

@Configuration
public class HandlerTestConfig {
    @Bean
    public PaymentService paymentService() {
        return new SimplePaymentService();
    }

    @Bean
    public PaymentMapper paymentMapper() {
        return new SimplePaymentMapper();
    }

    @Bean
    public ClientService clientService() {
        return new SimpleClientService();
    }

    @Bean
    public TerminalService terminalService() {
        return new SimpleTerminalService();
    }
}
