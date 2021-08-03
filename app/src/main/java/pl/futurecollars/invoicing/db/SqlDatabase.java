package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.model.Invoice;

@AllArgsConstructor
public class SqlDatabase implements Database{

    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Invoice invoice) {
        try{
            jdbcTemplate.execute(
                "insert into invoicing.public.companies (name, address) values ('International Transporters', 'Washington Post 3');");
            jdbcTemplate.execute(
                "insert into invoicing.public.invoices (date, number, buyer, seller) values ('2021-03-05' , '2021/03/05/0001', 1, 2);");
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Invoice> getAll() {
        return null;
    }

    @Override
    public void update(int id, Invoice updatedInvoice) {

    }

    @Override
    public void delete(int id) {

    }
}
