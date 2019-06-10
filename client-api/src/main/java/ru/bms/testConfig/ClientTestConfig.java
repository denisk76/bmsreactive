package ru.bms.testConfig;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.service.ClientService;
import ru.bms.service.pilot.PilotClientService;

@Configuration
public class ClientTestConfig {
    @Bean
    public WebClient clientWebClient() {
        return WebClient.builder().baseUrl("http://localhost:8001").build();
    }

    @Bean
    public ClientService clientService() {
        return new PilotClientService();
    }

}
