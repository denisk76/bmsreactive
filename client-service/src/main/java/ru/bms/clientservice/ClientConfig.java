package ru.bms.clientservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bms.clientservice.service.ClientService;
import ru.bms.clientservice.service.pilot.ClientServiceImpl;

@Configuration
//@EnableJpaRepositories(basePackages = {"ru.bms.clientservice.dao"})
//@EnableConfigurationProperties
public class ClientConfig {
    @Bean
    public ClientService clientService() {
        return new ClientServiceImpl();
    }

//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
}
