package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.db.InMemoryDataBase

class InMemoryDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDataBase()
    }
}
