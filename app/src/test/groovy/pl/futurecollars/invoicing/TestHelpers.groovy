package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.*
import spock.lang.Specification

import java.time.LocalDate

class TestHelpers extends Specification {

    private static List<InvoiceEntry> invoiceEntries
    private static List<Company> sampleCompaniesList = getSampleCompaniesList()

    def "should return suitable values of Vat"() {
        expect:
        Vat.VAT_23.getRate() == 23
        Vat.VAT_5.getRate() == 5
        Vat.VAT_0.getRate() == 0
    }

    static def getSampleCompaniesList() {
        return List.of(
                new Company("382-22-1584", "377 Ohio Road Pulo", "Microsoft",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57)),
                new Company("677-31-4788", "ul. Dluga Warszawa", "JBL",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57)),
                new Company("289-03-6711", "6 Stoughton Alley Plan de Ayala", "Voolith",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57)),
                new Company("803-36-7695", "26 Claremont Street Borås", "Tazzy",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57)),
                new Company("161-65-1354", "75 Saint Paul Alley Nangela", "Fivespan",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57)),
                new Company("100-16-1976", "67 Nangela", "Samsung",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57)),
                new Company("100-16-0000", "67 Nangela", "Test",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        )
    }

    static def getSampleInvoicesList() {
        invoiceEntries = List.of(
                /* 0*/ new InvoiceEntry("tv", BigDecimal.valueOf(1.00).setScale(2), 1000.00, 230.00, Vat.VAT_23, null),
                /* 1*/ new InvoiceEntry("RadioX", BigDecimal.valueOf(1.00).setScale(2), 100.00, 23.00, Vat.VAT_23, null),
                /* 2*/ new InvoiceEntry("RadioY", BigDecimal.valueOf(1.00).setScale(2), 100.00, 23.00, Vat.VAT_23, null),
                /* 3*/ new InvoiceEntry("Dell XX", BigDecimal.valueOf(1.00).setScale(2), 3000.00, 690.00, Vat.VAT_23, null),
                /* 4*/ new InvoiceEntry("Fuel", BigDecimal.valueOf(120).setScale(2), 600.00, 138.00, Vat.VAT_23, null),
                /* 5*/ new InvoiceEntry("Tv", BigDecimal.valueOf(1.00).setScale(2), 2000.00, 460.00, Vat.VAT_23, null),
                /* 6*/ new InvoiceEntry("Car Clean", BigDecimal.valueOf(1.00).setScale(2), 2000.00, 460.00, Vat.VAT_23,
                new Car(true, "HPE-2343")),
                /* 7*/ new InvoiceEntry("Desk", BigDecimal.valueOf(1.00).setScale(2), 100.00, 8.00, Vat.VAT_8, null),
                /* 8*/ new InvoiceEntry("Chair", BigDecimal.valueOf(1.00).setScale(2), 230.00, 18.40, Vat.VAT_8, null),
                /* 9*/ new InvoiceEntry("Table", BigDecimal.valueOf(1.00).setScale(2), 34.00, 7.82, Vat.VAT_23, null),
                /*10*/ new InvoiceEntry("Sofa", BigDecimal.valueOf(1.00).setScale(2), 230.00, 0.00, Vat.VAT_0, null),
                /*11*/ new InvoiceEntry("Printer", BigDecimal.valueOf(2.00).setScale(2), 300.00, 0.00, Vat.VAT_0, null),
                /*12*/ new InvoiceEntry("Speackres", BigDecimal.valueOf(3.00).setScale(2), 50.00, 0.00, Vat.VAT_0, null),
                /*13*/ new InvoiceEntry("Radiator", BigDecimal.valueOf(1.00).setScale(2), 80.00, 18.40, Vat.VAT_23, null),
                /*14*/ new InvoiceEntry("Internet", BigDecimal.valueOf(1.00).setScale(2), 50.00, 0.00, Vat.VAT_0, null),
                /*15*/ new InvoiceEntry("Door", BigDecimal.valueOf(1.00).setScale(2), 300.00, 24.00, Vat.VAT_8, null),
                /*16*/ new InvoiceEntry("Car", BigDecimal.valueOf(1.00).setScale(2), 20000.00, 1600.00, Vat.VAT_8,
                new Car(true, "EU-23421")),
                /*17*/ new InvoiceEntry("Tablet", BigDecimal.valueOf(1.00).setScale(2), 100.00, 8.00, Vat.VAT_8, null),
                /*18*/ new InvoiceEntry("SmartPhone", BigDecimal.valueOf(1.00).setScale(2), 1000.00, 80.00, Vat.VAT_8, null),
                /*19*/ new InvoiceEntry("Vacuum cleaner", BigDecimal.valueOf(1.00).setScale(2), 120.00, 27.60, Vat.VAT_23, null),
                /*20*/ new InvoiceEntry("Elecrtic grill", BigDecimal.valueOf(1.00).setScale(2), 1000.00, 230.00, Vat.VAT_23, null),
                /*21*/ new InvoiceEntry("Toster", BigDecimal.valueOf(1.00).setScale(2), 300.00, 15.00, Vat.VAT_5, null),
                /*22*/ new InvoiceEntry("Projector", BigDecimal.valueOf(1.00).setScale(2), 2000.00, 100.00, Vat.VAT_5, null),
                /*23*/ new InvoiceEntry("SmartWatch", BigDecimal.valueOf(1.00).setScale(2), 500.00, 115.00, Vat.VAT_23, null),
                /*24*/ new InvoiceEntry("Camera", BigDecimal.valueOf(1.00).setScale(2), 700.00, 35.00, Vat.VAT_5, null),
                /*25*/ new InvoiceEntry("Keyboard", BigDecimal.valueOf(1.00).setScale(2), 100.00, 0.00, Vat.VAT_0, null),
                /*26*/ new InvoiceEntry("Mouse", BigDecimal.valueOf(1.00).setScale(2), 50.00, 11.50, Vat.VAT_23, null),
                /*27*/ new InvoiceEntry("TvDecoder", BigDecimal.valueOf(1.00).setScale(2), 500.00, 115.00, Vat.VAT_23, null),
                /*28*/ new InvoiceEntry("Tires", BigDecimal.valueOf(1.00).setScale(2), 702.74, 161.63, Vat.VAT_23,
                new Car(true, "HPE-2343")),
                /*29*/ new InvoiceEntry("car service", BigDecimal.valueOf(1.00).setScale(2), 400.00, 92.00, Vat.VAT_23,
                new Car(false, "HPE-2343")),
                /*30*/ new InvoiceEntry("car wash", BigDecimal.valueOf(1.00).setScale(2), 50.00, 4.00, Vat.VAT_8, null),
                /*31*/ new InvoiceEntry("testIn", BigDecimal.valueOf(1.00).setScale(2), 76_011.62, 0.00, Vat.VAT_23, null),
                /*32*/ new InvoiceEntry("testOut", BigDecimal.valueOf(1.00).setScale(2), 11_329.47, 0.00, Vat.VAT_0, null),
                /*33*/ new InvoiceEntry("hdmi cable", BigDecimal.valueOf(5.00).setScale(2), 250.00, 20.00, Vat.VAT_8, null)
        )

        return List.of(
                Invoice.builder()
                        .date(LocalDate.of(2000, 11, 23))
                        .number("2021/05/09/1289")
                        .buyer(sampleCompaniesList.get(0))
                        .seller(sampleCompaniesList.get(1))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(1), invoiceEntries.get(3), invoiceEntries.get(9), invoiceEntries.get(10),
                                invoiceEntries.get(26), invoiceEntries.get(20), invoiceEntries.get(17))
                        )
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2005, 10, 25))
                        .number("2021/05/09/5675")
                        .buyer(sampleCompaniesList.get(2))
                        .seller(sampleCompaniesList.get(1))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(15), invoiceEntries.get(4), invoiceEntries.get(18), invoiceEntries.get(12),
                                invoiceEntries.get(11), invoiceEntries.get(22)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2009, 1, 22))
                        .number("2021/05/09/3333")
                        .buyer(sampleCompaniesList.get(3))
                        .seller(sampleCompaniesList.get(0))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(21), invoiceEntries.get(23), invoiceEntries.get(24), invoiceEntries.get(25),
                                invoiceEntries.get(27), invoiceEntries.get(30)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2010, 7, 22))
                        .number("2021/05/09/5555")
                        .buyer(sampleCompaniesList.get(1))
                        .seller(sampleCompaniesList.get(0))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(31), invoiceEntries.get(33), invoiceEntries.get(10)))
                        .build(),

                Invoice.builder()
                        .date(LocalDate.of(2010, 2, 22))
                        .number("2021/05/09/2222")
                        .buyer(sampleCompaniesList.get(3))
                        .seller(sampleCompaniesList.get(2))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(2), invoiceEntries.get(5), invoiceEntries.get(6), invoiceEntries.get(7),
                                invoiceEntries.get(8), invoiceEntries.get(13)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2010, 2, 22))
                        .number("2021/05/09/1111")
                        .buyer(sampleCompaniesList.get(3))
                        .seller(sampleCompaniesList.get(4))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(14), invoiceEntries.get(16), invoiceEntries.get(28)))
                        .build(),

                Invoice.builder()
                        .date(LocalDate.of(2010, 2, 22))
                        .number("2021/05/09/9864")
                        .buyer(sampleCompaniesList.get(5))
                        .seller(sampleCompaniesList.get(3))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(29), invoiceEntries.get(19)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2012, 2, 23))
                        .number("2021/05/09/3278")
                        .buyer(sampleCompaniesList.get(5))
                        .seller(sampleCompaniesList.get(6))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(31)))
                        .build(),
                Invoice.builder()
                        .date(LocalDate.of(2012, 2, 23))
                        .number("2021/05/09/3455")
                        .buyer(sampleCompaniesList.get(6))
                        .seller(sampleCompaniesList.get(5))
                        .invoiceEntries(List.of(
                                invoiceEntries.get(32)))
                        .build()
        )
    }
}
