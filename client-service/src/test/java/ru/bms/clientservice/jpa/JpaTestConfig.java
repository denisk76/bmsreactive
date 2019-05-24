package ru.bms.clientservice.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.bms.clientservice.service.ClientService;
import ru.bms.clientservice.service.pilot.ClientServiceImpl;

import javax.sql.DataSource;

import static ru.bms.clientservice.jpa.ClientServiceJpaTest.postgreSQLContainer;


@Configuration
@EnableJpaRepositories(basePackages = {"ru.bms.clientservice.dao"})
//@EnableConfigurationProperties
public class JpaTestConfig {
    @Bean
    public ClientService clientService() {
        return new ClientServiceImpl();
    }

    @Bean
    public DataSource dataSource() {
        String postgresAddress = postgreSQLContainer.getContainerIpAddress() + ":" + postgreSQLContainer.getMappedPort(5432);
        System.out.println("postgresAddress = " + postgresAddress);
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://"+postgresAddress+"/questionmarks");
        ds.setUsername("postgres");
        ds.setPassword("mysecretpassword");
        return ds;
    }
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix="spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .driverClassName("org.postgresql.Driver")
//                .build();
//    }


//@Bean
//public DataSource dataSource() {
//    DriverManagerDataSource ds = new DriverManagerDataSource();
//    ds.setDriverClassName("org.postgresql.Driver");
//    ds.setUrl("jdbc:postgresql://localhost/questionmarks");
//    ds.setUsername("postgres");
//    ds.setPassword("mysecretpassword");
//    return ds;
//}

}
