package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.service.file.FileService;

@Slf4j
public class IdProvider {

    private final FileService fileService;
    private final Path filePath;

    public IdProvider(Path filePath) {
        this.fileService = new FileService(filePath);
        this.filePath = filePath;

    }

    public int getNextIdAndIncrement() {
        int id = readLastId() + 1;
        saveId(id);
        return id;
    }

    private int readLastId() {
        createFileIfNotExists();
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

    private void createFileIfNotExists() {
        if (Files.notExists(filePath)) {
            try {
                if (!Objects.isNull(filePath.getParent())) {
                    Files.createDirectories(filePath.getParent());
                }
                Files.createFile(filePath);
                log.info("IdProvider file was created");
            } catch (IOException e) {
                String message = "There was problem to create file for idProvider: '" + filePath + "'";
                log.error(message);
                e.printStackTrace();
                throw new IllegalStateException(message);
            }
        }
    }
}
