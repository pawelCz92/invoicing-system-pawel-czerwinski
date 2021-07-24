package pl.futurecollars.invoicing.service.taxcalculator

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.FileBasedDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.file.FileService
import pl.futurecollars.invoicing.service.file.IdProvider
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class TaxCalculatorServiceTest extends Specification {

    private static final Path DB_TEST_FILE_PATH = Path.of("testDBfile.json")
    private static final Path ID_TEST_FILE_PATH = Path.of("testIdsFile.json")
    private FileBasedDatabase fileBasedDatabase
    private IdProvider idProvider
    private FileService fileServiceForData
    private List<Invoice> sampleInvoices
    private TaxCalculatorService taxCalculatorService

    def setup() {
        idProvider = new IdProvider(ID_TEST_FILE_PATH)
        fileServiceForData = new FileService(DB_TEST_FILE_PATH)
        fileBasedDatabase = new FileBasedDatabase(fileServiceForData, idProvider, new JsonService())
        sampleInvoices = TestHelpers.getSampleInvoicesList()
        taxCalculatorService = new TaxCalculatorService(fileBasedDatabase)
        saveSampleInvoicesToBase()
    }

    def cleanup() {
        Files.deleteIfExists(DB_TEST_FILE_PATH)
        Files.deleteIfExists(ID_TEST_FILE_PATH)
    }

    def saveSampleInvoicesToBase() {
        sampleInvoices.forEach(invoice -> fileBasedDatabase.save(invoice))
    }

    def "should calculate taxes"() {
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
        TaxCalculatorResult result = taxCalculatorService.calculateTaxes(company)

        then:
        result == expectedResult

        where:
        tin << ["100-16-0000", "803-36-7695"]
        income << [76011.62, 22702.74]
        costs << [11329.47, 30591.56]
        earnings << [64682.15, -7888.82]
        incomingVat << [0, 1861.63]
        outgoingVat << [0, 1942.85]
        vatToReturn << [0, -81.22]
        earningsMinusPensionInsurance << [64167.58, -8403.39]
        taxCalculationBase << [64168, -8403]
        incomeTax << [12191.92, -1596.57]
        pensionInsurance << [514.57, 514.57]
        healthInsurance << [319.94, 319.94]
        reducedHealthInsurance << [275.5, 275.5]
        incomeTaxMinusHealthInsurance << [11916.42, -1872.07]
        finalIncomeTax << [11916, -1872]
    }
}
