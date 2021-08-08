package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDataBase implements Database {

    private final HashMap<Integer, Invoice> invoiceInMemoryDatabase = new HashMap<>();
    private int index = 1;

    @Override
    public int save(Invoice invoice) {
        invoice.setId(index);
        invoiceInMemoryDatabase.put(index, invoice);
        return index++;
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return Optional.ofNullable(invoiceInMemoryDatabase.get(id));
    }

    @Override
    public List<Invoice> getAll() {
        return new ArrayList<>(invoiceInMemoryDatabase.values());
    }

    @Override
    public void update(int id, Invoice updatedInvoice) {
        if (!invoiceInMemoryDatabase.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
        updatedInvoice.setId(id);
        invoiceInMemoryDatabase.put(id, updatedInvoice);
    }

    @Override
    public void delete(int id) {
        if (!invoiceInMemoryDatabase.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
        invoiceInMemoryDatabase.remove(id);
    }
}
