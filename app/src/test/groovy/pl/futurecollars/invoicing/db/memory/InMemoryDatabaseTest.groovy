package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.InMemoryDataBase
import spock.lang.Specification

class InMemoryDatabaseTest extends Specification {

    def inMemoryBase
    def sampleInvoicesList


    def setup() {
        inMemoryBase = new InMemoryDataBase()
        sampleInvoicesList = TestHelpers.getSampleInvoicesList()
    }

    def "should save invoice to database"() {
        given:
        def invoice1 = sampleInvoicesList.get(0)
        def invoice2 = sampleInvoicesList.get(1)
        def invoice3 = sampleInvoicesList.get(2)

        when:
        invoice1.setId(inMemoryBase.save(invoice1))
        invoice2.setId(inMemoryBase.save(invoice2))
        invoice3.setId(inMemoryBase.save(invoice3))

        then:
        inMemoryBase.getAll().size() == 3
        inMemoryBase.getAll().get(0) == invoice1
        inMemoryBase.getAll().get(1) == invoice2
        inMemoryBase.getAll().get(2) == invoice3
    }

    def "should return optional with value if entry exist in base"() {
        setup:
        inMemoryBase.save(sampleInvoicesList.get(5))

        expect:
        inMemoryBase.getById(1) == Optional.of(sampleInvoicesList.get(5))
    }

    def "should return empty optional if id not exists"() {
        expect:
        inMemoryBase.getById(1000000) == Optional.empty()
    }

    def "should throw IllegalArgumentException if updated entry id does not exist"() {
        when:
        inMemoryBase.update(1000000, sampleInvoicesList.get(3))

        then:
        thrown(IllegalArgumentException)
    }

    def "should update entry in base"() {
        setup:
        def invoice = sampleInvoicesList.get(4)
        invoice.setId(inMemoryBase.save(invoice))
        def updatedInvoice = invoice

        when:
        updatedInvoice.setDate(invoice.getDate().minusDays(5))
        inMemoryBase.update(invoice.getId(), updatedInvoice)

        then:
        inMemoryBase.getById(1).get() == updatedInvoice
    }

    def "should delete entry from base"() {
        setup:
        inMemoryBase.save(sampleInvoicesList.get(0))

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
