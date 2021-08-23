package pl.futurecollars.invoicing.db.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
public class InMemoryDbConfiguration {

    @Bean
    public Database<Invoice> inMemoryDbForInvoice() {
        log.info("In memory based database is in use (Invoice)");
        return new InMemoryDataBase<>();
    }

    @Bean
    public Database<Company> inMemoryDbForCompany() {
        log.info("In memory based database is in use (Company)");
        return new InMemoryDataBase<>();
    }
}
