package pl.futurecollars.invoicing.db.sql.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
public class MongoDatabaseConfiguration {

    @Bean
    public MongoDatabase mongoDb(
        @Value("${invoicing-system.database.name}") String databaseName
    ) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();

        MongoClient client = MongoClients.create(settings);
        return client.getDatabase(databaseName);
    }

    @Bean
    public MongoIdProvider mongoIdProvider(
        @Value("${invoicing-system.database.counter.collection}") String collectionName,
        MongoDatabase mongoDb
    ) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
        return new MongoIdProvider(collection);
    }

    @Bean
    public Database<Invoice> mongoDatabaseForInvoice(
        @Value("${invoicing-system.database.invoice.collection}") String collectionName,
        MongoDatabase mongoDatabase,
        MongoIdProvider mongoIdProvider
    ) {
        MongoCollection<Invoice> collection = mongoDatabase.getCollection(collectionName, Invoice.class);

        log.info("Mongo database is in use");
        return new MongoBaseDatabase<>(collection, mongoIdProvider);
    }

    @Bean
    public Database<Company> mongoDatabaseForCompany(
        @Value("${invoicing-system.database.company.collection}") String collectionName,
        MongoDatabase mongoDatabase,
        MongoIdProvider mongoIdProvider
    ) {
        MongoCollection<Company> collection = mongoDatabase.getCollection(collectionName, Company.class);

        log.info("Mongo database is in use");
        return new MongoBaseDatabase<>(collection, mongoIdProvider);
    }
}
