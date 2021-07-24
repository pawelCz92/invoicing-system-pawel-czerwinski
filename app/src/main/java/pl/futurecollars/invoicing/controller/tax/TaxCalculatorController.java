package pl.futurecollars.invoicing.controller.tax;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.taxcalculator.TaxCalculatorResult;
import pl.futurecollars.invoicing.service.taxcalculator.TaxCalculatorService;

@AllArgsConstructor
@RestController
public class TaxCalculatorController implements TaxCalculatorControllerApi {

    private final TaxCalculatorService taxCalculatorService;

    @Override
    public TaxCalculatorResult calculateTaxes(@RequestBody Company company) {
        return taxCalculatorService.calculateTaxes(company);
    }
}
