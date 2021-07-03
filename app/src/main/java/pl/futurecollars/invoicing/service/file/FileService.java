package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileService {

    private static final Pattern PATTERN_FOR_SEARCH_BY_ID = Pattern.compile("^\\{\"id\":(\\d+).+");
    private final Path filePath;

    public FileService(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public void appendLine(String line) {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                log.warn("File not exists: " + filePath + " - it will be created");
            }
            Files.writeString(filePath, line.concat(System.lineSeparator()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("There was problem to read file: " + filePath);
        }
    }

    public void rewriteFileByList(List<String> lines) {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
            Files.write(filePath, lines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("There is problem to update base file");
        }
    }

    public List<String> readLinesToList() {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            log.error("There was problem to read from file");
            throw new IllegalStateException("There was problem to read file: " + filePath);
        }
    }

    public Optional<String> findLineById(int id) {
        List<String> searchResult = null;
        try {
            searchResult = readLinesToList().stream()
                .filter(line -> checkMatching(line, id))
                .collect(Collectors.toList());
        } catch (Exception e) {
            return Optional.empty();
        }
        if (searchResult.size() > 1) {
            throw new IllegalStateException("Error - There is " + searchResult.size() + " id's: " + id + " in base...");
        }
        if (searchResult.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(searchResult.get(0));
    }

    private boolean checkMatching(String line, int id) {
        Matcher matcher = PATTERN_FOR_SEARCH_BY_ID.matcher(line);
        if (matcher.matches()) {
            return matcher.group(1).equals(String.valueOf(id));
        }
        return false;
    }

    public Optional<Integer> getLineNumberById(int id) {
        List<String> lines = readLinesToList();
        int lineNumber = 0;
        int foundedIdNumber = 0;

        for (int i = 0; i < lines.size(); i++) {
            if (checkMatching(lines.get(i), id)) {
                lineNumber = i;
                foundedIdNumber++;
            }
        }
        if (foundedIdNumber > 1) {
            return Optional.empty();
        }
        if (foundedIdNumber == 0) {
            return Optional.empty();
        }
        return Optional.of(lineNumber);
    }
}
