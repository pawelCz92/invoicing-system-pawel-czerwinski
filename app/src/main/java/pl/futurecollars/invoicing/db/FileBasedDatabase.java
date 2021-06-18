package pl.futurecollars.invoicing.db;

import static pl.futurecollars.invoicing.Configuration.DB_DATA_FILE_NAME_PATH;
import static pl.futurecollars.invoicing.Configuration.DB_ID_FILE_NAME_PATH;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;

public class FileBasedDatabase implements Database {


    private final FileService fileServiceForData;
    private final FileService fileServiceForId;
    private final JsonService jsonService = new JsonService();
    private int id = 1;

    public FileBasedDatabase() {
        this.fileServiceForData = new FileService(DB_DATA_FILE_NAME_PATH, true);
        this.fileServiceForId = new FileService(DB_ID_FILE_NAME_PATH, false);
        id = getLastId();

    }

    public FileBasedDatabase(String path) {
        fileServiceForData = new FileService(path, true);
        this.fileServiceForId = new FileService(DB_ID_FILE_NAME_PATH, false);
    }


    @Override
    public int save(Invoice invoice) {
        invoice.setId(id);
        fileServiceForData.writeLine(jsonService.objectToString(invoice));
        updateLastId(id);
        return id++;
    }

    @Override
    public Optional<Invoice> getById(int id) {
        //"{\"id\":0,"

        Pattern pattern = Pattern.compile("^[{\"]id[\":](\\d+)");
        List<String> searchResult = fileServiceForData.readLinesToList().stream()
            .filter(line -> pattern.matcher(line).group().equals(String.valueOf(id)))
            .collect(Collectors.toList());

        if (searchResult.size() > 1) {
            throw new IllegalArgumentException("There is " + searchResult.size() + "id's: " + id + " in base...");
        }
        if (searchResult.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(jsonService.stringToObject(searchResult.get(0), Invoice.class));
    }

    @Override
    public List<Invoice> getAll() {
        return fileServiceForData.readLinesToList().stream()
            .map(line -> jsonService.stringToObject(line, Invoice.class))
            .collect(Collectors.toList());
    }

    @Override
    public void update(int id, Invoice updatedInvoice) {

    }

    @Override
    public void delete(int id) {

    }

    private int getLastId() {
        List<String> idFileLines = fileServiceForId.readLinesToList();
        if (idFileLines.isEmpty()) {
            System.out.println("ID file content emppty - id starting from 1");
            return 1;
        }
        return Integer.parseInt(idFileLines.get(0));
    }

    private void updateLastId(int id) {
        fileServiceForId.writeLine(String.valueOf(id));
    }
}
