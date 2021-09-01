package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.WithId
import pl.futurecollars.invoicing.db.memory.InMemoryDataBase
import pl.futurecollars.invoicing.model.Company

class InMemoryDatabaseForCompanyTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDataBase<Company>()
    }

    @Override
    List<WithId> getItemsList() {
        return TestHelpers.getSampleCompaniesList()
    }

    def "should update company"() {
        setup:
        saveItems()
        long companyId = database.getAll().get(1).getId();
        Company companyToUpdate = database.getById(companyId).get() as Company
        companyToUpdate.setName("xxxxxxxx")

        when:
        database.update(companyToUpdate.getId(), companyToUpdate)

        then:
        jsonService.objectToString(companyToUpdate) == jsonService.objectToString(database.getById(companyId).get())
    }
}
