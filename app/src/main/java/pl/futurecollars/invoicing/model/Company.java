package pl.futurecollars.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
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
