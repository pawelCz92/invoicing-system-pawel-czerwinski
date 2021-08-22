package pl.futurecollars.invoicing.controller.invoice;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.ItemService;

@RestController
@AllArgsConstructor
public class InvoiceController implements InvoiceApi {

    private final ItemService<Invoice> itemService;

    @Override
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok().body(itemService.getAll());
    }

    @Override
    public ResponseEntity<Long> saveInvoice(@RequestBody Invoice invoice) {
        try {
            return ResponseEntity.ok(itemService.save(invoice));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        try {
            return itemService.getById(id)
                .map(invoice -> ResponseEntity.ok().body(invoice))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody Invoice invoice) {
        try {
            itemService.update(id, invoice);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            itemService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
