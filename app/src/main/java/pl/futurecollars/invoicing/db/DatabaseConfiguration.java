package pl.futurecollars.invoicing.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.service.IdProvider;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;

@Slf4j
@Configuration
public class DatabaseConfiguration {

    private static final String DB_DATA_FILE_NAME_PATH = "db-data.json";
    private static final String DB_ID_FILE_NAME_PATH = "db-ids.json";

    @Bean
    IdProvider idProvider() {
        return new IdProvider(DB_ID_FILE_NAME_PATH);
    }

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    @Bean
    FileBasedDatabase fileBasedDatabase(JsonService jsonService, IdProvider idProvider) {
        FileService fileServiceForData = new FileService(DB_DATA_FILE_NAME_PATH);
        log.info("File based database is in use");
        return new FileBasedDatabase(fileServiceForData, idProvider, jsonService);
    }

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    @Bean
    InMemoryDataBase inMemoryDataBase(){
        log.info("In memory based database is in use");
        return new InMemoryDataBase();
    }
}
