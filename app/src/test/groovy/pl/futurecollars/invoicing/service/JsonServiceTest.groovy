package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.model.Car
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
        buyer =  new Company("382-22-1584", "377 Ohio Road Pulo", "Microsoft",
                BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))
        seller = new Company("677-31-4788", "ul. Dluga Warszawa", "JBL",
                BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))

        invoiceEntries = List.of(
                new InvoiceEntry("tv", 1, 1000, 230, Vat.VAT_23),
                new InvoiceEntry("Radio", 1, 100, 23, Vat.VAT_23, new Car(true, "HPEXE-3"))
        )

        invoice = new Invoice(LocalDate.of(2020, 1, 20), buyer, seller, invoiceEntries)
        jsonSample = "{\"id\":0,\"number\":null,\"date\":\"2020-01-20\",\"buyer\":{\"tax" +
                "IdentificationNumber\":\"382-22-1584\",\"address\":\"377 Ohio Road Pulo\",\"name\":\"Micro" +
                "soft\",\"healthInsurance\":319.94,\"pensionInsurance\":514.57},\"seller\":{\"tax" +
                "IdentificationNumber\":\"677-31-4788\",\"address\":\"ul. Dluga Warszawa\",\"name\":\"JBL\",\"health" +
                "Insurance\":319.94,\"pensionInsurance\":514.57},\"invoiceEntries\":[{\"de" +
                "scription\":\"tv\",\"quantity\":1,\"price\":1000,\"vatValue\":230,\"vatRate\":\"VAT_" +
                "23\",\"car\":null},{\"description\":\"Radio\",\"quantity\":1,\"price\":100,\"vatValue\":23,\"vat" +
                "Rate\":\"VAT_23\",\"car\":{\"registration\":\"HPEXE-3\",\"includingPrivateExpense\":true}}]}"
    }

    def "should convert object to string"() {
        when:
        String resultForInvoice = jsonService.objectToString(invoice)
        String resultForBuyer = jsonService.objectToString(buyer)

        then:
        verifyAll {
            resultForInvoice == jsonSample
            resultForBuyer == "{\"taxIdentificationNumber\":\"382-22-1584\",\"address\":\"377 Ohio" +
                    " Road Pulo\",\"name\":\"Microsoft\",\"healthInsurance\":319.94,\"pensionInsurance\":514.57}"
        }

    }

    def "should convert string to object"() {
        when:
        Company resultCompany = jsonService.stringToObject(("{\"taxIdentificationNumber\":\"382-22-1584\",\"addre" +
                "ss\":\"377 Ohio Road Pulo\",\"name\":\"Microsoft\",\"health" +
                "Insurance\":319.94,\"pensionInsurance\":514.57}"), Company.class)
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