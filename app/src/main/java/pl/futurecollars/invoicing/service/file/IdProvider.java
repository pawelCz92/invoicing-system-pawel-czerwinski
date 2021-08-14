package pl.futurecollars.invoicing.service.file;

import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdProvider {

    private final FileService fileService;
    private final Path filePath;

    public IdProvider(Path path, FileService fileService) {
        this.filePath = path;
        this.fileService = fileService;
    }

    public long getNextIdAndIncrement() {
        long id = readLastId() + 1;
        saveId(id);
        return id;
    }

    private int readLastId() {

        List<String> idFileLines = fileService.readLinesToList(filePath);
        String message;
        if (idFileLines.isEmpty()) {
            log.warn("There is no last id - starting from 0");
            return 0;
        }
        if (idFileLines.size() > 1) {
            message = "Something went wrong - in id file should not be more than 1 line";
            log.error(message);
            throw new IllegalStateException(message);
        }
        try {
            return Integer.parseInt(idFileLines.get(0));
        } catch (NumberFormatException e) {
            message = "There was a problem to convert: '" + e.getMessage() + "' to number";
            log.error(message);
            throw new IllegalStateException(message);
        }
    }

    private void saveId(long id) {
        fileService.rewriteFileByList(filePath, List.of(String.valueOf(id)));
    }

    Path getFilePath() {
        return this.filePath;
    }

    public void deleteAll() {
        fileService.truncateFile(filePath);
    }
}
