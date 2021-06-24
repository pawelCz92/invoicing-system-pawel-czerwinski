package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class InvoiceEntry {

    private String description;
    private BigDecimal price;
    private BigDecimal vatValue;
    private Vat vatRate;

    public InvoiceEntry(String description, double price, double vatValue, Vat vatRate) {
        this.description = description;
        this.price = BigDecimal.valueOf(price);
        this.vatValue = BigDecimal.valueOf(vatValue);
        this.vatRate = vatRate;
    }
}
