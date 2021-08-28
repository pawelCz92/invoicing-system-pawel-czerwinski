package pl.futurecollars.invoicing.service.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.StandardOpenOption

@SpringBootTest
@ActiveProfiles("file")
class IdProviderTest extends Specification {

    @Autowired
    private IdProvider idProviderForInvoices

    def "should throw IllegalStateException for more than one line in idProvide file"() {
        setup:
        idProviderForInvoices.getNextIdAndIncrement()
        Files.writeString(idProviderForInvoices.getFilePath(),
                "next line1".concat(System.lineSeparator()), StandardOpenOption.APPEND)
        Files.writeString(idProviderForInvoices.getFilePath(),
                "next line2".concat(System.lineSeparator()), StandardOpenOption.APPEND)

        when:
        idProviderForInvoices.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)
    }

    def "should throw IllegalStateException if there is problem to convert id file content to id"() {
        setup:
        Files.writeString(idProviderForInvoices.getFilePath(), "text - not be able to convert to id (integer)", StandardOpenOption.TRUNCATE_EXISTING)

        when:
        idProviderForInvoices.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)
    }
}
