package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;

public class Invoice {

    private int id;
    private LocalDate date;
    private Company buyer;
    private Company seller;
    private List<InvoiceEntry> invoiceEntries;


}
