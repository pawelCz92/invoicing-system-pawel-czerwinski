package pl.futurecollars.invoicing.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;
import pl.futurecollars.invoicing.service.JsonService;

@RestController()
@RequestMapping("invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final JsonService jsonService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, JsonService jsonService) {
        this.invoiceService = invoiceService;
        this.jsonService = jsonService;
    }

    @GetMapping()
    private List<Invoice> getAllInvoices() {
        return invoiceService.getAll();
    }

    @PostMapping()
    private int saveInvoice(@RequestBody String invoiceInJson) {
        return invoiceService.save(jsonService.stringToObject(invoiceInJson, Invoice.class));
    }

    @GetMapping("/{id}")
    private ResponseEntity<Invoice> getInvoiceById(@PathVariable int id) {
        return invoiceService.getById(id)
            .map(invoice -> ResponseEntity.ok().body(invoice))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateById(@PathVariable int id, @RequestBody String invoiceString) {
        try {
            invoiceService.update(id, jsonService.stringToObject(invoiceString, Invoice.class));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteById(@PathVariable int id) {
        try {
            invoiceService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
