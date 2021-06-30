package pl.futurecollars.invoicing.service

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class IdProviderTest extends Specification {

    def "should throw IllegalStateException for more than one line in idProvide file"() {
        setup:
        String fileName = "idProviderTestFile.txt"
        IdProvider idProvider = new IdProvider(fileName)
        idProvider.getNextIdAndIncrement()
        Files.writeString(Path.of(fileName), "next line1".concat(System.lineSeparator()), StandardOpenOption.APPEND)
        Files.writeString(Path.of(fileName), "next line2".concat(System.lineSeparator()), StandardOpenOption.APPEND)

        when:
        idProvider.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)

        cleanup:
        Files.deleteIfExists(Path.of(fileName))
    }

    def "should throw IllegalStateException if there is problem to convert id file content to number"() {
        setup:
        String fileName = "idProviderTestFile.txt"
        IdProvider idProvider = new IdProvider(fileName)
        idProvider.getNextIdAndIncrement()
        Files.writeString(Path.of(fileName), "next line1".concat(System.lineSeparator()), StandardOpenOption.APPEND)

        when:
        idProvider.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)

        cleanup:
        Files.deleteIfExists(Path.of(fileName))
    }
}
