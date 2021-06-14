package pl.futurecollars.invoicing.service.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FromFileReader {

    private final String FILE_NAME;
    private final File FILE;

    public FromFileReader(String fileName) {
        this.FILE_NAME = fileName;
        this.FILE = new File(FILE_NAME);
    }

    public List<String> readLinesFromFile() throws IOException {
        return Files.readAllLines(FILE.toPath());
    }
}
