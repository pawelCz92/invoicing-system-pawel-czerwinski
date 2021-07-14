package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class TestHelpers extends Specification {

    def "should return suitable values of Vat"() {
        expect:
        Vat.VAT_23.getRate() == 23
        Vat.VAT_5.getRate() == 5
        Vat.VAT_0.getRate() == 0
    }

    def "should return suitable description for Vat"(Vat vat, String description) {
        expect:
        vat.toString() == description

        where:
        vat        | description
        Vat.VAT_23 | "Vat.VAT_23(rate=23)"
        Vat.VAT_5  | "Vat.VAT_5(rate=5)"
        Vat.VAT_0  | "Vat.VAT_0(rate=0)"
    }

    static def getSampleInvoicesList() {
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

        List<InvoiceEntry> invoiceEntries = List.of(
                new InvoiceEntry("TV", 1050, 25, Vat.VAT_8),
                new InvoiceEntry("Radio", 2000, 59, Vat.VAT_23),
                new InvoiceEntry("Computer", 120, 0, Vat.VAT_0),
                new InvoiceEntry("Car", 1450, 25, Vat.VAT_8)
        )

        List<InvoiceEntry> invoiceEntries2 = List.of(
                new InvoiceEntry("TV", 10, 12, Vat.VAT_8),
                new InvoiceEntry("Radio", 45, 55, Vat.VAT_23),
                new InvoiceEntry("Computer", 55, 0, Vat.VAT_0),
                new InvoiceEntry("Car", 60, 2, Vat.VAT_8)
        )

        return List.of(
                new Invoice(LocalDate.of(2000, 11, 23), buyer1, seller1, invoiceEntries),
                new Invoice(LocalDate.of(2005, 10, 25), buyer2, seller1, invoiceEntries),
                new Invoice(LocalDate.of(2021, 8, 11), buyer3, seller2, invoiceEntries),
                new Invoice(LocalDate.of(2010, 7, 22), seller1, buyer1, invoiceEntries),
                new Invoice(LocalDate.of(2010, 7, 22), buyer1, seller1, invoiceEntries2),
                new Invoice(LocalDate.of(2010, 2, 22), seller2, buyer2, invoiceEntries),
        )
    }
}
