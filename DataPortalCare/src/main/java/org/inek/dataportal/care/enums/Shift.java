package org.inek.dataportal.care.enums;

public enum Shift {
    NIGHT(0, "Nacht"),
    DAY(1, "Tag");

    private int _id;
    private String _name;

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    Shift(int id, String name) {
        _id = id;
        _name = name;
    }

    public static Shift getById(int id) {
        for (Shift shift : Shift.values()) {
            if (shift.getId() == id) {
                return shift;
            }
        }
        return null;
    }
}
