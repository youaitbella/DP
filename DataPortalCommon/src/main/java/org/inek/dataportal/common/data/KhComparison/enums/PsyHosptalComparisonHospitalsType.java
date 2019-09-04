package org.inek.dataportal.common.data.KhComparison.enums;

public enum PsyHosptalComparisonHospitalsType {
    Hospital("Hospital"),
    Group("Group"),
    Unknown("Unknown");

    private String _value;

    PsyHosptalComparisonHospitalsType(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public static PsyHosptalComparisonHospitalsType getByValue(String value) {
        for (PsyHosptalComparisonHospitalsType psyHosptalComparisonHospitalsType : PsyHosptalComparisonHospitalsType.values()) {
            if (psyHosptalComparisonHospitalsType.getValue().equals(value)) {
                return psyHosptalComparisonHospitalsType;
            }
        }
        return PsyHosptalComparisonHospitalsType.Unknown;
    }
}
