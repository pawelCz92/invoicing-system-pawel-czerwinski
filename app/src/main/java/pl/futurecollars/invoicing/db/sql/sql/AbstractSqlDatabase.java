package pl.futurecollars.invoicing.db.sql.sql;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import pl.futurecollars.invoicing.model.Company;

public class AbstractSqlDatabase {

    JdbcTemplate jdbcTemplate;

    public AbstractSqlDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected Long insertCompany(Company company) {
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

    protected Optional<Company> findCompanyById(long id) {
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

    protected Optional<Company> findCompanyByTin(String taxIdentificationNumber) {
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
}
