package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDataBase implements Database<Invoice> {

    private final HashMap<Long, Invoice> invoiceInMemoryDatabase = new HashMap<>();
    private Long index = 1L;

    @Override
    public Long save(Invoice invoice) {
        invoice.setId(index);
        invoiceInMemoryDatabase.put(index, invoice);
        return index++;
    }

    @Override
    public Optional<Invoice> getById(Long id) {
        return Optional.ofNullable(invoiceInMemoryDatabase.get(id));
    }

    @Override
    public List<Invoice> getAll() {
        return new ArrayList<>(invoiceInMemoryDatabase.values());
    }

    @Override
    public void update(Long id, Invoice updatedInvoice) {
        if (!invoiceInMemoryDatabase.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
        updatedInvoice.setId(id);
        invoiceInMemoryDatabase.put(id, updatedInvoice);
    }

    @Override
    public void delete(Long id) {
        if (!invoiceInMemoryDatabase.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
        invoiceInMemoryDatabase.remove(id);
    }

    public void deleteAll() {
        invoiceInMemoryDatabase.clear();
    }
}
