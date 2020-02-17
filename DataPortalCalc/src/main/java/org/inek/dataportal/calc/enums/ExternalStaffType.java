package org.inek.dataportal.calc.enums;

public enum ExternalStaffType {
    NURSINSTAFF(1, "NursingStaff"),
    ASSISTANTNURSINSTAFF(2,"AssistantNursingStaff"),
    OTHERNURSINSTAFF(3,"OtherNursingStaff");

    private int _id;
    String _name;

    ExternalStaffType(int _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public static ExternalStaffType getById(int _id) {
        for (ExternalStaffType externalStaffType:ExternalStaffType.values()){
            if(externalStaffType.getId() == _id){
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