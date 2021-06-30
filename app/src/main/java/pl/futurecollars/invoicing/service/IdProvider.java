package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.service.file.FileService;

@Slf4j
public class IdProvider {

    private final FileService fileService;
    private final String fileName;

    public IdProvider(String fileName) {
        this.fileService = new FileService(fileName);
        this.fileName = fileName;
    }

    public int getNextIdAndIncrement() {
        int id = readLastId() + 1;
        saveId(id);
        return id;
    }

    private int readLastId() {
        createFileIfNotExists(fileName);
        List<String> idFileLines = fileService.readLinesToList();
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

    private void saveId(int id) {
        fileService.rewriteFileByList(List.of(String.valueOf(id)));
    }

    private void createFileIfNotExists(String fileName) {
        if (Files.notExists(Path.of(fileName))) {
            try {
                Files.createFile(Path.of(fileName));
            } catch (IOException e) {
                String message = "There was problem to create file for idProvider";
                log.error(message);
                throw new IllegalStateException(message);
            }
        }
    }
}
