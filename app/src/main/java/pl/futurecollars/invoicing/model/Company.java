package pl.futurecollars.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Builder
@AllArgsConstructor
@ToString
public class Company {

    private String taxIdentificationNumber;
    private String address;
    private String name;
}
