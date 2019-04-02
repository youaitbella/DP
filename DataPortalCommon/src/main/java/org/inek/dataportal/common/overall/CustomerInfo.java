package org.inek.dataportal.common.overall;

import org.inek.dataportal.common.data.icmt.enums.State;

public class CustomerInfo {

    CustomerInfo(String name, String town, State state, State psyState) {
        _name = name;
        _town = town;
        _state = state;
        _psyState = psyState;
    }

    private final String _name;

    public String getName() {
        return _name;
    }

    private final String _town;

    public String getTown() {
        return _town;
    }

    private final State _state;

    public State getState() {return _state; }

    private final State _psyState;

    public State getPsyState() {return _psyState; }
}
