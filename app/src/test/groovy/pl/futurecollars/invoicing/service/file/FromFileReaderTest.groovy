package pl.futurecollars.invoicing.service.file

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FromFileReaderTest extends Specification {

    def "should read lines from file"() {
        setup:
        String filePath = "readingFileTest.json"
        FromFileReader fromFileReader = new FromFileReader(filePath)
        String content = "test line 1" + System.lineSeparator() + "test line 2"
        Files.writeString(Path.of(filePath), content)

        when:
        List<String> lines = fromFileReader.readLinesFromFile()

        then:
        lines == List.of("test line 1", "test line 2")

        cleanup:
        Files.deleteIfExists(Path.of(filePath))
    }

    def "should read empty file as empty list"() {
        setup:
        String filePath = "readingFileTest.json"
        FromFileReader fromFileReader = new FromFileReader(filePath)
        Files.createFile(Path.of(filePath))

        when:
        List<String> lines = fromFileReader.readLinesFromFile()

        then:
        lines.size() == 0

        cleanup:
        Files.deleteIfExists(Path.of(filePath))
    }
}
