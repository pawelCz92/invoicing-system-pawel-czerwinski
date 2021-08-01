package pl.futurecollars.invoicing.db

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import pl.futurecollars.invoicing.service.file.IdProvider
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("file")
class FileBasedDatabaseTest extends Specification {

    @Autowired
    private FileBasedDatabase fileBasedDatabase
    @Autowired
    private IdProvider idProvider
    @Autowired
    private FileService fileServiceForData
    private static List<Invoice> sampleInvoices

    def setupSpec() {
        sampleInvoices = TestHelpers.getSampleInvoicesList()
    }

    def cleanup(){
        fileBasedDatabase.deleteAll()
    }

    def setup() {
        idProvider.deleteAll()
        fileBasedDatabase.deleteAll()
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
        List<Invoice> invoicesInBase = fileBasedDatabase.getAll()

        invoicesInBase.size() == 3
        invoicesInBase.containsAll(List.of(sampleInvoices.get(0), sampleInvoices.get(1), sampleInvoices.get(3)))
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
