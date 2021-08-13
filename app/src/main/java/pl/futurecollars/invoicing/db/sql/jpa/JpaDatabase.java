package pl.futurecollars.invoicing.db.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@RequiredArgsConstructor
@Slf4j
public class JpaDatabase implements Database {

    private final InvoiceRepository invoiceRepository;

    @Override
    public int save(Invoice invoice) {
        return invoiceRepository.save(invoice).getId();
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return StreamSupport
            .stream(invoiceRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public void update(int id, Invoice updatedInvoice) {
        Optional<Invoice> invoiceOpt = getById(id);

        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            updatedInvoice.setId(id);
            updatedInvoice.getBuyer().setId(invoice.getBuyer().getId());
            updatedInvoice.getSeller().setId(invoice.getSeller().getId());
            invoiceRepository.save(updatedInvoice);
        } else {
            String message = "Id: " + id + " not found. Update impossible";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void delete(int id) {
        Optional<Invoice> invoice = getById(id);
        invoice.ifPresentOrElse(invoiceRepository::delete, () -> {
            String message = "There is no such id for delete invoice";
            log.error(message);
            throw new IllegalArgumentException(message);
        });
    }

    @Override
    public void deleteAll() {
        invoiceRepository.deleteAll(getAll());
    }
}
