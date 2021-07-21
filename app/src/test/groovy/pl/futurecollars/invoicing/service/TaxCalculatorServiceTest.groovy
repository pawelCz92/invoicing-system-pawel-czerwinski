package pl.futurecollars.invoicing.service


import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.FileBasedDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.file.FileService
import spock.lang.Ignore
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

    @Ignore
    def "should calculate taxes"() {
        given:
        TaxCalculatorResult expectedResult = TaxCalculatorResult.builder()
                .income(income)
                .costs(costs)
                .incomingVat(inVat)
                .outgoingVat(outVat)
                .earnings(earnings)
                .vatToReturn(vatToReturn)
                .build()

        when:
        TaxCalculatorResult actual = taxCalculatorService.calculateTaxes(tin)

        then:
        actual == expectedResult

        where:
        tin           | income | costs    | inVat   | outVat | earnings  | vatToReturn
        "382-22-1584" | 10548  | 4514.00  | 1906.14 | 970.32 | 6034.00   | 935.82
        "677-31-4788" | 10548  | 4514.00  | 1906.14 | 970.32 | 6034.00   | 935.82
        "100-16-1976" | 0      | 23583.56 | 0       | 980.81 | -23583.56 | -980.81
    }

    def "should calculate taxes new"() {
        given:
        TaxCalculatorResult expectedResultOne = TaxCalculatorResult.builder()
                .income(BigDecimal.valueOf(10548))
                .costs(BigDecimal.valueOf(4514))
                .earnings(BigDecimal.valueOf(6034))
                .incomingVat(BigDecimal.valueOf(1906.14))
                .outgoingVat(BigDecimal.valueOf(970.32))
                .vatToReturn(BigDecimal.valueOf(935.82))
                .earningsMinusPensionInsurance(BigDecimal.valueOf(0))
                .taxCalculationBase(BigDecimal.valueOf(0))
                .pensionInsurance(BigDecimal.valueOf(0))
                .healthInsurance(BigDecimal.valueOf(0))
                .finalIncomeTax(BigDecimal.valueOf(0))
                .build()

        TaxCalculatorResult expectedResultTwo = TaxCalculatorResult.builder()
                .income(BigDecimal.valueOf(0))
                .costs(BigDecimal.valueOf(4514))
                .earnings(BigDecimal.valueOf(6034))
                .incomingVat(BigDecimal.valueOf(1906.14))
                .outgoingVat(BigDecimal.valueOf(970.32))
                .vatToReturn(BigDecimal.valueOf(935.82))
                .earningsMinusPensionInsurance(BigDecimal.valueOf(0))
                .taxCalculationBase(BigDecimal.valueOf(0))
                .pensionInsurance(BigDecimal.valueOf(0))
                .healthInsurance(BigDecimal.valueOf(0))
                .finalIncomeTax(BigDecimal.valueOf(0))
                .build()

        TaxCalculatorResult expectedResultThree = TaxCalculatorResult.builder()
                .income(BigDecimal.valueOf(0))
                .costs(BigDecimal.valueOf(23583.56))
                .earnings(BigDecimal.valueOf(-23583.56))
                .incomingVat(BigDecimal.valueOf(BigInteger.ZERO))
                .outgoingVat(BigDecimal.valueOf(980.81))
                .vatToReturn(BigDecimal.valueOf(-980.81))
                .earningsMinusPensionInsurance(BigDecimal.valueOf(0))
                .taxCalculationBase(BigDecimal.valueOf(0))
                .pensionInsurance(BigDecimal.valueOf(0))
                .healthInsurance(BigDecimal.valueOf(0))
                .finalIncomeTax(BigDecimal.valueOf(0))
                .build()

        TaxCalculatorResult expectedResultFour = TaxCalculatorResult.builder()
                .income(BigDecimal.valueOf(76011.62))
                .costs(BigDecimal.valueOf(11329.47))
                .earnings(BigDecimal.valueOf(64682.15))
                .incomingVat(BigDecimal.valueOf(0))
                .outgoingVat(BigDecimal.valueOf(0))
                .vatToReturn(BigDecimal.valueOf(0))
                .earningsMinusPensionInsurance(BigDecimal.valueOf(64167.58))
                .taxCalculationBase(BigDecimal.valueOf(64168))
                .incomeTax(BigDecimal.valueOf(12191.92))
                .pensionInsurance(BigDecimal.valueOf(514.57))
                .healthInsurance(BigDecimal.valueOf(319.94))
                .reducedHealthInsurance(BigDecimal.valueOf(275.50))
                .incomeTaxMinusHealthInsurance(BigDecimal.valueOf(11916.42))
                .finalIncomeTax(BigDecimal.valueOf(11916))
                .build()

        Company company7 =
                new Company("100-16-0000", "67 Nangela", "Test",
                        BigDecimal.valueOf(319.94), BigDecimal.valueOf(514.57))

        when:
        TaxCalculatorResult resultOne = taxCalculatorService.calculateTaxes("382-22-1584")
        //  TaxCalculatorResult resultTwo = taxCalculatorService.calculateTaxes("677-31-4788")
        //   TaxCalculatorResult resultThree = taxCalculatorService.calculateTaxes("100-16-1976")
        TaxCalculatorResult resultFour = taxCalculatorService.calculateTaxes(company7)

        then:
        resultOne == expectedResultOne

        //  resultTwo == expectedResultTwo

        //   resultThree == expectedResultThree

        resultFour == expectedResultFour

    }
}
