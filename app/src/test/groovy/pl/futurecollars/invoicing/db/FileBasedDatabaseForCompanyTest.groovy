package pl.futurecollars.invoicing.db

import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.file.FileBasedDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import pl.futurecollars.invoicing.service.file.IdProvider

import java.nio.file.Files
import java.nio.file.Path

@IfProfileValue(name = "spring.profiles.active", value = "file")
class FileBasedDatabaseForCompanyTest extends AbstractDatabaseTest {

    private Path dbPath

    @Override
    Database getDatabaseInstance() {

        FileService fileService = new FileService()
        Path idPath = Files.createTempFile("idsCompany", ".txt")
        dbPath = Files.createTempFile("dataCompany", ".txt")
        IdProvider idProvider = new IdProvider(idPath, fileService)

        return new FileBasedDatabase<Company>(dbPath, idProvider, fileService, new JsonService(), Company.class)
    }

    @Override
    List<Company> getItemsList() {
        return TestHelpers.getSampleCompaniesList()
    }

    def "should save data to right file"() {
        given:
        Database database = getDatabaseInstance()

        when:
        database.save(getItemsList().get(0))

        then:
        Files.readAllLines(dbPath).size() == 1

        when:
        database.save(getItemsList().get(0))

        then:
        Files.readAllLines(dbPath).size() == 2
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
