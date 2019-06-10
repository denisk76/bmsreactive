package ru.bms.testConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.paymentapi.service.PaymentService;
import ru.bms.paymentapi.service.pilot.PilotPaymentService;

@Configuration
public class PaymentTetsConfig {
    @Bean
    public WebClient paymentWebClient() {
        return WebClient.builder().baseUrl("http://localhost:8002").build();
    }

    @Bean
    public PaymentService paymentService() {
        return new PilotPaymentService();
    }

}
