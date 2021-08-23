package pl.futurecollars.invoicing.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InMemoryDataBase<T extends WithId> implements Database<T> {

    private final HashMap<Long, T> inMemoryDatabase = new HashMap<>();
    private Long index = 1L;

    @Override
    public Long save(T item) {
        item.setId(index);
        inMemoryDatabase.put(index, item);
        return index++;
    }

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(inMemoryDatabase.get(id));
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(inMemoryDatabase.values());
    }

    @Override
    public void update(Long id, T updatedItem) {
        if (!inMemoryDatabase.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
        updatedItem.setId(id);
        inMemoryDatabase.put(id, updatedItem);
    }

    @Override
    public void delete(Long id) {
        if (!inMemoryDatabase.containsKey(id)) {
            throw new IllegalArgumentException("Id " + id + " does not exists");
        }
        inMemoryDatabase.remove(id);
    }
}
