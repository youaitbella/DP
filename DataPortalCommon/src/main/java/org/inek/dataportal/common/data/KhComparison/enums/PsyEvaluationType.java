package org.inek.dataportal.common.data.KhComparison.enums;

public enum PsyEvaluationType {
    Type_1(1, "Gruppe a; Jahresübergreifend"),
    Type_2(2, "Gruppe a; Vereinbarungsjahr - 1"),
    Type_3(3, "Gruppe a; Vereinbarungsjahr - 2"),
    Type_4(4, "Gruppe b; Jahresübergreifend"),
    Type_5(5, "Gruppe b; Vereinbarungsjahr - 1"),
    Type_6(6, "Gruppe b; Vereinbarungsjahr - 2"),
    Type_7(7, "Gruppe c; Jahresübergreifend"),
    Type_8(8, "Gruppe c; Vereinbarungsjahr - 1"),
    Type_9(9, "Gruppe c; Vereinbarungsjahr - 2");

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
