package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification
import spock.lang.Stepwise

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class InvoiceControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc
    @Autowired
    private JsonService jsonService
    private static String COLLECTION = "/invoices/"

    def cleanupSpec() {
        String currentDir = Paths.get("").toAbsolutePath().toString()
        Path idFilePath = Path.of(currentDir, "db", "db-ids.json")
        Path dataFilePath = Path.of(currentDir, "db", "db-data.json")

        Files.deleteIfExists(idFilePath)
        Files.deleteIfExists(dataFilePath)
        Files.delete(idFilePath.getParent())
    }

    def "should return not found status when try to get all invoices and db file was not created yet"() {
        expect:
        def response = mockMvc.perform(get(COLLECTION))
                .andExpect(status().isNotFound())
    }

    def "should return notFound response when try to get invoice by not existing id"() {
        expect:
        mockMvc.perform(get(COLLECTION + "1"))
                .andExpect(status().isNotFound())
    }

    def "should save invoice to base"() {
        given:
        Invoice invoice = TestHelpers.sampleInvoicesList.get(0)

        when:
        def invoiceId = mockMvc.perform(
                post(COLLECTION).content(jsonService.objectToString(invoice)))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        invoiceId == "1"
    }

    def "should return invoice by existing id"() {
        given:
        Invoice invoice = TestHelpers.sampleInvoicesList.get(0)
        invoice.setId(1)
        String invoiceAsJson = jsonService.objectToString(invoice)

        when:
        def responseAsJson = mockMvc.perform(get(COLLECTION + "1"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        responseAsJson == invoiceAsJson
    }

    def "should return notFound response when try to update invoice by not existing id"() {
        given:
        Invoice invoice = TestHelpers.sampleInvoicesList.get(0)
        invoice.setId(1)
        String invoiceAsJson = jsonService.objectToString(invoice)

        expect:
        mockMvc.perform(put(COLLECTION + "99999").content(invoiceAsJson))
                .andExpect(status().isNotFound())
    }

    def "should update invoice by existing id"() {
        given:
        Invoice invoice = TestHelpers.sampleInvoicesList.get(0)
        invoice.setId(1)
        Invoice updatedInvoice = TestHelpers.sampleInvoicesList.get(1)
        updatedInvoice.setId(1)
        String updatedInvoiceAsString = jsonService.objectToString(updatedInvoice)

        when:
        def invoicesNotEquals = invoice != updatedInvoice

        then:
        invoicesNotEquals

        when:
        mockMvc.perform(put(COLLECTION + "1").content(updatedInvoiceAsString))
                .andExpect(status().isNoContent())

        and:
        def updatedInvoiceFromBase = mockMvc.perform(get(COLLECTION + "1"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedInvoiceAsString == updatedInvoiceFromBase
    }

    def "should delete invoice by id"() {
        when:
        mockMvc.perform(delete(COLLECTION + "1"))
                .andExpect(status().isNoContent())

        and:
        def invoicesAfterDelete = mockMvc.perform(get(COLLECTION))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        invoicesAfterDelete == "[]"
    }

    def "should return notFound response when try to delete invoice by using not existing id"() {
        expect:
        mockMvc.perform(delete(COLLECTION + "999"))
                .andExpect(status().isNotFound())
    }

    def "should delete one out of few invoices"() {
        setup:
        Invoice invoiceA = TestHelpers.sampleInvoicesList.get(0)
        Invoice invoiceB = TestHelpers.sampleInvoicesList.get(1)
        Invoice invoiceC = TestHelpers.sampleInvoicesList.get(2)

        and: "saving sample three invoices to base"
        mockMvc.perform(post(COLLECTION)
                .content(jsonService.objectToString(invoiceA)))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        String idInvoiceB = mockMvc.perform(post(COLLECTION)
                .content(jsonService.objectToString(invoiceB)))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        mockMvc.perform(post(COLLECTION)
                .content(jsonService.objectToString(invoiceC)))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        when:
        mockMvc.perform(delete((COLLECTION + idInvoiceB)))
                .andExpect(status().isNoContent())

        and:
        String response = mockMvc.perform(get(COLLECTION))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        jsonService.stringToObject(response, List<String>.class).size() == 2
    }
}
