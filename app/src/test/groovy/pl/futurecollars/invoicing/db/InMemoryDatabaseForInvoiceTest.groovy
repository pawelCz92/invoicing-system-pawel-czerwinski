package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.memory.InMemoryDataBase
import pl.futurecollars.invoicing.model.Invoice

class InMemoryDatabaseForInvoiceTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDataBase<Invoice>()
    }

    @Override
    List<WithId> getItemsList() {
        return TestHelpers.getSampleInvoicesList()
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
