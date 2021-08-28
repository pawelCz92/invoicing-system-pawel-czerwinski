package pl.futurecollars.invoicing.db.sql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.WithId
import pl.futurecollars.invoicing.db.sql.jpa.InvoiceRepository
import pl.futurecollars.invoicing.db.sql.jpa.JpaDatabase
import pl.futurecollars.invoicing.model.Invoice

import java.util.stream.Collectors

@DataJpaTest
@IfProfileValue(name = "spring.profiles.active", value = "jpa")
class JpaDatabaseForInvoiceTest extends AbstractDatabaseTest {

    @Autowired
    private InvoiceRepository invoiceRepository

    @Override
    Database getDatabaseInstance() {
        assert invoiceRepository != null
        return new JpaDatabase<Invoice>(invoiceRepository)
    }

    @Override
    List<WithId> getItemsList() {

        List<Invoice> invoices = new ArrayList<>()

        invoices.addAll(
                TestHelpers.getSampleInvoicesList().get(0),
                TestHelpers.getSampleInvoicesList().get(4),
                TestHelpers.getSampleInvoicesList().get(8)
        )
Arrays.asList()
        invoices = invoices.stream()
                .map({ inv ->
                    inv.getBuyer().setId(null)
                    inv.getSeller().setId(null)
                    return inv
                })
                .collect(Collectors.toList())
        return invoices
    }

    def "should update invoice"() {
        setup:
        saveItems()

        long invoiceId = database.getAll().get(1).getId()
        Invoice invoiceToUpdate = database.getById(invoiceId).get() as Invoice
        invoiceToUpdate.setNumber("xxxxxxxx")

        when:
        database.update(invoiceToUpdate.getId(), invoiceToUpdate)

        then:
        jsonService.objectToString(invoiceToUpdate) == jsonService.objectToString(database.getById(invoiceId).get())
    }
}
