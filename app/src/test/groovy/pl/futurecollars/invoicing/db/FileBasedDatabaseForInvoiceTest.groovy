package pl.futurecollars.invoicing.db

import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.file.FileBasedDatabase
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import pl.futurecollars.invoicing.service.file.IdProvider

import java.nio.file.Files
import java.nio.file.Path

@IfProfileValue(name = "spring.profiles.active", value = "file")
class FileBasedDatabaseForInvoiceTest extends AbstractDatabaseTest {

    private Path dbPath

    @Override
    Database getDatabaseInstance() {

        FileService fileService = new FileService()
        Path idPath = Files.createTempFile("ids", ".txt")
        dbPath = Files.createTempFile("data", ".txt")
        IdProvider idProvider = new IdProvider(idPath, fileService)

        return new FileBasedDatabase<Invoice>(dbPath, idProvider, fileService, new JsonService(), Invoice.class)
    }

    @Override
    List<Invoice> getItemsList() {
        return TestHelpers.getSampleInvoicesList()
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

    def "should update invoice"() {
        setup:
        saveItems()
        long invoiceId = database.getAll().get(1).getId();
        Invoice invoiceToUpdate = database.getById(invoiceId).get() as Invoice
        invoiceToUpdate.setNumber("xxxxxxxx")

        when:
        database.update(invoiceToUpdate.getId(), invoiceToUpdate)

        then:
        jsonService.objectToString(invoiceToUpdate) == jsonService.objectToString(database.getById(invoiceId).get())
    }
}
