package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InvoiceEntry {

    @JsonIgnore
    private int id;

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

    public InvoiceEntry(String description, int quantity, BigDecimal price, BigDecimal vatValue, Vat vatRate, Car car) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
        this.car = car;
    }
    public InvoiceEntry(String description, int quantity, double price, double vatValue, Vat vatRate, Car car) {
        this.description = description;
        this.quantity = quantity;
        this.price = BigDecimal.valueOf(price);
        this.vatValue = BigDecimal.valueOf(vatValue);
        this.vatRate = vatRate;
        this.car = car;
    }
}
