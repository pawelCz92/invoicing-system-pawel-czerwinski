package pl.futurecollars.invoicing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.InMemoryDataBase;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;
import pl.futurecollars.invoicing.service.InvoiceService;

public class App {

    public static void main(String[] args) {
        runExampleOfInMemoryDatabase();
    }

    private static void runExampleOfInMemoryDatabase() {
        Database database = new InMemoryDataBase();
        InvoiceService invoiceService = new InvoiceService(database);

        Company buyer = Company.builder()
            .taxIdentificationNumber("645455465")
            .address("ul Błotna, nr zachlapany")
            .name("Biedronka")
            .build();

        Company seller = Company.builder()
            .taxIdentificationNumber("43534534534")
            .address("ul Jakas, Poznan")
            .name("Wiesław Paleta")
            .build();

        List<InvoiceEntry> products = List.of(
            new InvoiceEntry("Programming course", BigDecimal.valueOf(10000),
                BigDecimal.valueOf(2300), Vat.VAT_23),
            new InvoiceEntry("English course", BigDecimal.valueOf(200),
                BigDecimal.valueOf(46), Vat.VAT_23));

        Invoice invoice = new Invoice(LocalDate.now(), buyer, seller, products);

        int id = invoiceService.save(invoice);

        invoiceService.getById(id).ifPresent(System.out::println);

        invoice.setDate(LocalDate.now().minusDays(5));

        invoiceService.getById(id).ifPresent(invoice1 -> invoiceService.update(id, invoice));

        System.out.println(invoiceService.getAll());

        invoiceService.delete(id);
    }

}
