package org.inek.dataportal.care.enums;

public enum StructuralChangesDetailType {
    REASON1(1, "Grund 1"),
    REASON2(2, "Grund 2"),
    REASON3(3, "Grund 3");

    private int _id;
    private String _value;

    public int getId() {
        return _id;
    }

    public String getValue() {
        return _value;
    }

    StructuralChangesDetailType(int id, String value) {
        _id = id;
        _value = value;
    }

    public static StructuralChangesDetailType getById(int id) {
        for (StructuralChangesDetailType value : StructuralChangesDetailType.values()) {
            if (value._id == id) {
                return value;
            }
        }
        return StructuralChangesDetailType.REASON1;
    }
}
