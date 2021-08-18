package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.*
import pl.futurecollars.invoicing.service.JsonService
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
                /* 0*/ new InvoiceEntry("tv", BigDecimal.ONE, 1000.00, 230.00, Vat.VAT_23, null),
                /* 1*/ new InvoiceEntry("Radio", BigDecimal.ONE, 100.00, 23.00, Vat.VAT_23, null),
                /* 2*/ new InvoiceEntry("Radio", BigDecimal.ONE, 100.00, 23.00, Vat.VAT_23, null),
                /* 3*/ new InvoiceEntry("Dell XX", BigDecimal.ONE, 3000.00, 690.00, Vat.VAT_23, null),
                /* 4*/ new InvoiceEntry("Fuel", BigDecimal.valueOf(120), 600.00, 138.00, Vat.VAT_23, null),
                /* 5*/ new InvoiceEntry("Tv", BigDecimal.ONE, 2000.00, 460.00, Vat.VAT_23, null),
                /* 6*/ new InvoiceEntry("Car Clean", BigDecimal.ONE, 2000.00, 460.00, Vat.VAT_23,
                new Car(true, "HPE-2343")),
                /* 7*/ new InvoiceEntry("Desk", BigDecimal.valueOf(1.00), 100.00, 8.00, Vat.VAT_8, null),
                /* 8*/ new InvoiceEntry("Chair", BigDecimal.valueOf(1.00), 230.00, 18.40, Vat.VAT_8, null),
                /* 9*/ new InvoiceEntry("Table", BigDecimal.valueOf(1.00), 34.00, 7.82, Vat.VAT_23, null),
                /*10*/ new InvoiceEntry("Sofa", BigDecimal.valueOf(1.00), 230.00, 0.00, Vat.VAT_0, null),
                /*11*/ new InvoiceEntry("Printer", BigDecimal.valueOf(2), 300.00, 0.00, Vat.VAT_0, null),
                /*12*/ new InvoiceEntry("Speackres", BigDecimal.valueOf(3), 50.00, 0.00, Vat.VAT_0, null),
                /*13*/ new InvoiceEntry("Radiator", BigDecimal.valueOf(1.00), 80.00, 18.40, Vat.VAT_23, null),
                /*14*/ new InvoiceEntry("Internet", BigDecimal.valueOf(1.00), 50.00, 0.00, Vat.VAT_0, null),
                /*15*/ new InvoiceEntry("Door", BigDecimal.valueOf(1.00), 300.00, 24.00, Vat.VAT_8, null),
                /*16*/ new InvoiceEntry("Car", BigDecimal.valueOf(1.00), 20000.00, 1600.00, Vat.VAT_8,
                new Car(true, "EU-23421")),
                /*17*/ new InvoiceEntry("Tablet", BigDecimal.valueOf(1.00), 100.00, 8.00, Vat.VAT_8, null),
                /*18*/ new InvoiceEntry("SmartPhone", BigDecimal.valueOf(1.00), 1000.00, 80.00, Vat.VAT_8, null),
                /*19*/ new InvoiceEntry("Vacuum cleaner", BigDecimal.valueOf(1.00), 120.00, 27.60, Vat.VAT_23, null),
                /*20*/ new InvoiceEntry("Elecrtic grill", BigDecimal.valueOf(1.00), 1000.00, 230.00, Vat.VAT_23, null),
                /*21*/ new InvoiceEntry("Toster", BigDecimal.valueOf(1.00), 300.00, 15.00, Vat.VAT_5, null),
                /*22*/ new InvoiceEntry("Projector", BigDecimal.valueOf(1.00), 2000.00, 100.00, Vat.VAT_5, null),
                /*23*/ new InvoiceEntry("SmartWatch", BigDecimal.valueOf(1.00), 500.00, 115.00, Vat.VAT_23, null),
                /*24*/ new InvoiceEntry("Camera", BigDecimal.valueOf(1.00), 700.00, 35.00, Vat.VAT_5, null),
                /*25*/ new InvoiceEntry("Keyboard", BigDecimal.valueOf(1.00), 100.00, 0.00, Vat.VAT_0, null),
                /*26*/ new InvoiceEntry("Mouse", BigDecimal.valueOf(1.00), 50.00, 11.50, Vat.VAT_23, null),
                /*27*/ new InvoiceEntry("TvDecoder", BigDecimal.valueOf(1.00), 500.00, 115.00, Vat.VAT_23, null),
                /*28*/ new InvoiceEntry("Tires", BigDecimal.valueOf(1.00), 702.74, 161.63, Vat.VAT_23,
                new Car(true, "HPE-2343")),
                /*29*/ new InvoiceEntry("car service", BigDecimal.valueOf(1.00), 400.00, 92.00, Vat.VAT_23,
                new Car(false, "HPE-2343")),
                /*30*/ new InvoiceEntry("car wash", BigDecimal.valueOf(1.00), 50.00, 4.00, Vat.VAT_8, null),
                /*31*/ new InvoiceEntry("testIn", BigDecimal.valueOf(1.00), 76_011.62, 0.00, Vat.VAT_23, null),
                /*32*/ new InvoiceEntry("testOut", BigDecimal.valueOf(1.00), 11_329.47, 0.00, Vat.VAT_0, null),
                /*33*/ new InvoiceEntry("hdmi cable", BigDecimal.valueOf(5), 250.00, 20.00, Vat.VAT_8, null)
        )

        return List.of(
                Invoice.builder()
                        .date(LocalDate.of(2000, 11, 23))
                        .number("2021/05/09/1289")
                        .buyer(company1)
                        .seller(company2)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(1), invoiceEntries.get(3), invoiceEntries.get(9), invoiceEntries.get(10),
                                invoiceEntries.get(26), invoiceEntries.get(20), invoiceEntries.get(17))
                        )
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2005, 10, 25))
                        .number("2021/05/09/5675")
                        .buyer(company3)
                        .seller(company2)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(3), invoiceEntries.get(4), invoiceEntries.get(9), invoiceEntries.get(12),
                                invoiceEntries.get(11), invoiceEntries.get(22), invoiceEntries.get(12)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2009, 1, 22))
                        .number("2021/05/09/3333")
                        .buyer(company4)
                        .seller(company1)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(3), invoiceEntries.get(4), invoiceEntries.get(9), invoiceEntries.get(12),
                                invoiceEntries.get(11), invoiceEntries.get(22), invoiceEntries.get(12)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2010, 7, 22))
                        .number("2021/05/09/5555")
                        .buyer(company2)
                        .seller(company1)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(1), invoiceEntries.get(3), invoiceEntries.get(9), invoiceEntries.get(10),
                                invoiceEntries.get(26), invoiceEntries.get(20), invoiceEntries.get(17)))
                        .build(),

                Invoice.builder()
                        .date(LocalDate.of(2010, 2, 22))
                        .number("2021/05/09/2222")
                        .buyer(company4)
                        .seller(company3)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(9), invoiceEntries.get(10), invoiceEntries.get(11), invoiceEntries.get(10),
                                invoiceEntries.get(12), invoiceEntries.get(13), invoiceEntries.get(14)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2010, 2, 22))
                        .number("2021/05/09/1111")
                        .buyer(company4)
                        .seller(company5)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(22), invoiceEntries.get(16), invoiceEntries.get(28)))
                        .build(),

                Invoice.builder()
                        .date(LocalDate.of(2010, 2, 22))
                        .number("2021/05/09/9864")
                        .buyer(company6)
                        .seller(company4)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(22), invoiceEntries.get(16), invoiceEntries.get(28)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2012, 2, 23))
                        .number("2021/05/09/3278")
                        .buyer(company6)
                        .seller(company7)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(31)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2012, 2, 23))
                        .number("2021/05/09/3455")
                        .buyer(company7)
                        .seller(company6)
                        .invoiceEntries(List.of(
                                invoiceEntries.get(32)))
                        .build()
        )
    }
}
