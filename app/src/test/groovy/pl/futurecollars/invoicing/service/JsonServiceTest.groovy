package pl.futurecollars.invoicing.service


import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

import java.time.LocalDate

class JsonServiceTest extends Specification {

    private JsonService jsonService = new JsonService()
    private static String jsonSample
    def static buyer
    def static seller
    def static invoiceEntries
    def static invoice

    def setupSpec() {
        buyer = new Company("445656", "ul. Inna, Warszawa", "RTVG S A")
        seller = new Company("964849", "ul. Obok, Warszawa", "Avgm-com S A")

        invoiceEntries = List.of(
                new InvoiceEntry("tv", 1, 1000, 230, Vat.VAT_23),
                new InvoiceEntry("Radio", 1, 100, 23, Vat.VAT_23)
        )

        invoice = new Invoice(LocalDate.of(2020, 1, 20), buyer, seller, invoiceEntries)
        jsonSample = "{\"id\":0,\"date\":\"2020-01-20\",\"buyer\":{\"taxIdentificationNumber\":\"445656\",\"add" +
                "ress\":\"ul. Inna, Warszawa\",\"name\":\"RTVG S A\"},\"seller\":{\"taxIdentifica" +
                "tionNumber\":\"964849\",\"address\":\"ul. Obok, Warszawa\",\"name\":\"Avgm-com S A\"},\"invoi" +
                "ceEntries\":[{\"description\":\"tv\",\"quantity\":1,\"price\":1000,\"vatValue\":230,\"vatR" +
                "ate\":\"VAT_23\",\"car\":null},{\"description\":\"Radio\",\"quantity\":1,\"price\":100,\"vatV" +
                "alue\":23,\"vatRate\":\"VAT_23\",\"car\":null}]}"
    }

    def "should convert object to string"() {
        when:
        String resultForInvoice = jsonService.objectToString(invoice)
        String resultForBuyer = jsonService.objectToString(buyer)

        then:
        verifyAll {
            resultForInvoice == jsonSample
            resultForBuyer == "{\"taxIdentificationNumber\":\"445656\",\"" +
                    "address\":\"ul. Inna, Warszawa\",\"name\":\"RTVG S A\"}"
        }

    }

    def "should convert string to object"() {
        when:
        Company resultCompany = jsonService.stringToObject(("{\"taxIdentificationNumber\":\"445656\",\"" +
                "address\":\"ul. Inna, Warszawa\",\"name\":\"RTVG S A\"}"), Company.class)
        Invoice resultInvoice = jsonService.stringToObject(jsonSample, Invoice.class)

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