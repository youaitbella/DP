package org.inek.dataportal.psy.nub.enums;

public enum PsyNubNumberFields {
    USED_HOSPITALS("In wie vielen kliniken wird diese Methode eingesetzt"),
    PATIENTS_PRE_PRE_TARGETYEAR("Patienten in Vorjahr"),
    PATIENTS_PRE_TARGETYEAR("Patienten im aktuellem Jahr"),
    PATIENTS_TARGETYEAR("Patienten im nächsten Jahr");

    private String _description;

    PsyNubNumberFields(String _description) {
        this._description = _description;
    }

    public String getDescription() {
        return _description;
    }
}
