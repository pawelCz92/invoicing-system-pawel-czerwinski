package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ToFileWriter {

    private final Path filePath;
    private final String lineSeparator;

    public ToFileWriter(String fileName, String lineSeparator) {
        this.filePath = Path.of(fileName);
        this.lineSeparator = lineSeparator;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        if (Files.notExists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void writeLineToFile(String line) throws IOException {
            Files.writeString(filePath, line.concat(lineSeparator), StandardOpenOption.APPEND);
    }
}
