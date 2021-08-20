package pl.futurecollars.invoicing.db.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@RequiredArgsConstructor
@Slf4j
public class JpaDatabase implements Database {

    private final InvoiceRepository invoiceRepository;

    @Transactional
    @Override
    public Long save(Invoice invoice) {
        invoice.setId(null);
        invoice.getBuyer().setId(null);
        invoice.getSeller().setId(null);

//        invoice.setInvoiceEntries(invoice.getInvoiceEntries().stream().map(ie -> {
//            ie.setId(null);
//        return ie;
//        }).collect(Collectors.toList()));

        return invoiceRepository.save(invoice).getId();
    }

    @Override
    public Optional<Invoice> getById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return StreamSupport
            .stream(invoiceRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, Invoice updatedInvoice) {
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
    public void delete(Long id) {
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
