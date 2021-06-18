package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ToFileWriter {

    private final String fileName;
    private final boolean appendToFile;

    public ToFileWriter(String fileName, boolean appendToFile) {
        this.fileName = fileName;
        this.appendToFile = appendToFile;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        if (Files.notExists(Path.of(fileName))) {
            try {
                Files.createFile(Path.of(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void writeLineToFile(String line) throws IOException {
        if (appendToFile) {
            Files.writeString(Path.of(fileName), line.concat(System.lineSeparator()), StandardOpenOption.APPEND);
        } else {
            Files.writeString(Path.of(fileName), line.concat(System.lineSeparator()), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
