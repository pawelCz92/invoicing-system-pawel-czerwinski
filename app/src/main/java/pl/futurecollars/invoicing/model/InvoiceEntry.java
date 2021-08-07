package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoiceEntry that = (InvoiceEntry) o;
        return quantity == that.quantity && Objects.equals(description, that.description) && Objects.equals(price, that.price) &&
            Objects.equals(vatValue, that.vatValue) && vatRate == that.vatRate && Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, quantity, price, vatValue, vatRate, car);
    }
}
