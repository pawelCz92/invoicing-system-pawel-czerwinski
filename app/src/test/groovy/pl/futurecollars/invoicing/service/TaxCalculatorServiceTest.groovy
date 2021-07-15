package pl.futurecollars.invoicing.service

import org.springframework.boot.test.context.SpringBootTest
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.FileBasedDatabase
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.file.FileService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

@SpringBootTest
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
        TaxCalculatorResult expectedResult = TaxCalculatorResult.builder()
                .income(income)
                .costs(costs)
                .incomingVat(incomingVat)
                .outgoingVat(outgoingVat)
                .earnings(earnings)
                .vatToReturn(vatToReturn)
                .build()

        when:
        TaxCalculatorResult actual = taxCalculatorService.calculateTaxes(taxIdentificationNumber)

        then:
        actual == expectedResult

        where:
        taxIdentificationNumber | income | costs  | incomingVat | outgoingVat | earnings | vatToReturn
        "382-22-1584"           | 4620.0 | 4790.0 | 109.0       | 178.0       | -170.0   | -69.0
        "677-31-4788"           | 9410.0 | 4620.0 | 287.0       | 109.0       | 4790.0   | 178.0
        "161-65-1354"           | 0      | 4620.0 | 0           | 109.0       | -4620.0  | -109.0
    }


}
