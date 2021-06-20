package pl.futurecollars.invoicing.service.file;

import static pl.futurecollars.invoicing.Configuration.DEFAULT_LINE_SEPARATOR;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FileService {

    private final String fileName;
    private final FromFileReader fromFileReader;
    private final ToFileWriter toFileWriter;

    public FileService(String fileName) {
        this.fileName = fileName;
        this.fromFileReader = new FromFileReader(this.fileName, DEFAULT_LINE_SEPARATOR);
        this.toFileWriter = new ToFileWriter(this.fileName, DEFAULT_LINE_SEPARATOR);
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

    public Optional<String> findLineById(int id) {
        try {
            return fromFileReader.findLineById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Integer getLineNumberById(int id) {
        try {
            return fromFileReader.getLineNumberById(id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void updateBaseFile(List<String> lines) {
        try {
            toFileWriter.rewriteFile(lines);
        } catch (IOException e) {
            throw new IllegalStateException("There is problem to update base file");
        }
    }
}
