package org.inek.dataportal.care.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<Shift> orderedValues() {
        return Arrays.stream(Shift.values()).sorted(Comparator.comparing(Shift::getName)).collect(Collectors.toList());
    }

    public static Shift getByName(String name) {
        for (Shift shift : Shift.values()) {
            if (shift.getName().toUpperCase().equals(name.toUpperCase())) {
                return shift;
            }
        }
        return null;
    }
}
