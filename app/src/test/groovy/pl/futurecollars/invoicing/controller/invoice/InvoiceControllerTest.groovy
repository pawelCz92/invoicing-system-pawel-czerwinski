package pl.futurecollars.invoicing.controller.invoice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification
import spock.lang.Stepwise

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

    def "should return empty string if there is no invoices in base"() {
        when:
        def response = mockMvc.perform(get(COLLECTION))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "[]"
    }

    def "should return notFound response when try to get invoice by not existing id"() {
        expect:
        mockMvc.perform(get(COLLECTION + "1"))
                .andExpect(status().isNotFound())
    }

    def "should save invoice to base"() {
        given:
        Invoice invoice = TestHelpers.getSampleInvoicesList().get(0)

        when:
        def invoiceId = mockMvc.perform(
                post(COLLECTION).content(jsonService.objectToString(invoice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        invoiceId == "1"
    }

    def "should return invoice by existing id"() {
        given:
        Invoice invoice = TestHelpers.getSampleInvoicesList().get(0)
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
        mockMvc.perform(put(COLLECTION + "99999").content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
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
        mockMvc.perform(put(COLLECTION + "1").content(updatedInvoiceAsString)
                .contentType(MediaType.APPLICATION_JSON))
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
        def invoicesAfterDelete = mockMvc.perform(get(COLLECTION + "1"))
                .andExpect(status().isNotFound())
                .andReturn()
                .response
                .contentAsString

        then:
        invoicesAfterDelete == ""
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
                .content(jsonService.objectToString(invoiceA))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        String idInvoiceB = mockMvc.perform(post(COLLECTION)
                .content(jsonService.objectToString(invoiceB))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        mockMvc.perform(post(COLLECTION)
                .content(jsonService.objectToString(invoiceC))
                .contentType(MediaType.APPLICATION_JSON))
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
