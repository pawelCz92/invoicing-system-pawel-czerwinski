package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ToFileWriter {

    private final String fileName;

    public ToFileWriter(String fileName) {
        this.fileName = fileName;
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

    public void writeLineToFile(String line) throws IOException {
        Files.writeString(Path.of(fileName), line.concat(System.lineSeparator()), StandardOpenOption.APPEND);
    }
}
