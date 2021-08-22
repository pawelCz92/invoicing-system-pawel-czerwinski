package pl.futurecollars.invoicing.db.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;

public class SqlDatabase implements Database<Invoice> {

    private final JdbcTemplate jdbcTemplate;

    public SqlDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Long save(Invoice invoice) {
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            Long buyerId = findCompanyByTin(invoice.getBuyer().getTaxIdentificationNumber())
                .map(Company::getId)
                .orElseGet(() -> insertCompany(invoice.getBuyer()));

            Long sellerId = findCompanyByTin(invoice.getSeller().getTaxIdentificationNumber())
                .map(Company::getId).orElseGet(() -> insertCompany(invoice.getSeller()));

            Long invoiceId = insertInvoice(keyHolder, invoice, buyerId, sellerId);
            invoice.setId(invoiceId);
            invoice.getInvoiceEntries().forEach(entry -> {
                insertInvoiceEntry(keyHolder, entry);
                long invoiceEntryId = insertInvoiceEntry(keyHolder, entry);
                insertAssignationInvoiceEntryToInvoice(invoiceId, invoiceEntryId);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoice.getId();
    }

    @Override
    public Optional<Invoice> getById(Long id) {
        return findInvoiceById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return findAllInvoices();
    }

    @Override
    @Transactional
    public void update(Long id, Invoice updatedInvoice) {
        updateInvoice(id, updatedInvoice);
    }

    @Override
    public void delete(Long id) {
        deleteInvoiceById(id);
    }

    private long insertInvoice(GeneratedKeyHolder keyHolder, Invoice invoice, long buyerId, long sellerId) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO invoices (date, invoice_number, buyer, seller) VALUES (?, ?, ?, ?);", new String[] {"id"});
            ps.setDate(1, Date.valueOf(invoice.getDate()));
            ps.setString(2, invoice.getNumber());
            ps.setLong(3, buyerId);
            ps.setLong(4, sellerId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private long insertInvoiceEntry(GeneratedKeyHolder keyHolder, InvoiceEntry entry) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO invoice_entries (description, quantity, net_price, vat_value, vat_rate, expense_related_to_car) "
                    + "VALUES (?, ?, ?, ?, ?, ?);", new String[] {"id"});

            ps.setString(1, entry.getDescription());
            ps.setBigDecimal(2, entry.getQuantity());
            ps.setBigDecimal(3, entry.getPrice());
            ps.setBigDecimal(4, entry.getVatValue());
            ps.setString(5, entry.getVatRate().name());
            ps.setObject(6, insertCar(entry.getCar()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private void insertAssignationInvoiceEntryToInvoice(long invoiceId, long invoiceEntryId) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO invoice_invoice_entries VALUES (?, ?);");
            ps.setLong(1, invoiceId);
            ps.setLong(2, invoiceEntryId);
            return ps;
        });
    }

    private Long insertCompany(Company company) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO companies (name, address, tax_identification_number, health_insurance, pension_insurance) "
                    + "VALUES (?, ?, ?, ?, ?);", new String[] {"id"});
            ps.setString(1, company.getName());
            ps.setString(2, company.getAddress());
            ps.setString(3, company.getTaxIdentificationNumber());
            ps.setBigDecimal(4, company.getHealthInsurance());
            ps.setBigDecimal(5, company.getPensionInsurance());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private Long insertCar(Car car) {
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

        return keyHolder.getKey().longValue();
    }

