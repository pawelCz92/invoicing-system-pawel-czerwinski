package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ToFileWriter {

    private final String FILE_NAME;

    public ToFileWriter(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        if (Files.notExists(Path.of(FILE_NAME))) {
            try {
                Files.createFile(Path.of(FILE_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeLineToFile(String line) throws IOException {
        Files.writeString(Path.of(FILE_NAME), line.concat(System.lineSeparator()), StandardOpenOption.APPEND);
    }
}
