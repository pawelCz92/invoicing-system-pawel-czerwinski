package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
abstract class AbstractDatabaseTest extends Specification {

    List<Invoice> invoiceList = TestHelpers.getSampleInvoicesList()
    JsonService jsonService = new JsonService()

    abstract Database getDatabaseInstance()

    Database database

    def setup() {
        database = getDatabaseInstance()
        database.reset()

        assert database.getAll().isEmpty()
    }

    def saveInvoices() {
        List<Invoice> invoicesToSave = List.of(
                invoiceList.get(0),
                invoiceList.get(4),
                invoiceList.get(8)
        )
        invoicesToSave.forEach({ inv -> database.save(inv) })

    }

    def "should inject database instance"() {
        expect:
        database
    }

    def "should return empty collection if there is no invoices in base"() {
        expect:
        database.getAll().isEmpty()
    }

    def "should return empty optional if there is no invoice with given id"() {
        expect:
        database.getById(1).isEmpty()
    }

    def "should return all saved invoices"() {
        setup:
        List<Invoice> invoicesToSave = List.of(
                invoiceList.get(0),
                invoiceList.get(4),
                invoiceList.get(8)
        )
        invoicesToSave.forEach({ inv -> database.save(inv) })

        when:
        List<Invoice> savedInvoices = database.getAll()

        then:
        invoicesToSave.size() == savedInvoices.size()
        jsonService.objectToString(invoicesToSave) == jsonService.objectToString(savedInvoices)
    }

    def "should return invoice by id"() {
        setup:
        List<Invoice> invoicesToSave = List.of(
                invoiceList.get(0),
                invoiceList.get(4),
                invoiceList.get(8)
        )
        invoicesToSave.forEach({ inv -> database.save(inv) })
        int id1 = invoiceList.get(0).id
        int id2 = invoiceList.get(8).id

        when:
        Invoice resultInvoice1 = database.getById(id1).get()
        Invoice resultInvoice2 = database.getById(id2).get()

        then:
        jsonService.objectToString(resultInvoice1) == jsonService.objectToString(invoiceList.get(0))
        jsonService.objectToString(resultInvoice2) == jsonService.objectToString(invoiceList.get(8))
    }

    def "should return invoices without deleted one"() {
        given:
        saveInvoices()
        int invoicesNumberBeforeDelete = database.getAll().size()
        int idToDelete = database.getAll().get(0).getId()

        when:
        database.delete(idToDelete)

        then:
        database.getAll().size() == invoicesNumberBeforeDelete - 1
        database.getById(idToDelete).isEmpty()
    }

    def "can delete all invoices"() {
        setup:
        saveInvoices()

        when:
        database.getAll().forEach({ inv -> database.delete(inv.id) })

        then:
        database.getAll().isEmpty()
    }

    def "should update invoice"() {
        setup:
        saveInvoices()
        int invoiceId = database.getAll().get(1).getId();
        Invoice invoiceToUpdate = database.getById(invoiceId).get()
        invoiceToUpdate.setNumber("xxxxxxxx")

        when:
        database.update(invoiceToUpdate.getId(), invoiceToUpdate)

        then:
        jsonService.objectToString(invoiceToUpdate) == jsonService.objectToString(database.getById(invoiceId).get())
    }

    def "should throw illegalArgumentException if there is no id using update"() {
        when:
        database.update(999999, invoiceList.get(0))

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw illegalArgumentException if there is no id using delete"() {
        when:
        database.delete(999888)

        then:
        thrown(IllegalArgumentException)
    }
}
