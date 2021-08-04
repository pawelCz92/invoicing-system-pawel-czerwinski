package pl.futurecollars.invoicing.db.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@AllArgsConstructor
public class SqlDatabase implements Database {

    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Invoice invoice) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            long buyerId = insertCompany(invoice.getBuyer());
            long sellerId = insertCompany(invoice.getSeller());

            int invoiceId =  insertInvoice(keyHolder, invoice, buyerId, sellerId);


            invoice.getInvoiceEntries().forEach(entry -> {
                insertInvoiceEntry(keyHolder, entry);

                int invoiceEntryId = insertInvoiceEntry(keyHolder, entry);

                insertAssignationInvoiceEntryToInvoice(invoiceId, invoiceEntryId);
            });

        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int insertInvoice(GeneratedKeyHolder keyHolder, Invoice invoice, long buyerId, long sellerId) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO invoices (date, number, buyer, seller) VALUES (?, ?, ?, ?);", new String[] {"id"});
            ps.setDate(1, Date.valueOf(invoice.getDate()));
            ps.setString(2, invoice.getNumber());
            ps.setLong(3, buyerId);
            ps.setLong(4, sellerId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private int insertInvoiceEntry(GeneratedKeyHolder keyHolder, InvoiceEntry entry) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO invoice_entries (description, quantity, net_price, vat_value, vat_rate) VALUES (?, ?, ?, ?, ?);",
                new String[] {"id"});
            ps.setString(1, entry.getDescription());
            ps.setInt(2, entry.getQuantity());
            ps.setBigDecimal(3, entry.getPrice());
            ps.setBigDecimal(4, entry.getVatValue());
            ps.setInt(5, 1);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private void insertAssignationInvoiceEntryToInvoice(int invoiceId, int invoiceEntryId) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO invoices_invoice_entries VALUES (?, ?);");
            ps.setInt(1, invoiceId);
            ps.setInt(2, invoiceEntryId);
            return ps;
        });
    }

    private long insertCompany(Company company) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO companies (name, address, tax_identification_number) VALUES (?, ?, ?);", new String[] {"id"});
            ps.setString(1, company.getName());
            ps.setString(2, company.getAddress());
            ps.setString(3, company.getTaxIdentificationNumber());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Invoice> getAll() {
        return jdbcTemplate.query("SELECT i.date, i.number, c1.name AS seller_name, c2.name AS buyer_name FROM invoices i " +
            "INNER JOIN companies c1 ON i.seller = c1.id INNER JOIN companies c2 ON i.buyer = c2.id", ((rs, rowNum) ->
            Invoice.builder()
                .date(rs.getDate("date").toLocalDate())
                .number(rs.getString("number"))
                .buyer(Company.builder().name(rs.getString("buyer_name")).build())
                .seller(Company.builder().name(rs.getString("seller_name")).build())
                .build()
        ));

    }

    @Override
    public void update(int id, Invoice updatedInvoice) {

    }

    @Override
    public void delete(int id) {

    }

}
