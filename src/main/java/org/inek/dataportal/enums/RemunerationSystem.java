package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum RemunerationSystem {

    Unknown(-1, "???"),
    DRG(0, "G-DRG"),
    PEPP(1, "PEPP");

    private final int _id;
    private final String _name;
    private RemunerationSystem(int id, String name) {
        _id = id;
        _name = name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public static RemunerationSystem fromId(int value) {
        for (RemunerationSystem system : RemunerationSystem.values()) {
            if (system.getId() == value) {
                return system;
            }
        }
        return RemunerationSystem.Unknown;
    }

}
