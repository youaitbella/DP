package org.inek.dataportal.care.enums;

public enum SensitiveArea {
    INVALID(-1, "---", false),
    INTENSIVMEDIZIN(1, "Intensivmedizin", false),
    GERIATRIE(2, "Geriatrie", true),
    HERZCHIRUGIE(3, "Herzchirurgie", true),
    UNFALLCHIRUGIE(4, "Unfallchirurgie", true),
    KARDIOLOGIE(5, "Kardiologie", true),
    NEUROLOGIE(6, "Neurologie", true),
    NEURO_STROKE(7, "Neurologische Schlaganfalleinheit", false),
    NEURO_FRUH(8, "Neurologische Frührehabilitation", false);

    private int _id;
    private String _name;
    private boolean _fabRequired;

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public boolean isFabRequired() {
        return _fabRequired;
    }

    SensitiveArea(int id, String name, boolean fabRequired) {
        _id = id;
        _name = name;
        _fabRequired = fabRequired;
    }

    public static SensitiveArea getById(int id) {
        for (SensitiveArea sArea : SensitiveArea.values()) {
            if (sArea.getId() == id) {
                return sArea;
            }
        }
        return SensitiveArea.INVALID;
    }

    public static  SensitiveArea getByName(String name) {
        for (SensitiveArea sArea : SensitiveArea.values()) {
            if (sArea.getName().toUpperCase().equals(name.toUpperCase())) {
                return sArea;
            }
        }
        return SensitiveArea.INVALID;
    }
}
