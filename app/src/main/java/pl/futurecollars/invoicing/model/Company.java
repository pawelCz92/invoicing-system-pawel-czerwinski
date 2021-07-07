package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Company {

    @ApiModelProperty(value = "Tax identification number", required = true, example = "421-896-78-55")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address", required = true, example = "377 Ohio Road Pulo")
    private String address;

    @ApiModelProperty(value = "Company name", required = true, example = "Voolith")
    private String name;

}
