package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@AllArgsConstructor
@Service
public class TaxCalculatorService {

    private final Database database;


    private BigDecimal income(String taxIdentificationNumber) {
        return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
    }

    private BigDecimal costs(String taxIdentificationNumber) {
        return database.visit(buyerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
    }

    private BigDecimal incomingVat(String taxIdentificationNumber) {
        return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    private BigDecimal outgoingVat(String taxIdentificationNumber) {
        return database.visit(buyerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    private Predicate<Invoice> sellerPredicate(String taxIdentificationNumber) {
        return invoice -> invoice.getSeller().getTaxIdentificationNumber().equals(taxIdentificationNumber);
    }

    private Predicate<Invoice> buyerPredicate(String taxIdentificationNumber) {
        return invoice -> invoice.getBuyer().getTaxIdentificationNumber().equals(taxIdentificationNumber);
    }

    public TaxCalculatorResult calculateTaxes(String taxIdentificationNumber) {
        BigDecimal income = income(taxIdentificationNumber);
        BigDecimal costs = costs(taxIdentificationNumber);
        BigDecimal incomingVat = incomingVat(taxIdentificationNumber);
        BigDecimal outgoingVat = outgoingVat(taxIdentificationNumber);
        BigDecimal earnings = income.subtract(costs);
        BigDecimal vatToReturn = incomingVat.subtract(outgoingVat);

        return TaxCalculatorResult.builder()
            .income(income)
            .costs(costs)
            .earnings(earnings)
            .incomingVat(incomingVat)
            .outgoingVat(outgoingVat)
            .vatToReturn(vatToReturn)
            .build();
    }
}
