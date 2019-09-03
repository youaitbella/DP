package org.inek.dataportal.psy.nub.enums;

public enum PsyNubDateFields {
    IN_GERMANY("Eingeführt in Deutschland"),
    MEDICAL_APPROVAL("Medikamenten zulassung"),
    INTRODUCED_HOSPITAL("Eingeführt im Krankenhaus");

    private String _description;

    PsyNubDateFields(String _description) {
        this._description = _description;
    }

    public String getDescription() {
        return _description;
    }
}
