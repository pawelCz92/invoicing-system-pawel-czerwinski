package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Car {

    @JsonIgnore
    private int id;
    @ApiModelProperty (value = "Includes private use", required = true, example = "true")
    private boolean isIncludingPrivateExpense;
    @ApiModelProperty (value = "Car registration number", required = true, example = "FGH-1462")
    private String registration;
}
