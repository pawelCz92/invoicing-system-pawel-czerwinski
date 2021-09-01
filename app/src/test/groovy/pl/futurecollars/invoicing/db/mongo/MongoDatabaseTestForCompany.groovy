package pl.futurecollars.invoicing.db.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.WithId
import pl.futurecollars.invoicing.model.Company

@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "mongo")
class MongoDatabaseTestForCompany extends AbstractDatabaseTest {

    @Autowired
    private Database<Company> mongoDatabase

    @Override
    Database getDatabaseInstance() {
        return mongoDatabase
    }

    @Override
    List<WithId> getItemsList() {
        return TestHelpers.getSampleCompaniesList()
    }
}
