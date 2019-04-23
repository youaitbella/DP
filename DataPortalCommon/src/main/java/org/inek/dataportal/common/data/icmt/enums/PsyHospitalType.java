package org.inek.dataportal.common.data.icmt.enums;

public enum PsyHospitalType {

    Unknown(0, "Unbekannter Krankenhaustyp"),
    FAB(1, "Fachabteilung Psychiatrie / Psychosomatik"),
    FACH(2, "Fachkrankenhaus Psychiatrie / Psychosomatik");

    private final int _value;
    private final String _description;

    public static PsyHospitalType fromValue (int value){
        for (PsyHospitalType status : PsyHospitalType.values()){
            if (status.getId() == value){return status;}
        }
        return PsyHospitalType.Unknown;
    }

    PsyHospitalType(int value, String description) {
        _value = value;
        _description = description;
    }

    public int getId() {
        return _value;
    }

    public int getValue() {
        return _value;
    }

    public String getDescription() {
        return _description;
    }
}
