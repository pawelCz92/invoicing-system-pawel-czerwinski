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

    @Override
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            return ResponseEntity.ok().body(invoiceService.getAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Integer> saveInvoice(@RequestBody Invoice invoice) {
        try {
            return ResponseEntity.ok(invoiceService.save(invoice));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
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
    public ResponseEntity<?> updateById(@PathVariable int id, @RequestBody Invoice invoice) {
        try {
            invoiceService.update(id, invoice);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        try {
            invoiceService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
