package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "invoice_entries")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InvoiceEntry {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
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
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Car car;

    public InvoiceEntry(String description, int quantity, BigDecimal price, BigDecimal vatValue, Vat vatRate, Car car) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
        this.car = car;
    }
}
