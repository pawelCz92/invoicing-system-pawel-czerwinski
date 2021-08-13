package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "cars")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class Car {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private int id;

    @ApiModelProperty(value = "Includes private use", required = true, example = "true")
    private boolean isIncludingPrivateExpense;

    @ApiModelProperty(value = "Car registration number", required = true, example = "FGH-1462")
    private String registration;

    public Car(boolean isIncludingPrivateExpense, String registration) {
        this.isIncludingPrivateExpense = isIncludingPrivateExpense;
        this.registration = registration;
    }
}
