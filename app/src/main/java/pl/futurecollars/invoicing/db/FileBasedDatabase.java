package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;

@Slf4j
public class FileBasedDatabase implements Database {

    private final FileService fileServiceForData;
    private final FileService fileServiceForId;
    private final JsonService jsonService = new JsonService();
    private int id;

    public FileBasedDatabase(String fileNameForData, String fileNameForIds) {
        this.fileServiceForData = new FileService(fileNameForData);
        this.fileServiceForId = new FileService(fileNameForIds);
    }

    @Override
    public int save(Invoice invoice) {
        id = getLastId() + 1;
        invoice.setId(id);
        fileServiceForData.writeLine(jsonService.objectToString(invoice));
        updateLastId(id);
        return id;
    }

    @Override
    public Optional<Invoice> getById(int id) {
        Optional<String> founded = fileServiceForData.findLineById(id);
        return founded.map(line -> jsonService.stringToObject(line, Invoice.class));
    }

    @Override
    public List<Invoice> getAll() {
        return fileServiceForData.readLinesToList().stream()
            .map(line -> jsonService.stringToObject(line, Invoice.class))
            .collect(Collectors.toList());
    }

    @Override
    public void update(int id, Invoice updatedInvoice) {
        int lineNumber = fileServiceForData.getLineNumberById(id);
        List<Invoice> allInvoices = getAll();
        updatedInvoice.setId(id);
        allInvoices.set(lineNumber, updatedInvoice);
        fileServiceForData.updateBaseFile(
            allInvoices.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }

    @Override
    public void delete(int id) {
        int lineNumber = fileServiceForData.getLineNumberById(id);
        List<Invoice> allInvoices = getAll();
        allInvoices.remove(lineNumber);
        fileServiceForData.updateBaseFile(
            allInvoices.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }

    private int getLastId() {
        List<String> idFileLines = fileServiceForId.readLinesToList();
        int lastId = 0;
        if (idFileLines.isEmpty()){
            return 0;
        }
        try {
            lastId = Integer.parseInt(idFileLines.get(idFileLines.size() - 1));
        } catch (NumberFormatException e) {
            log.error("There was problem to read last id or there is no id yet");
        }
        return lastId;
    }

    private void updateLastId(int id) {
        fileServiceForId.writeLine(String.valueOf(id));
    }
}
