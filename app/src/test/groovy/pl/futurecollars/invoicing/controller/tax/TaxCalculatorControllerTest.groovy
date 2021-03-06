package pl.futurecollars.invoicing.controller.tax

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.taxcalculator.TaxCalculatorResult
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc
    @Autowired
    private JsonService jsonService
    @Autowired
    Database<Invoice> database
    private static String COLLECTION = "/tax/"


    def setup() {
        database.reset()

        assert database.getAll().isEmpty()
    }

    def "should return TaxCalculatorResult with values 0 for not existing TIN"() {
        setup:
        Company company = new Company("000-00-0000", "any address", "any name",
                BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))

        when:
        def response = jsonService.stringToObject(
                (mockMvc.perform(post(COLLECTION)
                        .content(jsonService.objectToString(company))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString), TaxCalculatorResult.class)

        then:
        response.income == 0
        response.costs == 0
        response.earnings == 0
        response.incomingVat == 0
        response.outgoingVat == 0
        response.vatToReturn == 0

    }

    def addInvoicesToBase() {
        setup:
        TestHelpers.getSampleInvoicesList()
                .forEach({ invoice ->
                    mockMvc.perform(post("/invoices/").contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
                            .content(jsonService.objectToString(invoice))).andExpect(status().isOk())
                })
    }

    def "should return object with calculated taxes for existing TIN"() {
        setup:
        List.of(
                TestHelpers.getSampleInvoicesList().get(0),
                TestHelpers.getSampleInvoicesList().get(4),
                TestHelpers.getSampleInvoicesList().get(8))
                .stream()
                .map({
                    inv ->
                        inv.setId(null)
                        inv.getBuyer().setId(null)
                        inv.getSeller().setId(null)
                        return inv
                })
                .forEach({ inv -> database.save(inv as Invoice) })

        Company company = new Company(tin, "any address", "any name",
                BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))

        TaxCalculatorResult expectedResult = TaxCalculatorResult.builder()
                .income(income)
                .costs(costs)
                .earnings(earnings)
                .incomingVat(incomingVat)
                .outgoingVat(outgoingVat)
                .vatToReturn(vatToReturn)
                .earningsMinusPensionInsurance(earningsMinusPensionInsurance)
                .taxCalculationBase(taxCalculationBase)
                .incomeTax(incomeTax)
                .pensionInsurance(pensionInsurance)
                .healthInsurance(healthInsurance)
                .reducedHealthInsurance(reducedHealthInsurance)
                .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthInsurance)
                .finalIncomeTax(finalIncomeTax)
                .build()

        when:
        TaxCalculatorResult result = jsonService.stringToObject(mockMvc.perform(
                post(COLLECTION)
                        .content(jsonService.objectToString(company))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString, TaxCalculatorResult.class)

        then:
        result == expectedResult

        where:
        tin << ["100-16-0000"]
        income << [0]
        costs << [11329.47]
        earnings << [-11329.47]
        incomingVat << [0]
        outgoingVat << [0.00]
        vatToReturn << [0.00]
        earningsMinusPensionInsurance << [-11844.04]
        taxCalculationBase << [-11844]
        incomeTax << [-2250.36]
        pensionInsurance << [514.57]
        healthInsurance << [319.94]
        reducedHealthInsurance << [275.5]
        incomeTaxMinusHealthInsurance << [-2525.86]
        finalIncomeTax << [-2526]
    }
}
