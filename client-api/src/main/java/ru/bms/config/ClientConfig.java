package ru.bms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.service.ClientService;
import ru.bms.service.pilot.PilotClientService;

@Configuration
public class ClientConfig {
    @Bean
    public WebClient clientWebClient() {
        return WebClient.builder().baseUrl("http://client-service:8080").build();
    }
    @Bean
    public ClientService clientService() {
        return new PilotClientService();
    }

}
