package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.util.stream.Collectors

class FileBasedDatabaseTest extends Specification {

    String fileNameForDataBaseTest = "testDBfile.json"
    String fileNameForIdsTest = "testIdsFile.json"
    List<Invoice> sampleInvoices
    FileBasedDatabase fileBasedDatabase

    def setup() {

        fileBasedDatabase = new FileBasedDatabase(fileNameForDataBaseTest, fileNameForIdsTest)

        Company buyer1 =
                new Company("382-22-1584", "377 Ohio Road Pulo", "Microsoft")
        Company seller1 =
                new Company("677-31-4788", "ul. Dluga Warszawa", "JBL")
        Company buyer2 =
                new Company("289-03-6711", "6 Stoughton Alley Plan de Ayala", "Voolith")
        Company seller2 =
                new Company("803-36-7695", "26 Claremont Street Borås", "Tazzy")
        Company buyer3 =
                new Company("161-65-1354", "75 Saint Paul Alley Nangela", "Fivespan")
        Company seller3 =
                new Company("714-52-4703", "6553 Fairview Drive Qorveh", "Realbuzz")

        List<InvoiceEntry> invoiceEntries = List.of(
                new InvoiceEntry("TV", 1050, 25, Vat.VAT_8),
                new InvoiceEntry("Radio", 2000, 59, Vat.VAT_23),
                new InvoiceEntry("Computer", 120, 0, Vat.VAT_0),
                new InvoiceEntry("Car", 1450, 25, Vat.VAT_8)
        )
        sampleInvoices = List.of(
                new Invoice(LocalDate.of(2000, 11, 23), buyer1, seller1, invoiceEntries),
                new Invoice(LocalDate.of(2005, 10, 25), buyer2, seller1, invoiceEntries),
                new Invoice(LocalDate.of(2021, 8, 11), buyer3, seller2, invoiceEntries),
                new Invoice(LocalDate.of(2010, 1, 22), buyer1, seller3, invoiceEntries),
        )
    }

    def cleanup() {
        Files.deleteIfExists(Path.of(fileNameForDataBaseTest))
        Files.deleteIfExists(Path.of(fileNameForIdsTest))
    }

    def saveSampleInvoicesToBase() {
        sampleInvoices.forEach(invoice -> fileBasedDatabase.save(invoice))
    }

    def "should save invoices to data base file"() {
        setup:
        JsonService jsonService = new JsonService()

        when:
        saveSampleInvoicesToBase()

        then:
        List<Invoice> invoicesFromFile = Files.readAllLines(Path.of(fileNameForDataBaseTest)).stream()
                .map(line -> jsonService.stringToObject(line, Invoice.class))
                .collect(Collectors.toList())

        invoicesFromFile.size() == sampleInvoices.size()
        invoicesFromFile == sampleInvoices
    }

    def "should read last id from file and start generate id from this number"() {
        setup:
        saveSampleInvoicesToBase()
        FileService fileService = new FileService(fileNameForIdsTest)
        fileService.writeLine("100")
        saveSampleInvoicesToBase()
        List<Invoice> savedInvoices = fileBasedDatabase.getAll()
        Invoice lastSavedInvoice = savedInvoices.get(savedInvoices.size() - 1)

        expect:
        lastSavedInvoice.getId() == 104

    }

    def "should find invoice by id"() {
        setup:
        saveSampleInvoicesToBase()
        saveSampleInvoicesToBase()

        when:
        Optional<Invoice> founded1 = fileBasedDatabase.getById(1)
        Optional<Invoice> founded2 = fileBasedDatabase.getById(6)

        then:
        founded1 != Optional.empty()
        founded2 != Optional.empty()
        founded1.get().getId() == 1
        founded2.get().getId() == 6
    }

    def "should get all invoices from base"() {
        setup:
        saveSampleInvoicesToBase()

        expect:
        fileBasedDatabase.getAll() == sampleInvoices

    }

    def "should return empty Optional if searched ID is not unique in base"() {
        setup:
        FileService fileService = new FileService(fileNameForDataBaseTest)
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":4,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":6,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        expect:
        fileService.findLineById(1) == Optional.empty()
    }

    def "should return empty Optional if there is no such ID"() {
        setup:
        FileService fileService = new FileService(fileNameForDataBaseTest)
        fileService.writeLine("{\"id\":1,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":2,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":3,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":4,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")
        fileService.writeLine("{\"id\":6,\"date\":\"2000-11-23\", AND REST TEXT NOT IMPORTANT FOR THIS TEST")

        expect:
        fileService.findLineById(10000) == Optional.empty()
    }

    def "Update"() {
    }

    def "Delete"() {
    }
}
