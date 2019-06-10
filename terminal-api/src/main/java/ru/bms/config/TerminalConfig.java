package ru.bms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.service.TerminalService;
import ru.bms.service.pilot.PilotTerminalService;

@Configuration
public class TerminalConfig {
    @Bean
    public WebClient terminalWebClient() {
        return WebClient.builder().baseUrl("http://terminal-service:8080").build();
    }
    @Bean
    public TerminalService terminalService() {
        return new PilotTerminalService();
    }

}
