package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;

public class InvoiceEntry {

    private String description;
    private BigDecimal price;
    private BigDecimal vatValue;
    private double vatRate;
}
