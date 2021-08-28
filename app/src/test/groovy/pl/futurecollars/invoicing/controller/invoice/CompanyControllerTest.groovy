package pl.futurecollars.invoicing.controller.invoice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class CompanyControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc
    @Autowired
    private JsonService jsonService
    private static String COLLECTION = "/companies/"
    private static lastId = -1

    def clearBase() {
        setup:
        String addingResponse = mockMvc.perform(get(COLLECTION))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        List<Company> companies = jsonService.stringToObject(addingResponse, List.class)

        if (companies.size() > 0) {
            companies.forEach({ comp ->
                mockMvc.perform(delete(COLLECTION + comp.id)).andExpect(status().isNoContent())
            }
            )
        }
    }

    def "should return empty string if there is no companies in base"() {
        when:
        def response = mockMvc.perform(get(COLLECTION))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "[]"
    }

    def "should return notFound response when try to get company by not existing id"() {
        expect:
        mockMvc.perform(get(COLLECTION + "1"))
                .andExpect(status().isNotFound())
    }

    def "should save company to base"() {
        given:
        Company company = TestHelpers.getSampleInvoicesList().get(0).getSeller()

        when:
        lastId = Integer.parseInt(
                mockMvc.perform(
                        post(COLLECTION).content(jsonService.objectToString(company))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString)

        then:
        lastId > 0
    }

    def "should return company by existing id"() {
        given:
        Company company = TestHelpers.getSampleInvoicesList().get(0).getSeller()
        company.setId(lastId)

        when:
        def responseAsJson = mockMvc.perform(get(COLLECTION + lastId))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        jsonService.stringToObject(responseAsJson, Company.class) == company
    }

    def "should return notFound response when try to update company by not existing id"() {
        given:
        Company company = TestHelpers.sampleInvoicesList.get(0).getBuyer()
        company.setId(1)
        String invoiceAsJson = jsonService.objectToString(company)

        expect:
        mockMvc.perform(put(COLLECTION + "99999").content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
    }

    def "should update company by existing id"() {
        given:
        Company company = Company.copyOf(TestHelpers.getSampleCompaniesList().get(0), true)
        company.setId(lastId)

        Company updatedCompany = Company.copyOf(TestHelpers.getSampleCompaniesList().get(1), true)
        updatedCompany.setId(lastId)
        String updatedCompanyAsString = jsonService.objectToString(updatedCompany)

        when:
        def companiesNotEquals = updatedCompany != company

        then:
        companiesNotEquals

        when:
        mockMvc.perform(put(COLLECTION + lastId).content(updatedCompanyAsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())

        and:
        def updatedCompanyFromBase = mockMvc.perform(get(COLLECTION + lastId))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedCompany == jsonService.stringToObject(updatedCompanyFromBase, Company.class)
    }

    def "should delete company by id"() {
        when:
        mockMvc.perform(delete(COLLECTION + lastId))
                .andExpect(status().isNoContent())

        and:
        def invoicesAfterDelete = mockMvc.perform(get(COLLECTION + lastId))
                .andExpect(status().isNotFound())
                .andReturn()
                .response
                .contentAsString

        then:
        invoicesAfterDelete == ""
    }

    def "should return notFound response when try to delete company by using not existing id"() {
        expect:
        mockMvc.perform(delete(COLLECTION + "99999"))
                .andExpect(status().isNotFound())
    }

    def "should delete one out of few companies"() {
        setup:
        Company companyA = TestHelpers.sampleInvoicesList.get(0).getSeller()
        Company companyB = TestHelpers.sampleInvoicesList.get(1).getSeller()
        Company companyC = TestHelpers.sampleInvoicesList.get(2).getSeller()

        and: "saving sample three invoices to base"
        mockMvc.perform(post(COLLECTION)
                .content(jsonService.objectToString(companyA))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        String idCompanyB = mockMvc.perform(post(COLLECTION)
                .content(jsonService.objectToString(companyB))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        lastId = Integer.parseInt(
                mockMvc.perform(post(COLLECTION)
                        .content(jsonService.objectToString(companyC))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString
        )

        when:
        mockMvc.perform(delete((COLLECTION + idCompanyB)))
                .andExpect(status().isNoContent())

        and:
        String response = mockMvc.perform(get(COLLECTION))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        jsonService.stringToObject(response, List.class).size() == 2
    }
}
