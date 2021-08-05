package pl.futurecollars.invoicing.db.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;

public class SqlDatabase implements Database {

    private final JdbcTemplate jdbcTemplate;
    private final SqlService sqlService = new SqlService();
    private final Map<Vat, Integer> vatToId = new HashMap<>();

    public SqlDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public int save(Invoice invoice) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            int buyerId = sqlService.findCompanyByTin(invoice.getBuyer().getTaxIdentificationNumber())
                .map(Company::getId)
                .orElseGet(() -> sqlService.insertCompany(invoice.getBuyer()));


            int sellerId = sqlService.findCompanyByTin(invoice.getSeller().getTaxIdentificationNumber())
                .map(Company::getId).orElseGet(() -> sqlService.insertCompany(invoice.getSeller()));


            int invoiceId = sqlService.insertInvoice(keyHolder, invoice, buyerId, sellerId);
            invoice.setId(invoiceId);
            invoice.getInvoiceEntries().forEach(entry -> {
                sqlService.insertInvoiceEntry(keyHolder, entry);
                int invoiceEntryId = sqlService.insertInvoiceEntry(keyHolder, entry);
                sqlService.insertAssignationInvoiceEntryToInvoice(invoiceId, invoiceEntryId);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoice.getId();
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return sqlService.findInvoiceById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return sqlService.findAllInvoices();
    }

    @Override
    @Transactional
    public void update(int id, Invoice updatedInvoice) {
        sqlService.updateInvoice(id, updatedInvoice);
    }

    @Override
    public void delete(int id) {
        sqlService.deleteInvoiceById(id);
    }

    @PostConstruct
    private void getVatRatesMap() {
        jdbcTemplate.query("SELECT * FROM vat",
            rs -> {
                vatToId.put(Vat.valueOf("VAT_" + rs.getString("name")), rs.getInt("id"));
            });
    }

    private class SqlService {

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
                    "INSERT INTO invoice_entries (description, quantity, net_price, vat_value, vat_rate, expense_related_to_car) " +
                        "VALUES (?, ?, ?, ?, ?, ?);", new String[] {"id"});

                ps.setString(1, entry.getDescription());
                ps.setInt(2, entry.getQuantity());
                ps.setBigDecimal(3, entry.getPrice());
                ps.setBigDecimal(4, entry.getVatValue());
                ps.setInt(5, vatToId.get(entry.getVatRate()));
                ps.setObject(6, insertCar(entry.getCar()));
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

        private Integer insertCompany(Company company) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO companies (name, address, tax_identification_number, health_insurance, pension_insurance) " +
                        "VALUES (?, ?, ?, ?, ?);", new String[] {"id"});
                ps.setString(1, company.getName());
                ps.setString(2, company.getAddress());
                ps.setString(3, company.getTaxIdentificationNumber());
                ps.setBigDecimal(4, company.getHealthInsurance());
                ps.setBigDecimal(5, company.getPensionInsurance());
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue();
        }

        private Integer insertCar(Car car) {
            if (car == null) {
                return null;
            }

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO cars (registration_number, personal_use) values (?, ?);",
                    new String[] {"id"});
                ps.setString(1, car.getRegistration());
                ps.setBoolean(2, car.isIncludingPrivateExpense());
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue();
        }

        private List<Invoice> findAllInvoices() {
            List<Invoice> invoices = jdbcTemplate.query(
                "SELECT i.id, i.date, i.number, c1.id AS seller_id, c2.id AS buyer_id FROM invoices i " +
                    "INNER JOIN companies c1 ON i.seller = c1.id " +
                    "INNER JOIN companies c2 ON i.buyer = c2.id", (rs, rowNum) ->
                    Invoice.builder()
                        .id(rs.getInt("id"))
                        .date(rs.getDate("date").toLocalDate())
                        .number(rs.getString("number"))
                        .buyer(findCompanyById(rs.getInt("buyer_id")))
                        .seller(findCompanyById(rs.getInt("seller_id")))
                        .build()
            );

            return invoices.stream()
                .peek(invoice -> invoice.setInvoiceEntries(getInvoiceEntriesFromInvoice(invoice.getId())))
                .collect(Collectors.toList());
        }

        private Optional<Invoice> findInvoiceById(int id) {
            List<Invoice> invoices = jdbcTemplate.query(
                "SELECT i.id, i.date, i.number, c1.id AS seller_id, c2.id AS buyer_id FROM invoices i " +
                    "INNER JOIN companies c1 ON i.seller = c1.id " +
                    "INNER JOIN companies c2 ON i.buyer = c2.id WHERE i.id = " + id, (rs, rowNum) ->
                    Invoice.builder()
                        .id(rs.getInt("id"))
                        .date(rs.getDate("date").toLocalDate())
                        .number(rs.getString("number"))
                        .buyer(findCompanyById(rs.getInt("buyer_id")))
                        .seller(findCompanyById(rs.getInt("seller_id")))
                        .build()
            );

            return invoices.stream()
                .peek(invoice -> invoice.setInvoiceEntries(getInvoiceEntriesFromInvoice(invoice.getId())))
                .findFirst();
        }

        private Company findCompanyById(int id) {
            return jdbcTemplate.query((
                    "SELECT * FROM companies WHERE companies.id = " + id),
                (rs, rowNum) -> Company.builder()
                    .id(rs.getInt("id"))
                    .taxIdentificationNumber(rs.getString("tax_identification_number"))
                    .address(rs.getString("address"))
                    .name(rs.getString("name"))
                    .healthInsurance(rs.getBigDecimal("health_insurance"))
                    .pensionInsurance(rs.getBigDecimal("pension_insurance"))
                    .build()).stream().findFirst().orElse(null);
        }

        private List<InvoiceEntry> getInvoiceEntriesFromInvoice(int invoiceId) {
            return jdbcTemplate.query(
                ("SELECT * FROM invoices_invoice_entries iie " +
                    "INNER JOIN invoice_entries e ON iie.invoice_entry_id = e.id " +
                    "LEFT OUTER JOIN cars c ON e.expense_related_to_car = c.id " +
                    "WHERE iie.invoice_id = " + invoiceId), (rs, ignored) ->
                    InvoiceEntry.builder()
                        .id(rs.getInt("id"))
                        .description(rs.getString("description"))
                        .quantity(rs.getInt("quantity"))
                        .price(rs.getBigDecimal("net_price"))
                        .vatValue(rs.getBigDecimal("vat_value"))
                        .vatRate(idToVat(rs.getInt("vat_rate")))
                        .car(findCarById(rs.getInt("expense_related_to_car")))
                        .build()
            );
        }

        private Vat idToVat(int id) {
            return vatToId.entrySet().stream()
                .filter(entry -> entry.getValue() == id)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
        }


        private Car findCarById(int id) {
            return jdbcTemplate.query(
                ("SELECT * FROM cars WHERE cars.id = " + id),
                (rs, rowNum) ->
                    Car.builder()
                        .id(rs.getInt("id"))
                        .registration(rs.getString("registration_number"))
                        .isIncludingPrivateExpense(rs.getBoolean("personal_use"))
                        .build()).stream().findFirst().orElse(null);
        }

        private Optional<Company> findCompanyByTin(String taxIdentificationNumber) {
            return jdbcTemplate.query(
                ("SELECT * FROM companies WHERE companies.tax_identification_number = '" + taxIdentificationNumber) + "'", (rs, rowNum) ->
                    Company.builder()
                        .id(rs.getInt("id"))
                        .taxIdentificationNumber(rs.getString("tax_identification_number"))
                        .address(rs.getString("address"))
                        .name(rs.getString("name"))
                        .healthInsurance(rs.getBigDecimal("health_insurance"))
                        .pensionInsurance(rs.getBigDecimal("pension_insurance"))
                        .build()).stream().findFirst();
        }

        private void deleteInvoiceById(int id) {
            jdbcTemplate.update("DELETE FROM invoices WHERE invoices.id = " + id);
        }

        private void updateInvoice(int id, Invoice invoice) {
            deleteInvoiceById(id);
            save(invoice);
        }
    }
}
