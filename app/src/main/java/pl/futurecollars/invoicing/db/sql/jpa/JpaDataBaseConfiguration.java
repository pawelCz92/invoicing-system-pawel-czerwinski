package pl.futurecollars.invoicing.db.sql.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
public class JpaDataBaseConfiguration {

    @Bean
    public Database<Invoice> jpaDatabaseForInvoice(InvoiceRepository invoiceRepository) {
        log.info("JPA database is in use");
        return new JpaDatabase<>(invoiceRepository);
    }

    @Bean
    public Database<Company> jpaDatabaseForCompany(CompanyRepository companyRepository) {
        log.info("JPA database is in use");
        return new JpaDatabase<>(companyRepository);
    }
}
