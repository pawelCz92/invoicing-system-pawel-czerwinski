package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;
import pl.futurecollars.invoicing.service.file.IdProvider;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
public class FileBasedDatabaseConfiguration {

    @Bean
    IdProvider idProviderForInvoices(
        FileService fileService,
        @Value("${invoicing-system.database.dbDirectory}") String dbDirectory,
        @Value("${invoicing-system.database.idFileNameForInvoices}") String idFileName
    ) throws IOException {
        Path idProviderPath = Files.createTempFile(dbDirectory, idFileName);
        return new IdProvider(idProviderPath, fileService);
    }

    @Bean
    FileBasedDatabase<Invoice> fileBasedDbForInvoice(
        FileService fileService,
        JsonService jsonService,
        IdProvider idProvider,
        @Value("${invoicing-system.database.dbDirectory}") String dbDirectory,
        @Value("${invoicing-system.database.dataFileNameForInvoices}") String dbFileName
    ) throws IOException {
        Path dbFilePath = Files.createTempFile(dbDirectory, dbFileName);
        log.info("File based database is in use");
        return new FileBasedDatabase<>(dbFilePath, idProvider, fileService, jsonService, Invoice.class);
    }

    @Bean
    IdProvider idProviderForCompany(
        FileService fileService,
        @Value("${invoicing-system.database.dbDirectory}") String dbDirectory,
        @Value("${invoicing-system.database.idFileNameForCompany}") String idFileName
    ) throws IOException {
        Path idProviderPath = Files.createTempFile(dbDirectory, idFileName);
        return new IdProvider(idProviderPath, fileService);
    }

    @Bean
    FileBasedDatabase<Company> fileBasedDbForCompany(
        FileService fileService,
        JsonService jsonService,
        IdProvider idProvider,
        @Value("${invoicing-system.database.dbDirectory}") String dbDirectory,
        @Value("${invoicing-system.database.dataFileNameForCompany}") String dbFileName
    ) throws IOException {
        Path dbFilePath = Files.createTempFile(dbDirectory, dbFileName);
        log.info("File based database is in use");
        return new FileBasedDatabase<>(dbFilePath, idProvider, fileService, jsonService, Company.class);
    }
}
