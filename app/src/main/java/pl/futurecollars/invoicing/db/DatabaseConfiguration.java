package pl.futurecollars.invoicing.db;

import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;
import pl.futurecollars.invoicing.service.file.IdProvider;

@Slf4j
@Configuration
public class DatabaseConfiguration {

    private final String currentDir = Paths.get("").toAbsolutePath().toString();

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    @Bean
    IdProvider idProvider(
        @Value("${invoicing-system.database.directory}") String fileDir,
        @Value("${invoicing-system.database.idFileName}") String fileName) {
        return new IdProvider(Paths.get(currentDir, fileDir, fileName));
    }

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    @Bean
    FileBasedDatabase fileBasedDatabase(
        JsonService jsonService,
        IdProvider idProvider,
        @Value("${invoicing-system.database.directory}") String dirName,
        @Value("${invoicing-system.database.dataFileName}") String fileName) {

        FileService fileServiceForData = new FileService(Paths.get(currentDir, dirName, fileName));
        log.info("File based database is in use");
        return new FileBasedDatabase(fileServiceForData, idProvider, jsonService);
    }

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    @Bean
    InMemoryDataBase inMemoryDataBase() {
        log.info("In memory based database is in use");
        return new InMemoryDataBase();
    }
}
