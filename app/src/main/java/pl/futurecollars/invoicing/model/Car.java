package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Car {

    @ApiModelProperty (value = "Includes private use", required = true, example = "true")
    private boolean isIncludingPrivateExpense;
    @ApiModelProperty (value = "Car registration number", required = true, example = "FGH-1462")
    private String registration;
}
