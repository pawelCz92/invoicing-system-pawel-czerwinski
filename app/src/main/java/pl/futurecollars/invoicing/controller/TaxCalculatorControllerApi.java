package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.service.TaxCalculatorResult;

@RequestMapping("tax")
@Api(tags = {"tax-controller"})
public interface TaxCalculatorControllerApi {

    @GetMapping(value = "/{taxIdentificationNumber}")
    @ApiOperation(value = "Calculate tax using tax identification number")
    TaxCalculatorResult calculateTaxes(@PathVariable String taxIdentificationNumber);
}
