package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.util.List;

public class FileService {

    private final String FILE_NAME;
    private final FromFileReader fromFileReader;
    private final ToFileWriter toFileWriter;

    public FileService(String fileName) {
        this.FILE_NAME = fileName;
        this.fromFileReader = new FromFileReader(FILE_NAME);
        this.toFileWriter = new ToFileWriter(FILE_NAME);
    }

    public void writeLine(String line) {
        try {
            toFileWriter.writeLineToFile(line);
        } catch (IOException e) {
            throw new IllegalStateException("There is problem to write to file: " + FILE_NAME);
        }
    }

    public List<String> readLines() {
        try {
            return fromFileReader.readLinesFromFile();
        } catch (IOException e) {
            throw new IllegalStateException("There was problem to read file: " + FILE_NAME);
        }
    }
}
