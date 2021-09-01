package pl.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;

@RequiredArgsConstructor
@Slf4j
public class MongoBaseDatabase<T extends WithId> implements Database<T> {

    private final MongoCollection<T> items;
    private final MongoIdProvider idProvider;

    @Override
    public Long save(T item) {
        item.setId(idProvider.getNextIdAndIncrement());
        items.insertOne(item);

        return item.getId();
    }

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(
            items.find(idFilter(id)).first()
        );
    }

    @Override
    public List<T> getAll() {
        return StreamSupport
            .stream(items.find().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, T updatedItem) {
        updatedItem.setId(id);

        if (Objects.isNull(items.findOneAndReplace(idFilter(id), updatedItem))) {
            String message = "Id: " + id + " not found. Update impossible";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void delete(Long id) {
        if (Objects.isNull(items.findOneAndDelete(idFilter(id)))) {
            String message = "Id: " + id + " not found. Delete impossible";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private Document idFilter(long id) {
        return new Document("_id", id);
    }
}
