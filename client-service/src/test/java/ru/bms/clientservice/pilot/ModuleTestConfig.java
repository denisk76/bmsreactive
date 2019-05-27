package ru.bms.clientservice.pilot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import ru.bms.clientservice.service.ClientService;
import ru.bms.clientservice.service.pilot.ClientServiceImpl;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static ru.bms.clientservice.pilot.ClientServiceModuleTest.postgreSQLContainer;
import static ru.bms.PostgresConfig.*;


@Configuration
@EnableJpaRepositories(basePackages = {"ru.bms.clientservice.dao"})
@EnableConfigurationProperties
public class ModuleTestConfig {
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

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[]{"ru.bms.clientservice.dao", "ru.bms.clientservice.data"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        return properties;
    }

}
