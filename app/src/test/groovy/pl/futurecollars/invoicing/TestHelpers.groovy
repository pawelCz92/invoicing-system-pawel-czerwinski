package pl.futurecollars.invoicing


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

}
