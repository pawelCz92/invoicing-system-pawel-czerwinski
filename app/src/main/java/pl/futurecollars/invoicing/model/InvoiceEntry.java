package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class InvoiceEntry {

    @ApiModelProperty(value = "Product/service description", required = true, example = "Fiat 126p")
    private String description;

    @ApiModelProperty(value = "Number of items", required = true, example = "1")
    private int quantity;

    @ApiModelProperty(value = "Product/service net price", required = true, example = "750")
    private BigDecimal price;

    @ApiModelProperty(value = "Product/service tax value", required = true, example = "250")
    private BigDecimal vatValue;

    @ApiModelProperty(value = "Tax rate", required = true)
    private Vat vatRate;

    @ApiModelProperty(value = "Car related", required = true)
    private Car car;

    public InvoiceEntry(String description, int quantity, BigDecimal price, BigDecimal vatValue, Vat vatRate) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
    }
}
