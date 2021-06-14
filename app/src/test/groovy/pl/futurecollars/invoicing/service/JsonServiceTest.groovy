package pl.futurecollars.invoicing.service


import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class JsonServiceTest extends Specification {

    private JsonService jsonService

    def setup() {
        jsonService = new JsonService()
    }

    def "should convert object to string"() {
        given:
        Company buyer = Company.builder()
                .name("RTVG S A")
                .taxIdentificationNumber("445656")
                .address("ul. Inna, Warszawa")
                .build()
        Company seller = Company.builder()
                .name("Avgm-com S A")
                .taxIdentificationNumber("964849")
                .address("ul. Obok, Warszawa")
                .build()
        List<InvoiceEntry> invoiceEntries = List.of(
                new InvoiceEntry("TV", BigDecimal.valueOf(500), BigDecimal.ZERO, Vat.VAT_0),
                new InvoiceEntry("Radio", BigDecimal.valueOf(1000), BigDecimal.ZERO, Vat.VAT_0))

        Invoice invoice = new Invoice(LocalDate.of(2020, 1, 20), buyer, seller, invoiceEntries)

        when:
        String resultForInvoice = jsonService.objectToString(invoice)
        String resultForBuyer = jsonService.objectToString(buyer)

        then:
        verifyAll {
            resultForInvoice == "{\"id\":0,\"date\":\"2020-01-20\",\"buyer\":{\"" +
                    "taxIdentificationNumber\":\"445656\",\"address\":\"ul. Inna, Warszawa\",\"" +
                    "name\":\"RTVG S A\"},\"seller\":{\"taxIdentificationNumber\":\"964849\",\"" +
                    "address\":\"ul. Obok, Warszawa\",\"name\":\"Avgm-com S A\"},\"" +
                    "invoiceEntries\":[{\"description\":\"TV\",\"price\":500,\"vatValue\":0,\"vatRate\":\"VAT_0\"}," +
                    "{\"description\":\"Radio\",\"price\":1000,\"vatValue\":0,\"vatRate\":\"VAT_0\"}]}"
            resultForBuyer == "{\"taxIdentificationNumber\":\"445656\",\"" +
                    "address\":\"ul. Inna, Warszawa\",\"name\":\"RTVG S A\"}"
        }

    }

    def "should convert string to object"() {
        Company buyer = Company.builder()
                .name("RTVG S A")
                .taxIdentificationNumber("445656")
                .address("ul. Inna, Warszawa")
                .build()
        Company seller = Company.builder()
                .name("Avgm-com S A")
                .taxIdentificationNumber("964849")
                .address("ul. Obok, Warszawa")
                .build()
        List<InvoiceEntry> invoiceEntries = List.of(
                new InvoiceEntry("TV", BigDecimal.valueOf(500), BigDecimal.ZERO, Vat.VAT_0),
                new InvoiceEntry("Radio", BigDecimal.valueOf(1000), BigDecimal.ZERO, Vat.VAT_0))

        Invoice invoice = new Invoice(LocalDate.of(2020, 1, 20), buyer, seller, invoiceEntries)

        when:
        Company resultCompany = jsonService.stringToObject(("{\"taxIdentificationNumber\":\"445656\",\"" +
                "address\":\"ul. Inna, Warszawa\",\"name\":\"RTVG S A\"}"), Company.class)
        Invoice resultInvoice = jsonService.stringToObject(("{\"id\":0,\"date\":\"2020-01-20\",\"buyer\":{\"" +
                "taxIdentificationNumber\":\"445656\",\"address\":\"ul. Inna, Warszawa\",\"" +
                "name\":\"RTVG S A\"},\"seller\":{\"taxIdentificationNumber\":\"964849\",\"" +
                "address\":\"ul. Obok, Warszawa\",\"name\":\"Avgm-com S A\"},\"" +
                "invoiceEntries\":[{\"description\":\"TV\",\"price\":500,\"vatValue\":0,\"vatRate\":\"VAT_0\"}," +
                "{\"description\":\"Radio\",\"price\":1000,\"vatValue\":0,\"vatRate\":\"VAT_0\"}]}"), Invoice.class)

        then:
        verifyAll {
            resultCompany == buyer
            resultInvoice == invoice
        }
    }

    def "should return empty string if serialization from object to string fail"() {
        setup:
        List<?> list = new LinkedList<>()


        expect:
        jsonService.objectToString(new Object()) == ""
    }

    def "should return null if serialization to object fail"() {
        expect:
        jsonService.stringToObject("incorrect form", Company.class) == null
    }
}