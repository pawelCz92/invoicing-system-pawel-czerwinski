package pl.futurecollars.invoicing.db.sql

import org.flywaydb.core.Flyway
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.sql.sql.SqlDatabaseForCompany
import pl.futurecollars.invoicing.model.Company

import javax.sql.DataSource
import java.util.stream.Collectors

class SqlDatabaseForCompanyTest extends AbstractDatabaseTest {

    private Database database

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

        database = new SqlDatabaseForCompany(jdbcTemplate)

        return database
    }

    @Override
    List<Company> getItemsList() {
        return new ArrayList<Company>(TestHelpers.getSampleInvoicesList().stream()
                .map({ inv -> List.of(inv.getBuyer(), inv.getSeller()) })
                .flatMap({ companyList -> companyList.stream() })
                .collect(Collectors.toSet()))
    }

    def "should update company tin and name"() {
        given:
        Company companyToUpdate = TestHelpers.getSampleInvoicesList().get(0).getBuyer()
        Company companyAfterUpdate = companyToUpdate
        companyAfterUpdate.setName("XYZ")
        companyAfterUpdate.setTaxIdentificationNumber("222222222")

        when:
        companyToUpdate.setId(database.save(companyToUpdate))
        database.update(companyToUpdate.getId(), companyAfterUpdate)

        then:
        companyAfterUpdate == database.getById(companyToUpdate.id).get()
    }

    def "should throw IllegalArgumentException if updated company is null"() {
        when:
        database.update(1, null)

        then:
        thrown(IllegalArgumentException.class)
    }
}
