package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

class FileBasedDatabaseTest extends Specification {

    List<Invoice> sampleInvoices
    FileBasedDatabase fileBasedDatabase

    def setup() {

        fileBasedDatabase = new FileBasedDatabase("testDBfile.json")

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

    def "should save invoices to data base file"() {

        when:
        sampleInvoices.forEach(invoice -> fileBasedDatabase.save(invoice))

        then:
        fileBasedDatabase.getAll() == sampleInvoices

        cleanup:
        Files.deleteIfExists(Path.of("testDBfile.json"))
        Files.deleteIfExists(Path.of("db-ids.json"))
    }

    def "GetById"() {
        setup:
        sampleInvoices.forEach(invoice -> fileBasedDatabase.save(invoice))

    }

    def "GetAll"() {
    }

    def "Update"() {
    }

    def "Delete"() {
    }
}
