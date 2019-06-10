package ru.bms.testConfig;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bms.service.TerminalService;
import ru.bms.service.pilot.PilotTerminalService;

@Configuration
@Log
public class TerminalTestConfig {
    @Bean
    public WebClient terminalWebClient() {
        String ip_addr = System.getenv("IP_ADDR_SERVICE");
        if(ip_addr == null) {
            log.info("IP_ADDR_SERVICE is null");
            ip_addr = "http://localhost:8000";
        } else {
            log.info("IP_ADDR_SERVICE = "+ip_addr);
        }
        return WebClient.builder().baseUrl(ip_addr).build();
    }

    @Bean
    public TerminalService terminalService() {
        return new PilotTerminalService();
    }

}
