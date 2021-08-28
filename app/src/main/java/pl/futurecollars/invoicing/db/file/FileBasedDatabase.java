package pl.futurecollars.invoicing.db.file;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;
import pl.futurecollars.invoicing.service.file.IdProvider;

@Slf4j
@RequiredArgsConstructor
public class FileBasedDatabase<T extends WithId> implements Database<T> {

    private final Path idFilePath;
    private final IdProvider idProvider;
    private final FileService fileServiceForData;
    private final JsonService jsonService;
    private final Class<T> clazz;

    @Override
    public Long save(T item) {
        Long id = idProvider.getNextIdAndIncrement();
        item.setId(id);
        fileServiceForData.appendLine(idFilePath, jsonService.objectToString(item));
        return id;
    }

    @Override
    public Optional<T> getById(Long id) {
        Optional<String> founded = fileServiceForData.findLineById(idFilePath, id);
        return founded.map(line -> jsonService.stringToObject(line, clazz));
    }

    @Override
    public List<T> getAll() {
        return fileServiceForData.readLinesToList(idFilePath).stream()
            .map(line -> jsonService.stringToObject(line, clazz))
            .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, T updatedItem) {
        int lineNumber = fileServiceForData.getLineNumberById(idFilePath, id)
            .orElseThrow(() -> new IllegalArgumentException("There is no id : " + id));

        List<T> allItems = getAll();
        updatedItem.setId(id);

        allItems.set(lineNumber, updatedItem);
        fileServiceForData.rewriteFileByList(idFilePath,
            allItems.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }

    @Override
    public void delete(Long id) {
        int lineNumber = fileServiceForData.getLineNumberById(idFilePath, id)
            .orElseThrow(() -> new IllegalArgumentException("There is no id : " + id));
        List<T> allItems = getAll();
        allItems.remove(lineNumber);
        fileServiceForData.rewriteFileByList(idFilePath,
            allItems.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }
}
