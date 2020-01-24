package org.inek.dataportal.psy.nub.enums;

public enum PsyNubMoneyFields {
    ADD_COSTS_PERSONAL("Mehrkosten - Personalkosten"),
    ADD_COSTS_MATERIAL("Mehrkosten - Sachkosten"),
    ADD_COSTS_OTHER("Mehrkosten - Sonstige Kosten"),
    LESS_COSTS_PERSONAL("Kosteneinsparungen - Personalkosten"),
    LESS_COSTS_MATERIAL("Kosteneinsparungen - Sachkosten"),
    LESS_COSTS_OTHER("Kosteneinsparungen - Sonstige Kosten");

    private String _description;

    public String getDescription() {
        return _description;
    }

    PsyNubMoneyFields(String _description) {
        this._description = _description;
    }
}
