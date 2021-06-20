package pl.futurecollars.invoicing.service.file

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class FileServiceTest extends Specification {

    private FileService fileService
    private static Path filePath = Path.of("fileServiceTest.json")

    def setup() {
        fileService = new FileService(filePath.toString())
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

    def "should return empty Optional if searched ID is not unique in base"() {
        setup:
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":4,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":6,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        expect:
        fileService.findLineById(1) == Optional.empty()

    }

    def "should return line number for existing and unique ID"() {
        setup:
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":3,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":4,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":6,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        expect:
        fileService.getLineNumberById(1) == 0
        fileService.getLineNumberById(4) == 3
    }

    def "should return null for not existing or not unique ID"() {
        setup:
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":3,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":6,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        expect:
        fileService.getLineNumberById(1) == null
        fileService.getLineNumberById(400) == null
    }

    def "should rewrite file"() {
        setup:
        fileService.writeLine("line one")
        fileService.writeLine("line two")
        List<String> linesToRewriteFile = List.of("Other line one", "Other line two")

        when:
        fileService.updateBaseFile(linesToRewriteFile)

        then:
        fileService.readLinesToList() == linesToRewriteFile
    }

    def "should throw IllegalStateException if there is problem to update file"() {
        setup:
        File file = new File(filePath.toString())
        file.setWritable(false)

        when:
        fileService.updateBaseFile(List.of("line 1", "line 2"))

        then:
        thrown(IllegalStateException)

        cleanup:
        file.delete()
    }

}
