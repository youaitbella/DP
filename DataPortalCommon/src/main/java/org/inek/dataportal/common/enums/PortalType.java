
package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum PortalType {
    COMMON("Allgemein"),
    DRG("DRG-Portal") ,
    PSY("PSY-Portal"),
    INSURANCE("Krankenkassen-Funktionen"),
    ADMIN("Admin-Bereich");
    
    private final String _displayName;

    PortalType(String displayName) {
        _displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

}
