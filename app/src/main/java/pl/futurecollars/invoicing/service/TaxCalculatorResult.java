package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class TaxCalculatorResult {

    private final BigDecimal income;
    private final BigDecimal costs;
    private final BigDecimal earnings;

    private final BigDecimal incomingVat;
    private final BigDecimal outgoingVat;
    private final BigDecimal vatToReturn;

    private final BigDecimal earningsMinusPensionInsurance;
    private final BigDecimal taxCalculationBase;
    private final BigDecimal incomeTax;

    private final BigDecimal pensionInsurance;
    private final BigDecimal healthInsurance;

    private final BigDecimal reducedHealthInsurance;
    private final BigDecimal incomeTaxMinusHealthInsurance;
    private final BigDecimal finalIncomeTax;
}
