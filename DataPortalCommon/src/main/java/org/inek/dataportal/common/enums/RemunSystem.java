package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum RemunSystem {

    Unknown(-1, ""),
    DRG(0, "G-DRG-System"),
    PEPP(1, "PEPP-Entgeltsystem");

    private final int _id;
    private final String _name;
    RemunSystem(int id, String name) {
        _id = id;
        _name = name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public static RemunSystem fromId(int value) {
        for (RemunSystem system : RemunSystem.values()) {
            if (system.getId() == value) {
                return system;
            }
        }
        return RemunSystem.Unknown;
    }

}
