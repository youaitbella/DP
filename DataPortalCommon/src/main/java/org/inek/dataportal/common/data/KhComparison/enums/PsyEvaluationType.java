package org.inek.dataportal.common.data.KhComparison.enums;

public enum PsyEvaluationType {
    Type_1(1, "Bundesland, Fachgebiet; Jahresübergreifend"),
    Type_2(2, "Bundesland, Fachgebiet; Vereinbarungsjahr - 1"),
    Type_3(3, "Bundesland, Fachgebiet; Vereinbarungsjahr - 2"),
    Type_4(4, "Bundesland; Jahresübergreifend"),
    Type_5(5, "Bundesland; Vereinbarungsjahr - 1"),
    Type_6(6, "Bundesland; Vereinbarungsjahr - 2"),
    Type_7(7, "Deutschland, Fachgebiet; Jahresübergreifend"),
    Type_8(8, "Deutschland, Fachgebiet; Vereinbarungsjahr - 1"),
    Type_9(9, "Deutschland, Fachgebiet; Vereinbarungsjahr - 2");

    private int _id;
    private String _description;

    PsyEvaluationType(int id, String description) {
        _id = id;
        _description = description;
    }

    public int getId() {
        return _id;
    }

    public String getDescription() {
        return _description;
    }

    public static PsyEvaluationType findById(int id) {
        for (PsyEvaluationType value : PsyEvaluationType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("PsyEvaluationType not found for id: " + id);
    }
}
