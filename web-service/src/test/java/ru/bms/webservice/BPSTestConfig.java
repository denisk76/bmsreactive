package ru.bms.webservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bms.webservice.service.PaymentMapper;
import ru.bms.webservice.service.PaymentService;
import ru.bms.webservice.service.simple.SimplePaymentMapper;
import ru.bms.webservice.service.simple.SimplePaymentService;

@Configuration
public class BPSTestConfig {
    @Bean
    public PaymentMapper paymentMapper() {
        return new SimplePaymentMapper();
    }

    @Bean
    public PaymentService paymentService() {
        return new SimplePaymentService();
    }
}
