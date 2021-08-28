package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final Database<Invoice> database;

    public Long save(Invoice invoice) {
        invoice.setId(null);
        invoice.getBuyer().setId(null);
        invoice.getSeller().setId(null);
        return database.save(invoice);
    }

    public Optional<Invoice> getById(Long id) {
        return database.getById(id);
    }

    public List<Invoice> getAll() {
        return database.getAll();
    }

    public void update(Long id, Invoice updatedInvoice) throws IllegalArgumentException {
        database.update(id, updatedInvoice);
    }

    public void delete(Long id) {
        database.delete(id);
    }
}