    private List<Invoice> findAllInvoices() {
        List<Invoice> invoices = jdbcTemplate.query(
            "SELECT i.id, i.date, i.invoice_number, c1.id AS buyer_id, c1.name AS buyer_name, c1.tax_identification_number AS buyer_tin, "
                + "c1.address AS buyer_address, c1.pension_insurance AS buyer_pension_insurance, c1.health_insurance AS buyer_health_insurance, "
                + "c2.id AS seller_id, c2.name AS seller_name, c2.tax_identification_number AS seller_tin, c2.address AS seller_address, "
                + "c2.pension_insurance AS seller_pension_insurance, c2.health_insurance AS seller_health_insurance "
                + "FROM invoices i "
                + "INNER JOIN companies c1 ON i.buyer = c1.id "
                + "INNER JOIN companies c2 ON i.seller = c2.id",
            (rs, rowNum) ->
                Invoice.builder()
                    .id(rs.getLong("id"))
                    .date(rs.getDate("date").toLocalDate())
                    .number(rs.getString("invoice_number"))
                    .buyer(Company.builder()
                        .id(rs.getLong("buyer_id"))
                        .address(rs.getString("buyer_address"))
                        .taxIdentificationNumber(rs.getString("buyer_tin"))
                        .name(rs.getString("buyer_name"))
                        .pensionInsurance(rs.getBigDecimal("buyer_pension_insurance"))
                        .healthInsurance(rs.getBigDecimal("buyer_health_insurance"))
                        .build())
                    .seller(Company.builder()
                        .id(rs.getLong("seller_id"))
                        .address(rs.getString("seller_address"))
                        .taxIdentificationNumber(rs.getString("seller_tin"))
                        .name(rs.getString("seller_name"))
                        .pensionInsurance(rs.getBigDecimal("seller_pension_insurance"))
                        .healthInsurance(rs.getBigDecimal("seller_health_insurance"))
                        .build())
                    .build()
        );

        return invoices.stream()
            .peek(invoice -> invoice.setInvoiceEntries(getInvoiceEntriesFromInvoice(invoice.getId())))
            .collect(Collectors.toList());
    }

    private Optional<Invoice> findInvoiceById(long id) {
        List<Invoice> invoices = jdbcTemplate.query(
            "SELECT i.id, i.date, i.invoice_number, c1.id AS seller_id, c2.id AS buyer_id FROM invoices i "
                + "INNER JOIN companies c1 ON i.seller = c1.id "
                + "INNER JOIN companies c2 ON i.buyer = c2.id WHERE i.id = " + id, (rs, rowNum) ->
                Invoice.builder()
                    .id(rs.getLong("id"))
                    .date(rs.getDate("date").toLocalDate())
                    .number(rs.getString("invoice_number"))
                    .buyer(findCompanyById(rs.getInt("buyer_id")).orElse(null))
                    .seller(findCompanyById(rs.getInt("seller_id")).orElse(null))
                    .invoiceEntries(getInvoiceEntriesFromInvoice(id))
                    .build()
        );
        return invoices.stream().findFirst();
    }

    private Optional<Company> findCompanyById(long id) {
        return jdbcTemplate.query(
            "SELECT * FROM companies WHERE companies.id = " + id,
            (rs, rowNum) -> Company.builder()
                .id(rs.getLong("id"))
                .taxIdentificationNumber(rs.getString("tax_identification_number"))
                .address(rs.getString("address"))
                .name(rs.getString("name"))
                .healthInsurance(rs.getBigDecimal("health_insurance"))
                .pensionInsurance(rs.getBigDecimal("pension_insurance"))
                .build()).stream().findFirst();
    }

    private List<InvoiceEntry> getInvoiceEntriesFromInvoice(long invoiceId) {
        return jdbcTemplate.query(
            "SELECT * FROM invoice_invoice_entries iie "
                + "INNER JOIN invoice_entries e ON iie.invoice_entry_id = e.id "
                + "LEFT OUTER JOIN cars c ON e.expense_related_to_car = c.id "
                + "WHERE iie.invoice_id = " + invoiceId, (rs, ignored) ->
                InvoiceEntry.builder()
                    .id(rs.getLong("id"))
                    .description(rs.getString("description"))
                    .quantity(rs.getBigDecimal("quantity"))
                    .price(rs.getBigDecimal("net_price"))
                    .vatValue(rs.getBigDecimal("vat_value"))
                    .vatRate(Vat.valueOf(rs.getString("vat_rate")))
                    .car(findCarById(rs.getLong("expense_related_to_car")))
                    .build()
        );
    }

