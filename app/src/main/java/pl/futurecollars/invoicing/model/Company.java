package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Company {

    @JsonIgnore
    private int id;

    @ApiModelProperty(value = "Tax identification number", required = true, example = "421-896-78-55")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address", required = true, example = "377 Ohio Road Pulo")
    private String address;

    @ApiModelProperty(value = "Company name", required = true, example = "Voolith")
    private String name;

    @ApiModelProperty(value = "Health insurance", required = true, example = "500")
    private BigDecimal healthInsurance;

    @ApiModelProperty(value = "Pension insurance", required = true, example = "400")
    private BigDecimal pensionInsurance;


}
