package ru.bms.handlerservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.handlerservice.service.pilot.SimplePaymentMapper;

@Configuration
@ComponentScan(basePackages = {"ru.bms.testConfig"})
public class HandlerTestConfig {

    @Bean
    public PaymentMapper paymentMapper() {
        return new SimplePaymentMapper();
    }

}
