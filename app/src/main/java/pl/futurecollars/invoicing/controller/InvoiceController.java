package pl.futurecollars.invoicing.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;
import pl.futurecollars.invoicing.service.JsonService;

@RestController
@AllArgsConstructor
public class InvoiceController implements InvoiceApi {

    private final InvoiceService invoiceService;
    private final JsonService jsonService;

    @Override
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            return ResponseEntity.ok().body(invoiceService.getAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<Integer> saveInvoice(@RequestBody String invoiceInJson) {
        try {
            return ResponseEntity.ok(invoiceService.save(jsonService.stringToObject(invoiceInJson, Invoice.class)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable int id) {
        try {
            return invoiceService.getById(id)
                .map(invoice -> ResponseEntity.ok().body(invoice))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody String invoiceString) {
        try {
            invoiceService.update(id, jsonService.stringToObject(invoiceString, Invoice.class));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        try {
            invoiceService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
