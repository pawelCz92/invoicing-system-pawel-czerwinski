package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;

public interface Database<T extends WithId> {

    Long save(T item);

    Optional<T> getById(Long id);

    List<T> getAll();

    void update(Long id, T updatedItem);

    void delete(Long id);

    default void reset() {
        getAll().forEach(item -> delete(item.getId()));
    }
}
