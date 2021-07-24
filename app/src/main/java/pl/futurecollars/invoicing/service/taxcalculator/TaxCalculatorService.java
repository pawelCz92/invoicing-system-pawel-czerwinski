package pl.futurecollars.invoicing.service.taxcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@RequiredArgsConstructor
@Service
public class TaxCalculatorService {

    private static final BigDecimal HEALTH_INSURANCE_PERCENT = BigDecimal.valueOf(9);
    private static final BigDecimal MIN_PERCENT_TO_REDUCE_HEALTH_INSURANCE = BigDecimal.valueOf(7.75);
    private static final BigDecimal PERCENT_TO_CALCULATE_INCOME_TAX = BigDecimal.valueOf(19);
    private final Database database;

    private Predicate<Invoice> sellerPredicate(Company company) {
        return invoice -> invoice.getSeller().getTaxIdentificationNumber().equals(company.getTaxIdentificationNumber());
    }

    private Predicate<Invoice> buyerPredicate(Company company) {
        return invoice -> invoice.getBuyer().getTaxIdentificationNumber().equals(company.getTaxIdentificationNumber());
    }

    private BigDecimal income(Company company) {
        return database.visit(sellerPredicate(company), InvoiceEntry::getPrice);
    }

    private BigDecimal costs(Company company) {
        return database.visit(buyerPredicate(company), this::getPriceIncludingPersonalExpense);
    }

    private BigDecimal getPriceIncludingPersonalExpense(InvoiceEntry invoiceEntry) {
        return invoiceEntry.getPrice().add(invoiceEntry.getVatValue()).subtract(getVatValueIncludingPersonalExpense(invoiceEntry));
    }

    private BigDecimal incomingVat(Company company) {
        return database.visit(sellerPredicate(company), InvoiceEntry::getVatValue);
    }

    private BigDecimal outgoingVat(Company company) {
        return database.visit(buyerPredicate(company), this::getVatValueIncludingPersonalExpense);
    }

    private BigDecimal getVatValueIncludingPersonalExpense(InvoiceEntry invoiceEntry) {
        return Optional.ofNullable(invoiceEntry.getCar())
            .map(Car::isIncludingPrivateExpense)
            .map(privateExpense -> privateExpense ? BigDecimal.valueOf(0.5) : BigDecimal.ONE)
            .map(multiplier -> invoiceEntry.getVatValue().multiply(multiplier))
            .map(vatValue -> vatValue.setScale(2, RoundingMode.HALF_DOWN))
            .orElse(invoiceEntry.getVatValue());
    }

    public TaxCalculatorResult calculateTaxes(Company company) {
        BigDecimal income = income(company);
        BigDecimal costs = costs(company);
        BigDecimal earnings = income.subtract(costs);

        BigDecimal incomingVat = incomingVat(company);
        BigDecimal outgoingVat = outgoingVat(company);
        BigDecimal vatToReturn = incomingVat.subtract(outgoingVat);

        BigDecimal earningsMinusPensionInsurance = earnings.subtract(company.getPensionInsurance());
        BigDecimal taxCalculationBase = earningsMinusPensionInsurance.setScale(0, RoundingMode.HALF_UP);
        BigDecimal incomeTax = taxCalculationBase.multiply(PERCENT_TO_CALCULATE_INCOME_TAX.divide(BigDecimal.valueOf(100)));
        BigDecimal reducedHealthInsurance = reduceHealthInsurance(company.getHealthInsurance());
        BigDecimal incomeTaxMinusHealthInsurance = incomeTax.subtract(reducedHealthInsurance);
        BigDecimal finalIncomeTax = incomeTaxMinusHealthInsurance.setScale(0, RoundingMode.HALF_UP);

        return TaxCalculatorResult.builder()
            .income(income)
            .costs(costs)
            .earnings(earnings)
            .incomingVat(incomingVat)
            .outgoingVat(outgoingVat)
            .vatToReturn(vatToReturn)
            .earningsMinusPensionInsurance(earningsMinusPensionInsurance)
            .taxCalculationBase(taxCalculationBase)
            .incomeTax(incomeTax)
            .reducedHealthInsurance(reducedHealthInsurance)
            .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthInsurance)
            .pensionInsurance(company.getPensionInsurance())
            .healthInsurance(company.getHealthInsurance())
            .finalIncomeTax(finalIncomeTax)
            .build();
    }

    private BigDecimal reduceHealthInsurance(BigDecimal healthInsurance) {
        return healthInsurance
            .divide(HEALTH_INSURANCE_PERCENT, 3, RoundingMode.HALF_UP)
            .multiply(MIN_PERCENT_TO_REDUCE_HEALTH_INSURANCE)
            .setScale(1, RoundingMode.HALF_UP);
    }
}
