package pl.futurecollars.invoicing.controller.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping("invoices")
@Api(tags = {"invoice-controller"})
public interface InvoiceApi {

    @ApiOperation(value = "Get list of all invoices")
    @GetMapping
    ResponseEntity<List<Invoice>> getAllInvoices();

    @ApiOperation(value = "Add new invoice to system")
    @PostMapping
    ResponseEntity<Long> saveInvoice(@RequestBody Invoice invoice);

    @ApiOperation(value = "Get invoice by id")
    @GetMapping("/{id}")
    ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id);

    @ApiOperation(value = "Update invoice with given id")
    @PutMapping("/{id}")
    ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody Invoice invoice);

    @ApiOperation(value = "Delete invoice with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id);
}
