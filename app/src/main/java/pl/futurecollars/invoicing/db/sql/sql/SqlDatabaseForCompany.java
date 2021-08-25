package pl.futurecollars.invoicing.db.sql.sql;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

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
        return null;
    }

    @Override
    public void update(Long id, Company updatedItem) {

    }

    @Override
    public void delete(Long id) {

    }
}
