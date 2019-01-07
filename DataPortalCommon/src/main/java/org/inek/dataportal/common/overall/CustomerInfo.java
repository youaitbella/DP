package org.inek.dataportal.common.overall;

public class CustomerInfo {

    CustomerInfo(String name, String town) {
        _name = name;
        _town = town;
    }

    private final String _name;

    public String getName() {
        return _name;
    }

    private final String _town;

    public String getTown() {
        return _town;
    }
}
