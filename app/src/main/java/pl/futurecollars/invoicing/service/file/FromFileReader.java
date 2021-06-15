package pl.futurecollars.invoicing.service.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FromFileReader {

    private final String fileName;
    private final File file;

    public FromFileReader(String fileName) {
        this.fileName = fileName;
        this.file = new File(this.fileName);
    }

    public List<String> readLinesFromFile() throws IOException {
        return Files.readAllLines(file.toPath());
    }
}