    private Car findCarById(long id) {
        return jdbcTemplate.query(
            "SELECT * FROM cars WHERE cars.id = " + id,
            (rs, rowNum) ->
                Car.builder()
                    .id(rs.getLong("id"))
                    .registration(rs.getString("registration_number"))
                    .isIncludingPrivateExpense(rs.getBoolean("personal_use"))
                    .build()).stream().findFirst().orElse(null);
    }

    private Optional<Company> findCompanyByTin(String taxIdentificationNumber) {
        return jdbcTemplate.query(
            ("SELECT * FROM companies WHERE companies.tax_identification_number = '" + taxIdentificationNumber) + "'", (rs, rowNum) ->
                Company.builder()
                    .id(rs.getLong("id"))
                    .taxIdentificationNumber(rs.getString("tax_identification_number"))
                    .address(rs.getString("address"))
                    .name(rs.getString("name"))
                    .healthInsurance(rs.getBigDecimal("health_insurance"))
                    .pensionInsurance(rs.getBigDecimal("pension_insurance"))
                    .build()).stream().findFirst();
    }

    private void deleteInvoiceEntryAndCarByInvoiceId(long invoiceId) {
        jdbcTemplate.update("DELETE FROM invoice_invoice_entries iie WHERE iie.invoice_id = " + invoiceId);
    }

    private void deleteInvoiceById(long id) {
        if (findInvoiceById(id).isPresent()) {
            deleteInvoiceEntryAndCarByInvoiceId(id);
            jdbcTemplate.update("DELETE FROM invoices WHERE invoices.id = " + id);
        } else {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
    }

    private void updateInvoice(long id, Invoice invoice) {
        if (findInvoiceById(id).isPresent()) {
            jdbcTemplate.update("UPDATE invoices SET invoices.date = ? WHERE invoices.id = ?",
                invoice.getDate(), invoice.getId());
            jdbcTemplate.update("UPDATE invoices SET invoices.invoice_number = ? WHERE invoices.id = ?",
                invoice.getNumber(), invoice.getId());

            long buyerId = jdbcTemplate.query("SELECT i.buyer FROM invoices i WHERE i.id = " + id,
                (rs, rowNr) -> rs.getLong(1)).get(0);
            long sellerId = jdbcTemplate.query("SELECT i.seller FROM invoices i WHERE i.id = " + id,
                (rs, rowNr) -> rs.getLong(1)).get(0);

            Company actualBuyer = findCompanyById(buyerId).get();
            Company actualSeller = findCompanyById(sellerId).get();

            long newBuyerId = buyerId;
            long newSellerId = sellerId;

            if (!actualBuyer.equals(invoice.getBuyer())) {
                newBuyerId = findCompanyByTin(invoice.getBuyer().getTaxIdentificationNumber())
                    .map(Company::getId)
                    .orElseGet(() -> insertCompany(invoice.getBuyer()));
            }
            if (!actualSeller.equals(invoice.getSeller())) {
                newSellerId = findCompanyByTin(invoice.getSeller().getTaxIdentificationNumber())
                    .map(Company::getId)
                    .orElseGet(() -> insertCompany(invoice.getSeller()));
            }
            if (buyerId != newBuyerId) {
                jdbcTemplate.update("UPDATE invoices SET invoices.buyer = ? WHERE invoices.id = ?",
                    newBuyerId, invoice.getId());
            }
            if (sellerId != newSellerId) {
                jdbcTemplate.update("UPDATE invoices SET invoices.seller = ? WHERE invoices.id = ?",
                    newSellerId, invoice.getId());
            }
            Invoice orginalInvoice = findInvoiceById(id).get();
            if (!(orginalInvoice.getInvoiceEntries().size() == invoice.getInvoiceEntries().size()
                && orginalInvoice.getInvoiceEntries().containsAll(invoice.getInvoiceEntries()))) {

                deleteInvoiceEntryAndCarByInvoiceId(id);
                invoice.getInvoiceEntries()
                    .forEach(invoiceEntry -> {
                        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
                        insertAssignationInvoiceEntryToInvoice(id, insertInvoiceEntry(generatedKeyHolder, invoiceEntry));
                    });
            }

        } else {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
    }
}
