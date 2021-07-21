package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.IdProvider
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.util.stream.Collectors

class FileBasedDatabaseTest extends Specification {

    private static final Path DB_TEST_FILE_PATH = Path.of("testDBfile.json")
    private static final Path ID_TEST_FILE_PATH = Path.of("testIdsFile.json")
    private static FileBasedDatabase fileBasedDatabase
    private static IdProvider idProvider
    private static FileService fileServiceForData
    private static List<Invoice> sampleInvoices

    def setupSpec() {
        idProvider = new IdProvider(ID_TEST_FILE_PATH)
        fileServiceForData = new FileService(DB_TEST_FILE_PATH)
        fileBasedDatabase = new FileBasedDatabase(fileServiceForData, idProvider, new JsonService())
        sampleInvoices = TestHelpers.getSampleInvoicesList()
    }

    def cleanup() {
        Files.deleteIfExists(DB_TEST_FILE_PATH)
        Files.deleteIfExists(ID_TEST_FILE_PATH)
    }

    def saveSampleInvoicesToBase() {
        fileBasedDatabase.save(sampleInvoices.get(0))
        fileBasedDatabase.save(sampleInvoices.get(1))
        fileBasedDatabase.save(sampleInvoices.get(3))
    }

    def "should save invoices to data base file"() {
        setup:
        JsonService jsonService = new JsonService()

        when:
        saveSampleInvoicesToBase()

        then:
        List<Invoice> invoicesFromFile = Files.readAllLines(DB_TEST_FILE_PATH).stream()
                .map(line -> jsonService.stringToObject(line, Invoice.class))
                .collect(Collectors.toList())

        invoicesFromFile.size() == 3
        invoicesFromFile.containsAll(List.of(sampleInvoices.get(0), sampleInvoices.get(1), sampleInvoices.get(3)))
    }

    def "should read last id from file and start generate id from this number"() {
        setup:
        saveSampleInvoicesToBase()
        Files.writeString(ID_TEST_FILE_PATH, "100", StandardOpenOption.TRUNCATE_EXISTING)
        fileBasedDatabase.save((sampleInvoices.get(3)))
        List<Invoice> savedInvoices = fileBasedDatabase.getAll()
        Invoice lastSavedInvoice = savedInvoices.get(savedInvoices.size() - 1)

        expect:
        lastSavedInvoice.getId() == 101
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
        fileBasedDatabase.getAll() == List.of(sampleInvoices.get(0), sampleInvoices.get(1), sampleInvoices.get(3))
    }

    def "should update invoice by id"() {
        setup:
        saveSampleInvoicesToBase()
        Invoice updatedInvoice = sampleInvoices.get(1)
        updatedInvoice.setDate(LocalDate.of(1999, 1, 1))

        when:
        fileBasedDatabase.update(2, updatedInvoice)

        then:
        fileBasedDatabase.getAll().size() == 3
        fileBasedDatabase.getById(2).get() == updatedInvoice
    }

    def "should delete invoice by id"() {
        setup:
        saveSampleInvoicesToBase()

        when:
        fileBasedDatabase.delete(1)

        then:
        fileBasedDatabase.getAll().size() == 2
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
        incorrectId << [9, 100]
    }

    def "should throw IllegalArgumentException if there is no such id using update method"(int incorrectId) {
        setup:
        saveSampleInvoicesToBase()

        when:
        fileBasedDatabase.update(incorrectId, new Invoice())

        then:
        thrown(IllegalArgumentException)

        where:
        incorrectId << [9, 100]
    }
}
