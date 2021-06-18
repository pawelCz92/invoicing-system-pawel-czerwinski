package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.util.List;

public class FileService {

    private final String fileName;
    private final FromFileReader fromFileReader;
    private final ToFileWriter toFileWriter;

    public FileService(String fileName, boolean appendToFile) {
        this.fileName = fileName;
        this.fromFileReader = new FromFileReader(this.fileName);
        this.toFileWriter = new ToFileWriter(this.fileName, appendToFile);
    }

    public void writeLine(String line) {
        try {
            toFileWriter.writeLineToFile(line);
        } catch (IOException e) {
            throw new IllegalStateException("There is problem to write to file: " + fileName);
        }
    }

    public List<String> readLinesToList() {
        try {
            return fromFileReader.readLinesFromFile();
        } catch (IOException e) {
            throw new IllegalStateException("There was problem to read file: " + fileName);
        }
    }
}
