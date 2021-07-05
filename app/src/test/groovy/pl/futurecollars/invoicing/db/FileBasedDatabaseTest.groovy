package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.IdProvider
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.util.stream.Collectors

class FileBasedDatabaseTest extends Specification {

    private String fileNameForDataBaseTest = "testDBfile.json"
    private String fileNameForIdsTest = "testIdsFile.json"
    private FileBasedDatabase fileBasedDatabase
    private IdProvider idProvider = new IdProvider(fileNameForIdsTest)
    private FileService fileServiceForData = new FileService(fileNameForDataBaseTest)
    private List<Invoice> sampleInvoices

    def setup() {
        fileBasedDatabase = new FileBasedDatabase(fileServiceForData, idProvider, new JsonService())
        sampleInvoices = TestHelpers.getSampleInvoicesList()
    }

    def cleanup() {
        Files.deleteIfExists(Path.of(fileNameForDataBaseTest))
        Files.deleteIfExists(Path.of(fileNameForIdsTest))
    }

    def saveSampleInvoicesToBase() {
        sampleInvoices.forEach(invoice -> fileBasedDatabase.save(invoice))
    }

    def "should save invoices to data base file"() {
        setup:
        JsonService jsonService = new JsonService()

        when:
        saveSampleInvoicesToBase()

        then:
        List<Invoice> invoicesFromFile = Files.readAllLines(Path.of(fileNameForDataBaseTest)).stream()
                .map(line -> jsonService.stringToObject(line, Invoice.class))
                .collect(Collectors.toList())

        invoicesFromFile.size() == sampleInvoices.size()
        invoicesFromFile == sampleInvoices
    }

    def "should read last id from file and start generate id from this number"() {
        setup:
        saveSampleInvoicesToBase()
        FileService fileService = new FileService(fileNameForIdsTest)
        fileService.rewriteFileByList(List.of("100"))
        saveSampleInvoicesToBase()
        List<Invoice> savedInvoices = fileBasedDatabase.getAll()
        Invoice lastSavedInvoice = savedInvoices.get(savedInvoices.size() - 1)

        expect:
        lastSavedInvoice.getId() == 104
    }

    def "should find invoice by id"() {
        setup:
        saveSampleInvoicesToBase()
        saveSampleInvoicesToBase()

        when:
        Optional<Invoice> founded1 = fileBasedDatabase.getById(1)
        Optional<Invoice> founded2 = fileBasedDatabase.getById(6)

        then:
        founded1 != Optional.empty()
        founded2 != Optional.empty()
        founded1.get().getId() == 1
        founded2.get().getId() == 6
    }

    def "should get all invoices from base"() {
        setup:
        saveSampleInvoicesToBase()

        expect:
        fileBasedDatabase.getAll() == sampleInvoices
    }

    def "should update invoice by id"() {
        setup:
        saveSampleInvoicesToBase()
        Invoice updatedInvoice = sampleInvoices.get(2)
        updatedInvoice.setDate(LocalDate.of(1999, 1, 1))

        when:
        fileBasedDatabase.update(4, updatedInvoice)

        then:
        fileBasedDatabase.getAll().size() == sampleInvoices.size()
        fileBasedDatabase.getById(4).get() == updatedInvoice
    }

    def "should delete invoice by id"() {
        setup:
        saveSampleInvoicesToBase()

        when:
        fileBasedDatabase.delete(1)

        then:
        fileBasedDatabase.getAll().size() == sampleInvoices.size() - 1
        fileBasedDatabase.getById(1).isEmpty()
    }

    def "should throw IllegalArgumentException if there is no such id using delete method"(int incorrectId) {
        setup:
        saveSampleInvoicesToBase()

        when:
        fileBasedDatabase.delete(incorrectId)

        then:
        thrown(IllegalArgumentException)

        where:
        incorrectId << [5, 100]
    }

    def "should throw IllegalArgumentException if there is no such id using update method"(int incorrectId) {
        setup:
        saveSampleInvoicesToBase()

        when:
        fileBasedDatabase.update(incorrectId, new Invoice())

        then:
        thrown(IllegalArgumentException)

        where:
        incorrectId << [5, 100]
    }
}
