package org.inek.dataportal.care.enums;

public enum SensitiveArea {
    INTENSIVMEDIZIN(1, "Intensivmedizin"),
    GERIATRIE(2, "Geriatrie"),
    HERZCHIRUGIE(3, "Herzchirurgie"),
    UNFALLCHIRUGIE(4, "Unfallchirurgie"),
    KARDIOLOGIE(5, "Kardiologie"),
    NEUROLOGIE(6, "Neurologie");

    private int _id;
    private String _name;

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    SensitiveArea(int id, String name) {
        _id = id;
        _name = name;
    }

    public static SensitiveArea getById(int id) {
        for (SensitiveArea sArea : SensitiveArea.values()) {
            if (sArea.getId() == id) {
                return sArea;
            }
        }
        return null;
    }
}
