package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
abstract class AbstractDatabaseTest extends Specification {

    List<Invoice> invoiceList = TestHelpers.getSampleInvoicesList()

    Database database = getDatabaseInstance()

    abstract Database getDatabaseInstance()


    def saveInvoices() {
        invoiceList.forEach(database::save)
        invoiceList = database.getAll()
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
        saveInvoices()

        when:
        List<Invoice> savedInvoices = database.getAll()

        then:
        invoiceList.size() == savedInvoices.size()
        invoiceList.containsAll(savedInvoices)
    }

    def "should return invoice by id"() {
        given:
        saveInvoices()
        int id1 = invoiceList.get(0).id
        int id2 = invoiceList.get(1).id

        when:
        Invoice resultInvoice1 = database.getById(id1).get()
        Invoice resultInvoice2 = database.getById(id2).get()

        then:
        resultInvoice1 == invoiceList.get(0)
        resultInvoice2 == invoiceList.get(1)
    }

    def "should return invoices without deleted one"() {
        given:
        saveInvoices()
        int idToDelete = 5

        when:
        database.delete(idToDelete)

        then:
        database.getAll().size() == invoiceList.size() - 1
        database.getById(5).isEmpty()
    }

    def "can delete all invoices"() {
        setup:
        saveInvoices()

        when:
        database.getAll().forEach(inv -> database.delete(inv.id))

        then:
        database.getAll().isEmpty()
    }

    def "should update invoice"() {
        setup:
        saveInvoices()
        Invoice invoiceToUpdate = database.getById(5).get()
        invoiceToUpdate.setNumber("xxxxxxxx")

        when:
        int idNew = database.update(invoiceToUpdate.getId(), invoiceToUpdate)
        invoiceToUpdate.setId(idNew)

        then:
        invoiceToUpdate == database.getById(idNew).get()
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
