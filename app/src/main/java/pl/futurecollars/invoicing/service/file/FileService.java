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
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileService {

    private static final Pattern PATTERN_FOR_SEARCH_BY_ID = Pattern.compile("^\\{\"id\":(\\d+).+");

    public void appendLine(Path filePath, String line) {
        try {
            Files.writeString(filePath, line.concat(System.lineSeparator()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            String message = "There was problem to write to file: " + filePath;
            log.error(message);
            throw new IllegalStateException(message);
        }
    }

    public void rewriteFileByList(Path filePath, List<String> lines) {
        try {
            Files.write(filePath, lines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            String message = "There is problem to update base file";
            log.error(message);
            throw new IllegalStateException(message);
        }
    }

    public List<String> readLinesToList(Path filePath) {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            String message = "There was problem to read from file " + filePath;
            log.error(message);
            throw new IllegalStateException(message);
        }
    }

    public Optional<String> findLineById(Path filePath, int id) {
        List<String> searchResult;
        searchResult = readLinesToList(filePath).stream()
            .filter(line -> checkMatching(line, id))
            .collect(Collectors.toList());
        if (searchResult.size() > 1) {
            String message = String.format("Error - There is %d id's %d in base.", searchResult.size(), id);
            log.error(message);
            throw new IllegalStateException(message);
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

    public Optional<Integer> getLineNumberById(Path filePath, int id) {
        List<String> lines = readLinesToList(filePath);
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

    public void truncateFile(Path filePath) {
        try {
            Files.writeString(filePath, "", StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
