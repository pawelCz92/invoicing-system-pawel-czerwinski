package pl.futurecollars.invoicing.service.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FromFileReader {

    private static final Pattern PATTERN_FOR_SEARCH_BY_ID = Pattern.compile("^\\{\"id\":(\\d+).+");
    private final Path filePath;
    private final String lineSeparator;

    public FromFileReader(String filePath, String lineSeparator) {
        this.filePath = Path.of(filePath);
        this.lineSeparator = lineSeparator;
    }

    List<String> readLinesFromFile() throws IOException {
        String fileContent = Files.readString(filePath);
        fileContent = fileContent.trim();
        return Arrays.asList(fileContent.split(lineSeparator));
    }

    Optional<String> findLineById(int id) throws IOException, IllegalStateException {

        List<String> searchResult = readLinesFromFile().stream()
            .filter(line -> checkMatching(line, id, PATTERN_FOR_SEARCH_BY_ID))
            .collect(Collectors.toList());

        if (searchResult.size() > 1) {
            throw new IllegalStateException("Error - There is " + searchResult.size() + " id's: " + id + " in base...");
        }
        if (searchResult.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(searchResult.get(0));
    }

    public boolean checkMatching(String line, int id, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        matcher.matches();
        return matcher.group(1).equals(String.valueOf(id));
    }


}
