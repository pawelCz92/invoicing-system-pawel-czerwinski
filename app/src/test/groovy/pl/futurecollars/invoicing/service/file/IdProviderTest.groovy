package pl.futurecollars.invoicing.service.file


import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class IdProviderTest extends Specification {

    private Path testFilePath
    private IdProvider idProvider

    def setup() {
        testFilePath = Path.of("idProviderTestFile.txt")
        idProvider = new IdProvider(testFilePath)
    }

    def cleanup() {
        Files.deleteIfExists(testFilePath)
    }

    def "should throw IllegalStateException for more than one line in idProvide file"() {
        setup:
        idProvider.getNextIdAndIncrement()
        Files.writeString(testFilePath, "next line1".concat(System.lineSeparator()), StandardOpenOption.APPEND)
        Files.writeString(testFilePath, "next line2".concat(System.lineSeparator()), StandardOpenOption.APPEND)

        when:
        idProvider.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)
    }

    def "should throw IllegalStateException if there is problem to convert id file content to number"() {
        setup:
        idProvider.getNextIdAndIncrement()
        Files.writeString(testFilePath, "next line1".concat(System.lineSeparator()), StandardOpenOption.APPEND)

        when:
        idProvider.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)
    }

    def "should throw IllegalStateException if there is problem to convert id file content to id"() {
        setup:
        idProvider.getNextIdAndIncrement()
        Files.writeString(testFilePath, "text - not be able to convert to id (integer)", StandardOpenOption.TRUNCATE_EXISTING)

        when:
        idProvider.getNextIdAndIncrement()

        then:
        thrown(IllegalStateException.class)
    }
}
