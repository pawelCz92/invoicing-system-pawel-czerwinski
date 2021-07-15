package pl.futurecollars.invoicing.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.service.TaxCalculatorResult;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@AllArgsConstructor
@RestController
public class TaxCalculatorController implements TaxCalculatorControllerApi {

    private final TaxCalculatorService taxCalculatorService;

    @Override
    public TaxCalculatorResult calculateTaxes(@PathVariable String taxIdentificationNumber) {
        return taxCalculatorService.calculateTaxes(taxIdentificationNumber);
    }
}
