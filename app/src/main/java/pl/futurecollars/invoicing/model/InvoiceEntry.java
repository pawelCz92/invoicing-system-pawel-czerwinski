package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;
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
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoiceEntry that = (InvoiceEntry) o;
        boolean quantityCompare = false;
        if (!Objects.isNull(this.quantity) && !Objects.isNull(that.quantity)){
           quantityCompare = this.quantity.compareTo(that.quantity) == 0;
        }
        return Objects.equals(description, that.description) && quantityCompare &&
            Objects.equals(price, that.price) && Objects.equals(vatValue, that.vatValue) && vatRate == that.vatRate &&
            Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, quantity, price, vatValue, vatRate, car);
    }
}
