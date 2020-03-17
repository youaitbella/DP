package org.inek.dataportal.calc.enums;

public enum ExternalStaffType {
    NURSING(1, "NURSING"),
    ASSISTANTNURSING(2, "ASSISTANTNURSING"),
    OTHERNURSING(3, "OTHERNURSING");

    private int _id;
    private String _name;

    ExternalStaffType(int id, String name) {
        this._id = id;
        this._name = name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public static ExternalStaffType fromId(int id) {
        for (ExternalStaffType externalStaffType : ExternalStaffType.values()) {
            if (externalStaffType.getId() == id) {
                return externalStaffType;
            }
        }
        return null;
    }

    public static ExternalStaffType getByName(String _name) {
        for (ExternalStaffType externalStaffType:ExternalStaffType.values()) {
            if(externalStaffType.getName().toUpperCase().equals(_name.toUpperCase())){
                return externalStaffType;
            }
        }
        return null;
    }
}
