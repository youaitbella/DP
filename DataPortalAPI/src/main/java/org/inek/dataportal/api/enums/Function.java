package org.inek.dataportal.api.enums;

public enum Function {
    NONE(0),
    STRUCTURAL_CHANGES(10 * Feature.CARE.getId() + 1);

    private int id;

    Function(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Function fromId(int functionId) {
        for (Function function : Function.values()) {
            if (function.getId() == functionId) {
                return function;
            }
        }
        return Function.NONE;
    }
}
