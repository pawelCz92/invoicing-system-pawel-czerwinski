package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.IdProvider;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.file.FileService;

@Slf4j
public class FileBasedDatabase implements Database {

    private final FileService fileServiceForData;
    private final IdProvider idProvider;
    private final JsonService jsonService;

    @Autowired
    public FileBasedDatabase(FileService fileServiceForData, IdProvider idProvider, JsonService jsonService) {
        this.fileServiceForData = fileServiceForData;
        this.idProvider = idProvider;
        this.jsonService = jsonService;
    }

    @Override
    public int save(Invoice invoice) {
        int id = idProvider.getNextIdAndIncrement();
        invoice.setId(id);
        fileServiceForData.appendLine(jsonService.objectToString(invoice));
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
        int lineNumber = fileServiceForData.getLineNumberById(id)
            .orElseThrow(() -> new IllegalArgumentException("There is no id : " + id));
        List<Invoice> allInvoices = getAll();
        updatedInvoice.setId(id);
        allInvoices.set(lineNumber, updatedInvoice);
        fileServiceForData.rewriteFileByList(
            allInvoices.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }

    @Override
    public void delete(int id) {
        int lineNumber = fileServiceForData.getLineNumberById(id)
            .orElseThrow(() -> new IllegalArgumentException("There is no id : " + id));
        List<Invoice> allInvoices = getAll();
        allInvoices.remove(lineNumber);
        fileServiceForData.rewriteFileByList(
            allInvoices.stream()
                .map(jsonService::objectToString)
                .collect(Collectors.toList())
        );
    }
}
