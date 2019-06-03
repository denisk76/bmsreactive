package ru.bms.handlerservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.handlerservice.service.ClientService;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.handlerservice.service.PaymentService;
import ru.bms.handlerservice.service.TerminalService;
import ru.bms.handlerservice.service.pilot.PilotClientService;
import ru.bms.handlerservice.service.pilot.PilotPaymentService;
import ru.bms.handlerservice.service.pilot.PilotTerminalService;
import ru.bms.handlerservice.service.pilot.SimplePaymentMapper;

@Configuration
public class HandlerConfig {
    @Bean
    public WebClient terminalWebClient() {
        return WebClient.builder().baseUrl("http://terminal-service:8080").build();
    }

    @Bean
    public WebClient clientWebClient() {
        return WebClient.builder().baseUrl("http://client-service:8080").build();
    }

    @Bean
    public WebClient paymentWebClient() {
        return WebClient.builder().baseUrl("http://payment-service:8080").build();
    }

    @Bean
    public PaymentService paymentService() {
        return new PilotPaymentService();
    }

    @Bean
    public PaymentMapper paymentMapper() {
        return new SimplePaymentMapper();
    }

    @Bean
    public ClientService clientService() {
        return new PilotClientService();
    }

    @Bean
    public TerminalService terminalService() {
        return new PilotTerminalService();
    }
}
