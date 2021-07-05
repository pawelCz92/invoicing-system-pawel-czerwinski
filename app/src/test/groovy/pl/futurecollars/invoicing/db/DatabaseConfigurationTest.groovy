package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification

@SpringBootTest
class DatabaseConfigurationTest extends Specification {

    @Autowired
    DatabaseConfiguration databaseConfiguration

    @Autowired
    JsonService jsonService

    def "should provide new IdProvider"() {
        expect:
        databaseConfiguration.idProvider() != null

    }

    def "should provide fileBasedDatabase "() {
        expect:
        databaseConfiguration.fileBasedDatabase(jsonService, databaseConfiguration.idProvider()) != null
    }
}
