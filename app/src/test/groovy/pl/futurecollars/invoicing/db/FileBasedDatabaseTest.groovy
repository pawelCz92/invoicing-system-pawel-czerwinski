package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.file.FileBasedDatabase
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import pl.futurecollars.invoicing.service.file.IdProvider

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseTest extends AbstractDatabaseTest {

    Path dbPath

    @Override
    Database getDatabaseInstance() {

        FileService fileService = new FileService()
        Path idPath = Files.createTempFile("ids", ".txt")
        dbPath = Files.createTempFile("data", ".txt")
        IdProvider idProvider = new IdProvider(idPath, fileService)


        return new FileBasedDatabase(dbPath, idProvider, fileService, new JsonService())
    }

    def "should save data to right file"() {
        given:
        Database database = getDatabaseInstance()

        when:
        database.save(TestHelpers.getSampleInvoicesList().get(0))

        then:
        Files.readAllLines(dbPath).size() == 1

        when:
        database.save(TestHelpers.getSampleInvoicesList().get(0))

        then:
        Files.readAllLines(dbPath).size() == 2
    }
}
