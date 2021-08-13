package pl.futurecollars.invoicing.controller.tax

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.taxcalculator.TaxCalculatorResult
import spock.lang.Specification
import spock.lang.Stepwise

import java.nio.file.Path

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc
    @Autowired
    private JsonService jsonService
    private static String COLLECTION = "/tax/"

    def "should return TaxCalculatorResult with values 0 for not existing TIN"() {
        setup:
        Company company = new Company("000-00-0000", "any address", "any name",
                BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))

        when:
        def response = jsonService.stringToObject(
                (mockMvc.perform(post(COLLECTION)
                        .content(jsonService.objectToString(company))
                        .contentType(MediaType.APPLICATION_JSON))
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
                .forEach(invoice -> mockMvc.perform(post("/invoices/").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.objectToString(invoice))).andExpect(status().isOk()))
    }

    def "should return object with calculated taxes for existing TIN"() {
        given:
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
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString, TaxCalculatorResult.class)

        then:
        result == expectedResult

        where:
        tin << ["100-16-0000"]
        income << [76011.62]
        costs << [11329.47]
        earnings << [64682.15]
        incomingVat << [0.00]
        outgoingVat << [0.00]
        vatToReturn << [0.00]
        earningsMinusPensionInsurance << [64167.58]
        taxCalculationBase << [64168]
        incomeTax << [12191.92]
        pensionInsurance << [514.57]
        healthInsurance << [319.94]
        reducedHealthInsurance << [275.5]
        incomeTaxMinusHealthInsurance << [11916.42]
        finalIncomeTax << [11916]
    }
}
