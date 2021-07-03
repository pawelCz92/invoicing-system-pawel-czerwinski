package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.FileBasedDatabase
import pl.futurecollars.invoicing.db.InMemoryDataBase
import pl.futurecollars.invoicing.service.file.FileService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path


class InvoiceServiceTest extends Specification {

    private String fileNameForDataBaseTest = "testDBfile.json"
    private String fileNameForIdsTest = "testIdsFile.json"
    private Database fileDatabase
    private Database inMemoryDb
    private InvoiceService invoiceServiceForInMemoryDb
    private InvoiceService invoiceServiceForInFileDb


    def setup() {
        fileDatabase = new FileBasedDatabase(
                new FileService(fileNameForDataBaseTest),
                new IdProvider(fileNameForIdsTest),
                new JsonService())
        inMemoryDb = new InMemoryDataBase()
        invoiceServiceForInMemoryDb = new InvoiceService(inMemoryDb)
        invoiceServiceForInFileDb = new InvoiceService(fileDatabase)
    }

    def cleanup(){
        Files.deleteIfExists(Path.of(fileNameForIdsTest))
        Files.deleteIfExists(Path.of(fileNameForDataBaseTest))
    }

    def "should return empty list if in memory base is empty"(){
//        expect:
//        invoiceServiceForInFileDb.getAll() == List.of()
    }

    def "should save invoice to database "() {
       // setup:
        //invoiceServiceForInFileDb.save()
    }

    def "GetById"() {
    }

    def "GetAll"() {
    }

    def "Update"() {
    }

    def "Delete"() {
    }
}
