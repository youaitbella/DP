package org.inek.dataportal.psy.nub.enums;

public enum PsyNubDateFields {
    IN_GERMANY("Wann wurde die Methode in Deutschland eingeführt"),
    MEDICAL_APPROVAL("Bei Medikamenten: Wann zugelassen"),
    INTRODUCED_HOSPITAL("Wann im Krankenhaus eingeführt");

    private String _description;

    PsyNubDateFields(String _description) {
        this._description = _description;
    }
}
