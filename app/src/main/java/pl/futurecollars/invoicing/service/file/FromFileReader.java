package pl.futurecollars.invoicing.service.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FromFileReader {

    private final File file;

    public FromFileReader(String fileName) {
        this.file = new File(fileName);
    }

    List<String> readLinesFromFile() throws IOException {
        return Files.readAllLines(file.toPath());
    }
}
