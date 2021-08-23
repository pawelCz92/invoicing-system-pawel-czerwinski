package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.db.memory.InMemoryDataBase

class InMemoryDatabaseTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDataBase()
    }
}
