package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Invoice {

    @ApiModelProperty(value = "Invoice id (generated by application)", example = "1")
    private int id;

    @ApiModelProperty(value = "Invoice number (assigned by user)", required = true, example = "2021/05/09/0000001")
    private String number;

    @ApiModelProperty(value = "Date invoice was created", required = true)
    private LocalDate date;

    @ApiModelProperty(value = "Company who bought the product/service", required = true)
    private Company buyer;

    @ApiModelProperty(value = "Company who sold product/service", required = true)
    private Company seller;

    @ApiModelProperty(value = "List of products/service", required = true)
    private List<InvoiceEntry> invoiceEntries;


}
