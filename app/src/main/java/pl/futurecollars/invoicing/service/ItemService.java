package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;

@Service
@RequiredArgsConstructor
public class ItemService<T extends WithId> {

    private final Database<T> database;

    public Long save(T item) {
        return database.save(item);
    }

    public Optional<T> getById(Long id) {
        return database.getById(id);
    }

    public List<T> getAll() {
        return database.getAll();
    }

    public void update(Long id, T updatedItem) throws IllegalArgumentException {
        database.update(id, updatedItem);
    }

    public void delete(Long id) {
        database.delete(id);
    }
}
