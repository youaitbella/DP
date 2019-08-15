package org.inek.dataportal.psy.nub.enums;

public enum PsyNubNumberFields {
    USED_HOSPITALS("In wie vielen kliniken wird diese methode zurzeit eingesetzt"),
    PATIENTS_PRE_PRE_TARGETYEAR("Patienten in Targetyear - 2"),
    PATIENTS_PRE_TARGETYEAR("Patienten in Targetyear - 1"),
    PATIENTS_TARGETYEAR("Patienten in Targetyear");

    private String _description;

    PsyNubNumberFields(String _description) {
        this._description = _description;
    }
}
