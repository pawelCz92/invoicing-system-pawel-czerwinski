package pl.futurecollars.invoicing.service.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.StandardOpenOption

@SpringBootTest
@ActiveProfiles("file")
class IdProviderTest extends Specification {

    @Autowired
    private IdProvider idProvider

    def "should throw IllegalStateException for more than one line in idProvide file"() {
        setup:
        idProvider.getNextIdAndIncrement()
        Files.writeString(idProvider.getFilePath(), "next line1".concat(System.lineSeparator()), StandardOpenOption.APPEND)
        Files.writeString(idProvider.getFilePath(), "next line2".concat(System.lineSeparator()), StandardOpenOption.APPEND)

        when:
        idProvider.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)
    }

    def "should throw IllegalStateException if there is problem to convert id file content to id"() {
        setup:
        Files.writeString(idProvider.getFilePath(), "text - not be able to convert to id (integer)", StandardOpenOption.TRUNCATE_EXISTING)

        when:
        idProvider.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)
    }
}
