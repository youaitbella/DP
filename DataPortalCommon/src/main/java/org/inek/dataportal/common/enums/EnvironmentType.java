package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum EnvironmentType {
    UA("UserAgent"),
    SR("ScreenResolution");

    String _description;
    EnvironmentType(String description) {
        _description = description;
    }
    
    public String getDescription(){
        return _description;
    }
}
