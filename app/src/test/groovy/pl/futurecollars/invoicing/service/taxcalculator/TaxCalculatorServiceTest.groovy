package pl.futurecollars.invoicing.service.taxcalculator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", values = ["file", "sql", "memory"])
class TaxCalculatorServiceTest extends Specification {

    @Autowired
    private Database database
    @Autowired
    private TaxCalculatorService taxCalculatorService
    private static List<Invoice> sampleInvoices


    def saveSampleInvoicesToBase() {
        sampleInvoices = TestHelpers.getSampleInvoicesList()
        sampleInvoices.forEach({ invoice -> database.save(invoice) })
    }

    def "should calculate taxes"() {
        setup:
        database.deleteAll()
        saveSampleInvoicesToBase()
        Company company = new Company(tin, "any address x", "any name",
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
        TaxCalculatorResult result = taxCalculatorService.calculateTaxes(company)

        then:
        result == expectedResult

        where:
        tin << ["100-16-0000", "803-36-7695"]
        income << [76011.62, 520.00]
        costs << [11329.47, 28523.56]
        earnings << [64682.15, -28003.56]
        incomingVat << [0.00, 119.60]
        outgoingVat << [0.00, 1922.61]
        vatToReturn << [0.00, -1803.01]
        earningsMinusPensionInsurance << [64167.58, -28518.13]
        taxCalculationBase << [64168, -28518]
        incomeTax << [12191.92, -5418.42]
        pensionInsurance << [514.57, 514.57]
        healthInsurance << [319.94, 319.94]
        reducedHealthInsurance << [275.5,275.5]
        incomeTaxMinusHealthInsurance << [11916.42, -5693.92]
        finalIncomeTax << [11916, -5694]
    }
}
