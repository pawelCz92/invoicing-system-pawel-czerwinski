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

    def writeSampleDataToFileForSearchOperations() {
        fileService.appendLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine("{\"id\":3,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine("{\"id\":4,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine("{\"id\":6,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine("{\"\":,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
    }

    def "should write line to file"() {

        given:
        List<String> lines = List.of("Test line 1", "Test line 2")

        when:
        lines.forEach(line -> fileService.appendLine(line))

        then:
        Files.readAllLines(filePath) == lines
    }

    def "should throw IllegalStateException if there is problem to write to file"() {
        setup:
        File file = new File(filePath.toString())
        file.createNewFile()
        file.setWritable(false)

        when:
        fileService.appendLine("any text")

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

    def "should return empty Optional if id not found"() {
        setup:
        writeSampleDataToFileForSearchOperations()

        expect:
        fileService.findLineById(100) == Optional.empty()

    }

    def "should return Optional of line number for existing and unique ID"() {
        setup:
        writeSampleDataToFileForSearchOperations()

        expect:
        verifyAll {
            fileService.getLineNumberById(1) == Optional.of(0)
            fileService.getLineNumberById(4) == Optional.of(3)
        }
    }

    def "should return empty Optional for not existing or not unique ID"() {
        setup:
        writeSampleDataToFileForSearchOperations()
        fileService.appendLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        expect:
        verifyAll {
            fileService.getLineNumberById(2) == Optional.empty()
            fileService.getLineNumberById(400) == Optional.empty()
        }
    }

    def "should rewrite file"() {
        setup:
        fileService.appendLine("line one")
        fileService.appendLine("line two")
        List<String> linesToRewriteFile = List.of("Other line one", "Other line two")

        when:
        fileService.rewriteFileByList(linesToRewriteFile)

        then:
        fileService.readLinesToList() == linesToRewriteFile
    }

    def "should throw IllegalStateException if there is problem to update file"() {
        setup:
        File file = new File(filePath.toString())
        file.createNewFile()
        file.setWritable(false)

        when:
        fileService.rewriteFileByList(List.of("line 1", "line 2"))

        then:
        thrown(IllegalStateException)

        cleanup:
        file.delete()
    }

    def "should return list of two text lines if fileService write two lines to file"() {
        setup:
        fileService.appendLine("Line One")
        fileService.appendLine("Line two")

        expect:
        Files.readAllLines(filePath).size() == 2
    }

    def "should throw IllegalStateException if founded more than one line with same id"() {
        setup:
        writeSampleDataToFileForSearchOperations()
        fileService.appendLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        when:
        fileService.findLineById(2)

        then:
        thrown(IllegalStateException.class)
    }

    def "should create file if not exists when rewrite method is used"(){
        when:
        fileService.rewriteFileByList(List.of("Line"))

        then:
        Files.exists(filePath)
    }

}
