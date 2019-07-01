package ru.bms.webservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.webservice.service.PaymentMapper;
import ru.bms.webservice.service.PaymentService;
import ru.bms.webservice.service.pilot.PilotPaymentService;
import ru.bms.webservice.service.simple.SimplePaymentMapper;

@Configuration
public class BPSConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://handler-service:8080").build();
    }

    @Bean
    public PaymentMapper paymentMapper() {
        return new SimplePaymentMapper();
    }

    @Bean
    public PaymentService paymentService() {
        return new PilotPaymentService();
    }

}
