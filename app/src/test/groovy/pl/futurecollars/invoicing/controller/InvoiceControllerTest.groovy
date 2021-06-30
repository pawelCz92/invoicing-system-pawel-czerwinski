package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
class InvoiceControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc


//    def "test"() {
//        expect:
//        mockMvc.perform(get("/invoices/get-all"))
//                .andDo(println())
//                .andExpect(status().isOk())
//    }
}
