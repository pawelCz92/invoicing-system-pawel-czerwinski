package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.InMemoryDataBase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class InMemoryDatabaseTest extends Specification {

    def inMemoryBase
    def seller
    def buyer
    def invoiceEntries
    def invoice

    def setup() {
        inMemoryBase = new InMemoryDataBase()
        seller = new Company("654654654", "ul Kreta, Poznan", "Company S.A")
        buyer = new Company("9465444445", "ul Poboczna, Wroclaw", "Jan Kowalski")
        invoiceEntries = List.of(
                new InvoiceEntry("TV", BigDecimal.valueOf(2000), BigDecimal.valueOf(460), Vat.VAT_23),
                new InvoiceEntry("Radio", BigDecimal.valueOf(1000), BigDecimal.valueOf(230), Vat.VAT_23))
        invoice = new Invoice((LocalDate.of(2010, 2, 14)), buyer, seller, invoiceEntries)
        inMemoryBase.save(invoice)
    }

    def "should save invoice to database"() {
        when:
        inMemoryBase.save(invoice)

        then:
        inMemoryBase.getAll().size() == 2
        inMemoryBase.getAll().get(1) == invoice
    }

    def "should return optional with value if entry exist in base"() {
        expect:
        inMemoryBase.getById(1) == Optional.of(invoice)
    }

    def "should return empty optional if id not exists"() {
        expect:
        inMemoryBase.getById(1000000) == Optional.empty()
    }

    def "should throw IllegalArgumentException if updated entry id does not exist"() {
        when:
        inMemoryBase.update(1000000, invoice)

        then:
        thrown(IllegalArgumentException)
    }

    def "should update entry in base"() {
        given:
        def updatedInvoice = invoice

        when:
        updatedInvoice.setDate(invoice.getDate().minusDays(5))
        inMemoryBase.update(invoice.getId(), updatedInvoice)

        then:
        inMemoryBase.getById(1).get() == updatedInvoice
    }

    def "should delete entry from base"() {
        when:
        inMemoryBase.delete(1)

        then:
        inMemoryBase.getById(1) == Optional.empty()
    }

    def "should throw IllegalArgumentException if deleted entry id does not exist"() {
        when:
        inMemoryBase.delete(1000000)

        then:
        thrown(IllegalArgumentException)
    }
}
