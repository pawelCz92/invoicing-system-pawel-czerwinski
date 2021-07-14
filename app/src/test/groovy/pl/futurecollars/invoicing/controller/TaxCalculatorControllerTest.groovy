package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.TaxCalculatorResult
import spock.lang.Specification
import spock.lang.Stepwise

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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

    private static String currentDir
    private static Path idFilePath
    private static Path dataFilePath

    def setupSpec() {
        currentDir = Paths.get("").toAbsolutePath().toString()
        idFilePath = Path.of(currentDir, "db", "db-ids.json")
        dataFilePath = Path.of(currentDir, "db", "db-data.json")

        Files.createDirectory(dataFilePath.getParent())
        Files.createFile(idFilePath)
        Files.createFile(dataFilePath)
    }

    def cleanupSpec() {
        Files.deleteIfExists(idFilePath)
        Files.deleteIfExists(dataFilePath)
        Files.delete(idFilePath.getParent())
    }

    def "should return TaxCalculatorResult with values 0 for not existing TIN"() {
        setup:
        String taxIdentificationNumber = "000-00-0000"
        TaxCalculatorResult blankTaskCalculatorResult = TaxCalculatorResult.builder()
                .income(BigDecimal.ZERO)
                .costs(BigDecimal.ZERO)
                .earnings(BigDecimal.ZERO)
                .incomingVat(BigDecimal.ZERO)
                .outgoingVat(BigDecimal.ZERO)
                .vatToReturn(BigDecimal.ZERO)
                .build()

        when:
        def response = mockMvc.perform(get(COLLECTION + taxIdentificationNumber))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == jsonService.objectToString(blankTaskCalculatorResult)
    }

    def addInvoicesToBase() {
        setup:
        TestHelpers.getSampleInvoicesList()
                .forEach(invoice -> mockMvc.perform(post("/invoices/").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonService.objectToString(invoice))).andExpect(status().isOk()))
    }

    def "should return object with calculated taxes for existing TIN"() {
        given:
        TaxCalculatorResult expectedResult = TaxCalculatorResult.builder()
                .income(income)
                .costs(costs)
                .incomingVat(incomingVat)
                .outgoingVat(outgoingVat)
                .earnings(earnings)
                .vatToReturn(vatToReturn)
                .build()

        String expectedResultAsJson = jsonService.objectToString(expectedResult)

        when:
        String actual = mockMvc.perform(get(COLLECTION + taxIdentificationNumber))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        actual == expectedResultAsJson

        where:
        taxIdentificationNumber | income | costs  | incomingVat | outgoingVat | earnings | vatToReturn
        "382-22-1584"           | 4620.0 | 4790.0 | 109.0       | 178.0       | -170.0   | -69.0
        "677-31-4788"           | 9410.0 | 4620.0 | 287.0       | 109.0       | 4790.0   | 178.0
        "161-65-1354"           | 0      | 4620.0 | 0           | 109.0       | -4620.0  | -109.0
    }
}
