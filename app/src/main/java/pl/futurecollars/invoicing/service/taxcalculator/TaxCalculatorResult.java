package pl.futurecollars.invoicing.service.taxcalculator;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaxCalculatorResult {

    private BigDecimal income;
    private BigDecimal costs;
    private BigDecimal earnings;

    private BigDecimal incomingVat;
    private BigDecimal outgoingVat;
    private BigDecimal vatToReturn;

    private BigDecimal earningsMinusPensionInsurance;
    private BigDecimal taxCalculationBase;
    private BigDecimal incomeTax;

    private BigDecimal pensionInsurance;
    private BigDecimal healthInsurance;

    private BigDecimal reducedHealthInsurance;
    private BigDecimal incomeTaxMinusHealthInsurance;
    private BigDecimal finalIncomeTax;
}
