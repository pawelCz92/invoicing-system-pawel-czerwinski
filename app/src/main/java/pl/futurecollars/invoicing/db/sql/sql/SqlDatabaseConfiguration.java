package pl.futurecollars.invoicing.db.sql.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
public class SqlDatabaseConfiguration {

    @Bean
    public Database<Invoice> sqlDatabaseForInvoice(JdbcTemplate jdbcTemplate) {
        log.info("SQL database is in use");
        return new SqlDatabaseForInvoice(jdbcTemplate);
    }

    @Bean
    public Database<Company> sqlDatabaseForCompany(JdbcTemplate jdbcTemplate) {
        log.info("SQL database is in use");
        return new SqlDatabaseForCompany(jdbcTemplate);
    }
}
