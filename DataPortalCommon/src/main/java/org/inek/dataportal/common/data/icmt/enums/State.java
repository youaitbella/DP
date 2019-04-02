package org.inek.dataportal.common.data.icmt.enums;

public enum State {

    SH(1, "Schleswig-Holstein"),
    HH(2, "Hamburg"),
    NI(3, "Niedersachsen"),
    HB(4, "Bremen"),
    NW(5, "Nordrhein-Westfalen"),
    HE(6, "Hessen"),
    RP(7, "Rheinland-Pfalz"),
    BW(8, "Baden-Württemberg"),
    BY(9, "Bayern"),
    SL(10, "Saarland"),
    BE(11, "Berlin"),
    BB(12, "Brandenburg"),
    MV(13, "Mecklenburg-Vorpommern"),
    SN(14, "Sachsen"),
    ST(15, "Sachsen-Anhalt"),
    TH(16, "Thüringen"),
    Unknown(255, "Ungültig");

    private final int _value;
    private final String _description;

    public static State fromValue (int value){
        for (State status : State.values()){
            if (status.getId() == value){return status;}
        }
        return State.Unknown;
    }

    State(int value, String description) {
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
