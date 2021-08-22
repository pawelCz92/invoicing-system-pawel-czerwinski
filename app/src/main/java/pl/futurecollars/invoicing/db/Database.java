package pl.futurecollars.invoicing.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

public interface Database {

    Long save(Invoice invoice);

    Optional<Invoice> getById(Long id);

    List<Invoice> getAll();

    void update(Long id, Invoice updatedInvoice);

    void delete(Long id);

    void deleteAll();

    default BigDecimal visit(Predicate<Invoice> filterRules, Function<InvoiceEntry, BigDecimal> amountToSelect) {
        return getAll().stream()
            .filter(filterRules)
            .flatMap(invoice -> invoice.getInvoiceEntries().stream())
            .map(amountToSelect)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
