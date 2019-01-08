package org.inek.dataportal.care.enums;

public enum Months {
    JANUARY(1, "Januar"),
    FEBRUARY(2, "Februar"),
    MARCH(3, "März"),
    APRIL(4, "April"),
    MAY(5, "Mai"),
    JUNE(6, "Juni"),
    JULY(7, "Juli"),
    AUGUST(8, "August"),
    SEPTEMBER(9, "September"),
    OCTOBER(10, "Oktober"),
    NOVEMBER(11, "November"),
    DECEMBER(12, "Dezember");

    private int _id;
    private String _name;

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    Months(int id, String name) {
        _id = id;
        _name = name;
    }

    public static Months getById(int id) {
        for (Months mon : Months.values()) {
            if (mon.getId() == id) {
                return mon;
            }
        }
        return null;
    }

    public static  Months getByName(String name) {
        for (Months month : Months.values()) {
            if (month.getName().toUpperCase().equals(name.toUpperCase())) {
                return month;
            }
        }
        return null;
    }
}
