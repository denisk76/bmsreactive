package ru.bms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.paymentapi.service.PaymentService;
import ru.bms.paymentapi.service.pilot.PilotPaymentService;

@Configuration
public class PaymentConfig {
    @Bean
    public PaymentService paymentService() {
        return new PilotPaymentService();
    }

    @Bean
    public WebClient paymentWebClient() {
        return WebClient.builder().baseUrl("http://payment-service:8080").build();
    }

}
