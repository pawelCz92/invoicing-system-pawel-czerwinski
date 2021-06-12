package pl.futurecollars.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Company {

    private String taxIdentificationNumber;
    private String address;
    private String name;

    @Override
    public String toString() {
        return "Company{"
            + "TIN='" + taxIdentificationNumber + '\''
            + ", address='" + address + '\''
            + ", name='" + name + '\''
            + '}';
    }
}
