package pl.futurecollars.invoicing.db;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;
import pl.futurecollars.invoicing.service.file.IdProvider;

@Slf4j
public class FileBasedDatabase implements Database {

    private final Path dbFilePath;
    private final IdProvider idProvider;
    private final FileService fileServiceForData;
    private final JsonService jsonService;

    public FileBasedDatabase(Path dbFilePath,
                             IdProvider idProvider,
                             FileService fileService,
                             JsonService jsonService) {
        this.dbFilePath = dbFilePath;
        this.idProvider = idProvider;
        this.fileServiceForData = fileService;
        this.jsonService = jsonService;
    }

    @Override
    public Long save(Invoice invoice) {
        Long id = idProvider.getNextIdAndIncrement();
        invoice.setId(id);
        fileServiceForData.appendLine(dbFilePath, jsonService.objectToString(invoice));
        return id;
    }

    @Override
    public Optional<Invoice> getById(Long id) {
        Optional<String> founded = fileServiceForData.findLineById(dbFilePath, id);
        return founded.map(line -> jsonService.stringToObject(line, Invoice.class));
    }

    @Override
    public List<Invoice> getAll() {
        return fileServiceForData.readLinesToList(dbFilePath).stream()
            .map(line -> jsonService.stringToObject(line, Invoice.class))
            .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, Invoice updatedInvoice) {
        int lineNumber = fileServiceForData.getLineNumberById(dbFilePath, id)
            .orElseThrow(() -> new IllegalArgumentException("There is no id : " + id));

        List<Invoice> allInvoices = getAll();
        updatedInvoice.setId(id);

        allInvoices.set(lineNumber, updatedInvoice);
        fileServiceForData.rewriteFileByList(dbFilePath,
            allInvoices.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }

    @Override
    public void delete(Long id) {
        int lineNumber = fileServiceForData.getLineNumberById(dbFilePath, id)
            .orElseThrow(() -> new IllegalArgumentException("There is no id : " + id));
        List<Invoice> allInvoices = getAll();
        allInvoices.remove(lineNumber);
        fileServiceForData.rewriteFileByList(dbFilePath,
            allInvoices.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }

    public void deleteAll() {
        fileServiceForData.truncateFile(dbFilePath);
        idProvider.deleteAll();
    }
}
