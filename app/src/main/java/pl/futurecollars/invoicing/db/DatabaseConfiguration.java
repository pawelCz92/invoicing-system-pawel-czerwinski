package pl.futurecollars.invoicing.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.service.IdProvider;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;

@Configuration
public class DatabaseConfiguration {

    public static final String DB_DATA_FILE_NAME_PATH = "db-data.json";
    public static final String DB_ID_FILE_NAME_PATH = "db-ids.json";

    @Bean
    public IdProvider idProvider() {
        return new IdProvider(DB_ID_FILE_NAME_PATH);
    }

    @Bean
    FileBasedDatabase fileBasedDatabase(JsonService jsonService, IdProvider idProvider) {
        FileService fileServiceForData = new FileService(DB_DATA_FILE_NAME_PATH);
        return new FileBasedDatabase(fileServiceForData, idProvider, jsonService);
    }
}
