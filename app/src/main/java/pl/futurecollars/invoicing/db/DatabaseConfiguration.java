package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.sql.SqlDatabase;
import pl.futurecollars.invoicing.db.sql.jpa.InvoiceRepository;
import pl.futurecollars.invoicing.db.sql.jpa.JpaDatabase;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;
import pl.futurecollars.invoicing.service.file.IdProvider;

@Slf4j
@Configuration
public class DatabaseConfiguration {



    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    InMemoryDataBase inMemoryDataBase() {
        log.info("In memory based database is in use");
        return new InMemoryDataBase();
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
    public Database sqlDatabase(JdbcTemplate jdbcTemplate) {
        log.info("SQL database is in use");
        return new SqlDatabase(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
    public Database jpaDatabase(InvoiceRepository invoiceRepository) {
        log.info("JPA database is in use");
        return new JpaDatabase(invoiceRepository);
    }
}
