package pl.futurecollars.invoicing.service.file

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class FileServiceTest extends Specification {

    private FileService fileService
    private static Path filePath = Path.of("fileServiceTest.json")

    def setup() {
        fileService = new FileService(filePath.toString(), true)
    }

    def cleanup() {
        Files.deleteIfExists(filePath)
    }

    def "should write line to file"() {

        given:
        List<String> lines = List.of("Test line 1", "Test line 2")

        when:
        lines.forEach(line -> fileService.writeLine(line))

        then:
        Files.readAllLines(filePath) == lines
    }

    def "should throw IllegalStateException if there is problem to write to file"() {
        setup:
        File file = new File(filePath.toString())
        file.setWritable(false)

        when:
        fileService.writeLine("any text")

        then:
        thrown(IllegalStateException.class)

        cleanup:
        file.delete()
    }

    def "should read lines from file"() {
        setup:
        String textToFile = "Test line 1" + System.lineSeparator() + "Test line 2"
        Files.writeString(filePath, textToFile, StandardOpenOption.CREATE)

        expect:
        fileService.readLinesToList() == List.of("Test line 1", "Test line 2")
    }

    def "should throw IllegalStateException if there is problem to read from file"() {
        setup:
        File file = new File(filePath.toString())
        file.delete()

        when:
        fileService.readLinesToList()

        then:
        thrown(IllegalStateException.class)
    }
}
