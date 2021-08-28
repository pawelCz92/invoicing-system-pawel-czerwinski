package pl.futurecollars.invoicing.db.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;

@RequiredArgsConstructor
@Slf4j
public class JpaDatabase<T extends WithId> implements Database<T> {

    private final CrudRepository<T, Long> repository;

    @Override
    public Long save(T item) {
        item.setId(null);
        try {
            return repository.save(item).getId();
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + item);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<T> getAll() {
        return StreamSupport
            .stream(repository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, T updatedItem) {
        Optional<T> itemOpt = getById(id);

        if (itemOpt.isPresent()) {
            repository.save(updatedItem);
        } else {
            String message = "Id: " + id + " not found. Update impossible";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<T> item = getById(id);
        item.ifPresentOrElse(repository::delete, () -> {
            String message = "There is no such id for delete item";
            log.error(message);
            throw new IllegalArgumentException(message);
        });
    }
}
