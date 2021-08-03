package pl.futurecollars.invoicing.service.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

@SpringBootTest
@ActiveProfiles("file")
class FileServiceTest extends Specification {

    @Autowired
    private FileService fileService
    private Path tempFilePath = Files.createTempFile("fileServiceFile", "txt")

    def writeSampleDataToFileForSearchOperations() {
        fileService.appendLine(tempFilePath, "{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine(tempFilePath, "{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine(tempFilePath, "{\"id\":3,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine(tempFilePath, "{\"id\":4,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine(tempFilePath, "{\"id\":6,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.appendLine(tempFilePath, "{\"\":,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
    }

    def "should write line to file"() {

        given:
        List<String> lines = List.of("Test line 1", "Test line 2")

        when:
        lines.forEach(line -> fileService.appendLine(tempFilePath, line))

        then:
        Files.readAllLines(tempFilePath) == lines
    }

    def "should throw IllegalStateException if there is problem to write to file"() {
        setup:
        File file = new File(tempFilePath.toString())
        file.createNewFile()
        file.setWritable(false)

        when:
        fileService.appendLine(tempFilePath, "any text")

        then:
        thrown(IllegalStateException.class)

        cleanup:
        file.delete()
    }

    def "should read lines from file"() {
        setup:
        String textToFile = "Test line 1" + System.lineSeparator() + "Test line 2"
        Files.writeString(tempFilePath, textToFile, StandardOpenOption.CREATE)

        expect:
        fileService.readLinesToList(tempFilePath) == List.of("Test line 1", "Test line 2")
    }

    def "should throw IllegalStateException if there is problem to read from file"() {
        setup:
        File file = new File(tempFilePath.toString())
        file.delete()

        when:
        fileService.readLinesToList(tempFilePath)

        then:
        thrown(IllegalStateException.class)
    }

    def "should return empty Optional if id not found"() {
        setup:
        writeSampleDataToFileForSearchOperations()

        expect:
        fileService.findLineById(tempFilePath, 100) == Optional.empty()

    }

    def "should return Optional of line number for existing and unique ID"() {
        setup:
        writeSampleDataToFileForSearchOperations()

        expect:
        verifyAll {
            fileService.getLineNumberById(tempFilePath, 1) == Optional.of(0)
            fileService.getLineNumberById(tempFilePath, 4) == Optional.of(3)
        }
    }

    def "should return empty Optional for not existing or not unique ID"() {
        setup:
        writeSampleDataToFileForSearchOperations()
        fileService.appendLine(tempFilePath, "{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        expect:
        verifyAll {
            fileService.getLineNumberById(tempFilePath, 2) == Optional.empty()
            fileService.getLineNumberById(tempFilePath, 400) == Optional.empty()
        }
    }

    def "should rewrite file"() {
        setup:
        fileService.appendLine(tempFilePath, "line one")
        fileService.appendLine(tempFilePath, "line two")
        List<String> linesToRewriteFile = List.of("Other line one", "Other line two")

        when:
        fileService.rewriteFileByList(tempFilePath, linesToRewriteFile)

        then:
        fileService.readLinesToList(tempFilePath) == linesToRewriteFile
    }

    def "should throw IllegalStateException if there is problem to update file"() {
        setup:
        File file = new File(tempFilePath.toString())
        file.createNewFile()
        file.setWritable(false)

        when:
        fileService.rewriteFileByList(tempFilePath, List.of("line 1", "line 2"))

        then:
        thrown(IllegalStateException)

        cleanup:
        file.delete()
    }

    def "should return list of two text lines if fileService write two lines to file"() {
        setup:
        fileService.appendLine(tempFilePath, "Line One")
        fileService.appendLine(tempFilePath, "Line two")

        expect:
        Files.readAllLines(tempFilePath).size() == 2
    }

    def "should throw IllegalStateException if founded more than one line with same id"() {
        setup:
        writeSampleDataToFileForSearchOperations()
        fileService.appendLine(tempFilePath, "{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        when:
        fileService.findLineById(tempFilePath, 2)

        then:
        thrown(IllegalStateException.class)
    }

    def "should create file if not exists when rewrite method is used"() {
        when:
        fileService.rewriteFileByList(tempFilePath, List.of("Line"))

        then:
        Files.exists(tempFilePath)
    }

}
