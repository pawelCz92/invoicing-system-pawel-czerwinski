package pl.futurecollars.invoicing.db.sql

import org.flywaydb.core.Flyway
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.WithId
import pl.futurecollars.invoicing.db.sql.sql.SqlDatabaseForInvoice
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice

import javax.sql.DataSource
import java.time.LocalDate

class SqlDatabaseForInvoiceTest extends AbstractDatabaseTest {

    private Database database
    private Company company3 =
            new Company("289-03-6711", "6 Stoughton Alley Plan de Ayala", "Voolith",
                    BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
    private Company company4 =
            new Company("803-36-7695", "26 Claremont Street Borås", "Tazzy",
                    BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))

    @Override
    Database getDatabaseInstance() {

        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .load()

        flyway.clean()
        flyway.migrate()

        database = new SqlDatabaseForInvoice(jdbcTemplate)

        return database
    }

    @Override
    List<WithId> getItemsList() {
        return TestHelpers.getSampleInvoicesList()
    }

    def "should update invoice date and number"() {
        given:
        Invoice invoiceToUpdate = TestHelpers.getSampleInvoicesList().get(0)
        Invoice invoiceAfterUpdate = invoiceToUpdate
        invoiceAfterUpdate.setDate(LocalDate.of(1990, 9, 12))
        invoiceAfterUpdate.setNumber("xxx")

        when:
        invoiceToUpdate.setId(database.save(invoiceToUpdate))
        database.update(invoiceToUpdate.getId(), invoiceAfterUpdate)

        then:
        invoiceAfterUpdate.date == database.getById(invoiceToUpdate.id).
                map({ inv -> inv.getDate() }).orElse(null)
        invoiceAfterUpdate.number == database.getById(invoiceToUpdate.id).
                map({ inv -> inv.getNumber() }).orElse(null)
    }

    def "should update invoice buyer and seller for existing company"() {
        setup:
        Invoice invoice1 = database.getById(
                database.save(TestHelpers.getSampleInvoicesList().get(0))).get()
        Invoice invoice2 = database.getById(
                database.save(TestHelpers.getSampleInvoicesList().get(4))).get()
        int company3Id = invoice2.getBuyer().getId()
        int company4Id = invoice2.getSeller().getId()

        when:
        invoice1.setBuyer(company4)
        invoice1.setSeller(company3)
        database.update(invoice1.id, invoice1)

        then:
        database.getById(invoice1.id).get().getBuyer().getId() == company3Id
        database.getById(invoice1.id).get().getSeller().getId() == company4Id
    }

    def "should update invoice buyer and seller for not existing company"() {
        setup:
        Invoice invoice1 = database.getById(
                database.save(TestHelpers.getSampleInvoicesList().get(0))).get()
        int buyerIdBeforeUpdate = invoice1.getBuyer().getId()
        int sellerIdBeforeUpdate = invoice1.getSeller().getId()

        Company newBuyer =
                new Company("999-99-9999", "Ohio Pulo", "Panasonic",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        Company newSeller =
                new Company("888-88-888", "ul. Dluga Woclaw", "JVC",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        int lastBuyerId = database.getAll().stream()
                .map ({ inv -> inv.getBuyer().id })
                .max( {x, y -> Integer.compare(x,y)}).orElse(0)
        int lastSellerId = database.getAll().stream()
                .map({ inv -> inv.getSeller().id })
                .max({ x, y -> Integer.compare(x, y) }).orElse(0)

        int lastCompanyId = lastBuyerId > lastSellerId ? lastBuyerId : lastSellerId

        when:
        invoice1.setBuyer(newBuyer)
        invoice1.setSeller(newSeller)
        database.update(invoice1.getId(), invoice1)
        Invoice updatedInvoice = database.getById(invoice1.getId()).get()

        then:
        updatedInvoice.getBuyer().getId() != buyerIdBeforeUpdate
        updatedInvoice.getSeller().getId() != sellerIdBeforeUpdate
        updatedInvoice.getBuyer().getId() > lastCompanyId
        updatedInvoice.getSeller().getId() > lastCompanyId
    }
}
