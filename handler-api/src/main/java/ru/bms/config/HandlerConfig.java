package ru.bms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.service.HandlerService;
import ru.bms.service.pilot.PilotHandlerService;

@Configuration
public class HandlerConfig {
    @Bean
    public WebClient handlerWebClient() {
        return WebClient.builder().baseUrl("http://handler-service:8080").build();
    }

    @Bean
    public HandlerService clientService() {
        return new PilotHandlerService();
    }

}
