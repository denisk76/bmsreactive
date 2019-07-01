package ru.bms.testConfig;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.service.HandlerService;
import ru.bms.service.pilot.PilotHandlerService;

@Configuration
public class HandlerTestConfig {
    @Bean
    public WebClient handlerWebClient() {
        return WebClient.builder().baseUrl("http://localhost:8000").build();
    }

    @Bean
    public HandlerService clientService() {
        return new PilotHandlerService();
    }

}
