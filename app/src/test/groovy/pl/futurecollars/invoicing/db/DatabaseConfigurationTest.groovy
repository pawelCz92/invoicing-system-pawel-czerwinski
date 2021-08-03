package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.service.file.IdProvider
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("file")
class DatabaseConfigurationTest extends Specification {

    @Autowired
    FileBasedDatabase fileBasedDatabase

    @Autowired
    IdProvider idProvider

    @Autowired
    JsonService jsonService

    def "should provide new IdProvider"() {
        expect:
        idProvider != null
    }

    def "should provide fileBasedDatabase "() {
        expect:
       fileBasedDatabase != null
    }
}
