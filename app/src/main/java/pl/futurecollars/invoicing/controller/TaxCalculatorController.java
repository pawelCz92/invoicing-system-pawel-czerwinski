package pl.futurecollars.invoicing.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@AllArgsConstructor
@RestController
@RequestMapping("tax")
public class TaxCalculatorController {

    private final TaxCalculatorService taxCalculatorService;


}
