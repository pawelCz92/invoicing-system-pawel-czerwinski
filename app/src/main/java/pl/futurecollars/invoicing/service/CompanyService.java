package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final Database<Company> database;

    public Long save(Company company) {
        return database.save(company);
    }

    public Optional<Company> getById(Long id) {
        return database.getById(id);
    }

    public List<Company> getAll() {
        return database.getAll();
    }

    public void update(Long id, Company updatedCompany) throws IllegalArgumentException {
        database.update(id, updatedCompany);
    }

    public void delete(Long id) {
        database.delete(id);
    }
}
