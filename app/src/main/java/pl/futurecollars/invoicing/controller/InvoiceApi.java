package pl.futurecollars.invoicing.controller;

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
    ResponseEntity<Integer> saveInvoice(@RequestBody String invoiceInJson);

    @ApiOperation(value = "Get invoice by id")
    @GetMapping("/{id}")
    ResponseEntity<Invoice> getInvoiceById(@PathVariable int id);

    @ApiOperation(value = "Update invoice with given id")
    @PutMapping("/{id}")
    ResponseEntity<?> updateById(@PathVariable int id, @RequestBody String invoiceString);

    @ApiOperation(value = "Delete invoice with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable int id);
}
