package com.example.lab4.laba_4.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.lab4.laba_4")
public class DBConfig {

    private Logger log = LoggerFactory.getLogger(DBConfig.class);

    @Bean
    HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }


    @Bean
    DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
//        dataSource.setUsername("s243147");
//        dataSource.setPassword("mhz656");
        return dataSource;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        entityManagerFactoryBean.setJpaProperties(properties);
        entityManagerFactoryBean.setPackagesToScan("com.example.lab4.laba_4.domain");
        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @PostConstruct
    public void postConstruct(){
        try{
            dataSource().getConnection().createStatement()
                    .execute("CREATE TABLE SPRING_SESSION (\n" +
                            "\tPRIMARY_ID CHAR(36) NOT NULL,\n" +
                            "\tSESSION_ID CHAR(36) NOT NULL,\n" +
                            "\tCREATION_TIME BIGINT NOT NULL,\n" +
                            "\tLAST_ACCESS_TIME BIGINT NOT NULL,\n" +
                            "\tMAX_INACTIVE_INTERVAL INT NOT NULL,\n" +
                            "\tEXPIRY_TIME BIGINT NOT NULL,\n" +
                            "\tPRINCIPAL_NAME VARCHAR(100),\n" +
                            "\tCONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)\n" +
                            ");\n" +
                            "\n" +
                            "CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);\n" +
                            "CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);\n" +
                            "CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);\n" +
                            "\n" +
                            "CREATE TABLE SPRING_SESSION_ATTRIBUTES (\n" +
                            "\tSESSION_PRIMARY_ID CHAR(36) NOT NULL,\n" +
                            "\tATTRIBUTE_NAME VARCHAR(200) NOT NULL,\n" +
                            "\tATTRIBUTE_BYTES BYTEA NOT NULL,\n" +
                            "\tCONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),\n" +
                            "\tCONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE\n" +
                            ");\n");
            log.info("session tables created");
        } catch (SQLException e){
            log.info("session tables already exists");
        }
    }

}
