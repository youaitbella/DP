package org.inek.dataportal.care.enums;

public enum StructuralChangesType {
    NEW(1),
    CHANGE(2),
    CLOSE(3),
    COMBINE_WITH_NEW(4),
    UNKNOWN(99);

    private int _id;

    public int getId() {
        return _id;
    }

    StructuralChangesType(int id) {
        _id = id;
    }

    public static StructuralChangesType getById(int id) {
        for (StructuralChangesType value : StructuralChangesType.values()) {
            if (value._id == id) {
                return value;
            }
        }
        return StructuralChangesType.UNKNOWN;
    }
}
