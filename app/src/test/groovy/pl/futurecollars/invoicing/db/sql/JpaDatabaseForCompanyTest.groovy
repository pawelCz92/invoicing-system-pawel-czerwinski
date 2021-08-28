package pl.futurecollars.invoicing.db.sql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.WithId
import pl.futurecollars.invoicing.db.sql.jpa.CompanyRepository
import pl.futurecollars.invoicing.db.sql.jpa.InvoiceRepository
import pl.futurecollars.invoicing.db.sql.jpa.JpaDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice

@DataJpaTest
@IfProfileValue(name = "spring.profiles.active", value = "jpa")
class JpaDatabaseForCompanyTest extends AbstractDatabaseTest {

    @Autowired
    private CompanyRepository companyRepository

    @Override
    Database getDatabaseInstance() {
        assert companyRepository != null
        return new JpaDatabase<Company>(companyRepository)
    }

    @Override
    List<WithId> getItemsList() {
        return TestHelpers.getSampleCompaniesList()
    }

    def "should update company"() {
        setup:
        saveItems()
        long companyId = database.getAll().get(1).getId()
        Company companyToUpdate = database.getById(companyId).get() as Company
        companyToUpdate.setName("xxxxxxxx")

        when:
        database.update(companyToUpdate.getId(), companyToUpdate)

        then:
        jsonService.objectToString(companyToUpdate) == jsonService.objectToString(database.getById(companyId).get())
    }
}
