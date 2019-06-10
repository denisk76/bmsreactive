package ru.bms.clientservice.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.bms.clientservice.service.ClientService;
import ru.bms.clientservice.service.pilot.ClientServiceImpl;

import javax.sql.DataSource;

import static ru.bms.PostgresConfig.*;
import static ru.bms.clientservice.jpa.ClientServiceJpaTest.postgreSQLContainer;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.bms.clientservice.dao"})
public class JpaTestConfig {

    public static final String postgresAddress = postgreSQLContainer.getContainerIpAddress() + ":" + postgreSQLContainer.getMappedPort(5432);
    public static final String postgresUrl = "jdbc:postgresql://" + postgresAddress + "/questionmarks";

    @Bean
    public ClientService clientService() {
        return new ClientServiceImpl();
    }

    @Bean
    public DataSource dataSource() {
        System.out.println("postgresAddress = " + postgresAddress);
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(DRIVER_CLASS_NAME);
        ds.setUrl(postgresUrl);
        ds.setUsername(POSTGRES_USERNAME);
        ds.setPassword(POSTGRES_PASSWORD);
        return ds;
    }
}
