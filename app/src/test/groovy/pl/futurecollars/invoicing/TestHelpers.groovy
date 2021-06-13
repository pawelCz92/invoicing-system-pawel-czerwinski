package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Specification

class TestHelpers extends Specification {

    def "should return suitable values of Vat"() {
        expect:
        Vat.VAT_23.getRate() == 23
        Vat.VAT_5.getRate() == 5
        Vat.VAT_0.getRate() == 0
    }

    def "should return suitable description for Vat"(Vat vat, String description) {
        expect:
        vat.toString() == description

        where:
        vat        | description
        Vat.VAT_23 | "Vat.VAT_23(rate=23)"
        Vat.VAT_5  | "Vat.VAT_5(rate=5)"
        Vat.VAT_0  | "Vat.VAT_0(rate=0)"
    }

    def "Company toString method check"(String taxIdentificationNumber, String address,
                                        String name, String toStringResult) {
        given:
        Company company = Company.builder()
                .taxIdentificationNumber(taxIdentificationNumber)
                .address(address)
                .name(name)
                .build()

        and: "dump call for cover Company.builder.toString method (generated by lombok)"
        Company.builder().toString()

        expect:
        company.toString() == toStringResult

        where:
        taxIdentificationNumber | address                | name        | toStringResult
        "654654654"             | "ul Jakaś Tam, Poznan" | "Biedronka" | "Company(taxIdentificationNumber=654654654, address=ul Jakaś Tam, Poznan, name=Biedronka)"
        "856565545"             | "ul Inna, Poznan"      | "Lidl"      | "Company(taxIdentificationNumber=856565545, address=ul Inna, Poznan, name=Lidl)"
    }

}
