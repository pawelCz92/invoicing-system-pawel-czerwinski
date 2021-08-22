package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Builder
public class InvoiceEntry {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ApiModelProperty(value = "Product/service description", required = true, example = "Fiat 126p")
    private String description;

    @ApiModelProperty(value = "Number of items", required = true, example = "1")
    private BigDecimal quantity;

    @ApiModelProperty(value = "Product/service net price", required = true, example = "750")
    @Column(name = "net_price")
    private BigDecimal price;

    @ApiModelProperty(value = "Product/service tax value", required = true, example = "250")
    private BigDecimal vatValue;

    @ApiModelProperty(value = "Tax rate", required = true)
    @Enumerated(EnumType.STRING)
    private Vat vatRate;

    @ApiModelProperty(value = "Car related", required = true)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "expense_related_to_car")
    private Car car;

    public InvoiceEntry(String description, BigDecimal quantity, BigDecimal price, BigDecimal vatValue, Vat vatRate, Car car) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
        this.car = car;
    }

}
