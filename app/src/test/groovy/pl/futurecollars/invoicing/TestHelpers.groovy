package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.*
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDate

class TestHelpers extends Specification {

    private static List<InvoiceEntry> invoiceEntries
    private static JsonService jsonService = new JsonService()

    def "should return suitable values of Vat"() {
        expect:
        Vat.VAT_23.getRate() == 23
        Vat.VAT_5.getRate() == 5
        Vat.VAT_0.getRate() == 0
    }

    static def getSampleInvoicesList() {
        Company company1 =
                new Company("382-22-1584", "377 Ohio Road Pulo", "Microsoft",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        Company company2 =
                new Company("677-31-4788", "ul. Dluga Warszawa", "JBL",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        Company company3 =
                new Company("289-03-6711", "6 Stoughton Alley Plan de Ayala", "Voolith",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        Company company4 =
                new Company("803-36-7695", "26 Claremont Street Borås", "Tazzy",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        Company company5 =
                new Company("161-65-1354", "75 Saint Paul Alley Nangela", "Fivespan",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        Company company6 =
                new Company("100-16-1976", "67 Nangela", "Samsung",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        Company company7 =
                new Company("100-16-0000", "67 Nangela", "Test",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))

        invoiceEntries = List.of(
                /* 0*/ new InvoiceEntry("tv", 1, 1000, 230, Vat.VAT_23),
                /* 1*/ new InvoiceEntry("Radio", 1, 100, 23, Vat.VAT_23),
                /* 2*/ new InvoiceEntry("Radio", 1, 100, 23, Vat.VAT_23),
                /* 3*/ new InvoiceEntry("Dell XX", 1, 3000, 690, Vat.VAT_23),
                /* 4*/ new InvoiceEntry("Fuel", 120, 600, 138, Vat.VAT_23),
                /* 5*/ new InvoiceEntry("Tv", 1, 2000, 460, Vat.VAT_23),
                /* 6*/ new InvoiceEntry("Car Clean", 1, 2000, 460, Vat.VAT_23,
                new Car(true, "HPE-2343")),
                /* 7*/ new InvoiceEntry("Desk", 1, 100, 8, Vat.VAT_8),
                /* 8*/ new InvoiceEntry("Chair", 1, 230, 18.4, Vat.VAT_8),
                /* 9*/ new InvoiceEntry("Table", 1, 34, 7.82, Vat.VAT_23),
                /*10*/ new InvoiceEntry("Sofa", 1, 230, 0, Vat.VAT_0),
                /*11*/ new InvoiceEntry("Printer", 2, 300, 0, Vat.VAT_0),
                /*12*/ new InvoiceEntry("Speackres", 3, 50, 0, Vat.VAT_0),
                /*13*/ new InvoiceEntry("Radiator", 1, 80, 18.4, Vat.VAT_23),
                /*14*/ new InvoiceEntry("Internet", 1, 50, 0, Vat.VAT_0),
                /*15*/ new InvoiceEntry("Door", 1, 300, 24, Vat.VAT_8),
                /*16*/ new InvoiceEntry("Car", 1, 20000, 1600, Vat.VAT_8,
                new Car(true, "EU-23421")),
                /*17*/ new InvoiceEntry("Tablet", 1, 100, 8, Vat.VAT_8),
                /*18*/ new InvoiceEntry("SmartPhone", 1, 1000, 80, Vat.VAT_8),
                /*19*/ new InvoiceEntry("Vacuum cleaner", 1, 120, 27.6, Vat.VAT_23),
                /*20*/ new InvoiceEntry("Elecrtic grill", 1, 1000, 230, Vat.VAT_23),
                /*21*/ new InvoiceEntry("Toster", 1, 300, 15, Vat.VAT_5),
                /*22*/ new InvoiceEntry("Projector", 1, 2000, 100, Vat.VAT_5),
                /*23*/ new InvoiceEntry("SmartWatch", 1, 500, 115, Vat.VAT_23),
                /*24*/ new InvoiceEntry("Camera", 1, 700, 35, Vat.VAT_5),
                /*25*/ new InvoiceEntry("Keyboard", 1, 100, 0, Vat.VAT_0),
                /*26*/ new InvoiceEntry("Mouse", 1, 50, 11.5, Vat.VAT_23),
                /*27*/ new InvoiceEntry("TvDecoder", 1, 500, 115, Vat.VAT_23),
                /*28*/ new InvoiceEntry("Tires", 1, 702.74, 161.63, Vat.VAT_23,
                new Car(true, "HPE-2343")),
                /*29*/ new InvoiceEntry("car service", 1, 400, 92, Vat.VAT_23,
                new Car(false, "HPE-2343")),
                /*30*/ new InvoiceEntry("car wash", 1, 50, 4, Vat.VAT_8),
                /*31*/ new InvoiceEntry("testIn", 1,
                BigDecimal.valueOf(76_011.62), BigDecimal.valueOf(0), Vat.VAT_23),
                /*32*/ new InvoiceEntry("testOut", 1, BigDecimal.valueOf(11_329.47), 0, Vat.VAT_0),
                /*33*/ new InvoiceEntry("hdmi cable", 5, 250, 20, Vat.VAT_8)
        )

        return List.of(
                new Invoice(LocalDate.of(2000, 11, 23), company1, company2, List.of(
                        invoiceEntries.get(1), invoiceEntries.get(3), invoiceEntries.get(9), invoiceEntries.get(10),
                        invoiceEntries.get(26), invoiceEntries.get(20), invoiceEntries.get(17)
                )),
                new Invoice(LocalDate.of(2005, 10, 25), company3, company2, List.of(
                        invoiceEntries.get(3), invoiceEntries.get(4), invoiceEntries.get(9), invoiceEntries.get(12),
                        invoiceEntries.get(11), invoiceEntries.get(22), invoiceEntries.get(12)
                )),
                new Invoice(LocalDate.of(2009, 1, 22), company4, company1, List.of(
                        invoiceEntries.get(3), invoiceEntries.get(4), invoiceEntries.get(9), invoiceEntries.get(12),
                        invoiceEntries.get(11), invoiceEntries.get(22), invoiceEntries.get(12)
                )),
                new Invoice(LocalDate.of(2010, 7, 22), company2, company1, List.of(
                        invoiceEntries.get(1), invoiceEntries.get(3), invoiceEntries.get(9), invoiceEntries.get(10),
                        invoiceEntries.get(26), invoiceEntries.get(20), invoiceEntries.get(17)
                )),
                new Invoice(LocalDate.of(2010, 2, 22), company4, company3, List.of(
                        invoiceEntries.get(9), invoiceEntries.get(10), invoiceEntries.get(11), invoiceEntries.get(10),
                        invoiceEntries.get(12), invoiceEntries.get(13), invoiceEntries.get(14)
                )),
                new Invoice(LocalDate.of(2010, 2, 22), company4, company5, List.of(
                        invoiceEntries.get(22), invoiceEntries.get(16), invoiceEntries.get(28)
                )),
                new Invoice(LocalDate.of(2010, 2, 22), company6, company4, List.of(
                        invoiceEntries.get(22), invoiceEntries.get(16), invoiceEntries.get(28)
                )),
                new Invoice(LocalDate.of(2012, 2, 23), company6, company7, List.of(
                        invoiceEntries.get(31)
                )),
                new Invoice(LocalDate.of(2012, 2, 23), company7, company6, List.of(
                        invoiceEntries.get(32)
                ))
        )
    }

    @Ignore
    def doSth() {
        setup:
        getSampleInvoicesList().forEach(invoice -> {
            println(invoice.getDate())
            println("  Buyer: " + invoice.buyer)
            println("  Seller: " + invoice.seller)
            invoice.getInvoiceEntries().forEach(invEntr -> println("      " + invEntr))
        })
    }
}
