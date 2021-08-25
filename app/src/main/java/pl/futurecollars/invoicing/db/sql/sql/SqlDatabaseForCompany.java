package pl.futurecollars.invoicing.db.sql.sql;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

@Slf4j
public class SqlDatabaseForCompany extends AbstractSqlDatabase implements Database<Company> {

    public SqlDatabaseForCompany(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Long save(Company company) {
        return insertCompany(company);
    }

    @Override
    public Optional<Company> getById(Long id) {
        return findCompanyById(id);
    }

    @Override
    public List<Company> getAll() {

        return jdbcTemplate.query("SELECT * FROM companies", (rs, rowNum) ->
            Company.builder()
                .id(rs.getLong("id"))
                .taxIdentificationNumber(rs.getString("tax_identification_number"))
                .address(rs.getString("address"))
                .name(rs.getString("name"))
                .healthInsurance(rs.getBigDecimal("health_insurance"))
                .pensionInsurance(rs.getBigDecimal("pension_insurance"))
                .build());
    }

    @Override
    public void update(Long id, Company updatedCompany) {
        if (Objects.isNull(updatedCompany)) {
            String message = "Updated company must not be null";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        if (getById(id).isPresent()) {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE companies SET companies.tax_identification_number = ? WHERE companies.id = ?;");
                ps.setString(1, updatedCompany.getTaxIdentificationNumber());
                ps.setLong(2, id);

                ps = con.prepareStatement(
                    "UPDATE companies SET companies.address = ? WHERE companies.id = ?;");
                ps.setString(1, updatedCompany.getAddress());
                ps.setLong(2, id);

                ps = con.prepareStatement(
                    "UPDATE companies SET companies.name = ? WHERE companies.id = ?;");
                ps.setString(1, updatedCompany.getName());
                ps.setLong(2, id);

                ps = con.prepareStatement(
                    "UPDATE companies SET companies.health_insurance = ? WHERE companies.id = ?;");
                ps.setBigDecimal(1, updatedCompany.getHealthInsurance());
                ps.setLong(2, id);

                ps = con.prepareStatement(
                    "UPDATE companies SET companies.pension_insurance = ? WHERE companies.id = ?;");
                ps.setBigDecimal(1, updatedCompany.getPensionInsurance());
                ps.setLong(2, id);

                return ps;
            });
        }
    }

    @Override
    public void delete(Long id) {
        if (getById(id).isPresent()) {
            jdbcTemplate.update("DELETE FROM companies WHERE companies.id = " + id);
        } else {
            String message = "Id " + id + " does not exists";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
