package org.inek.dataportal.psy.nub.enums;

public enum PsyNubDateFields {
    IN_GERMANY("In wie vielen kliniken wird diese methode zurzeit eingesetzt"),
    MEDICAL_APPROVAL("Patienten in Targetyear - 2"),
    INTRODUCED_HOSPITAL("Patienten in Targetyear - 1");

    private String _description;

    PsyNubDateFields(String _description) {
        this._description = _description;
    }
}
