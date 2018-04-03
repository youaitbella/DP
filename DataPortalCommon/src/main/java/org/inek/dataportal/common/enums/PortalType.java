
package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum PortalType {
    COMMON("Allgemein"),
    BASE("Basisfunktionen") ,
    DRG("DRG-Portal") ,
    PSY("PSY-Portal"),
    INSURANCE("Krankenkassen-Funktionen"),
    CERT("Zertifizierung"),
    CALC("Teilnahme Kostenkalkulation"),
    INEK("Interne Funktionen"),
    ADMIN("Admin-Bereich");
    
    private final String _displayName;

    PortalType(String displayName) {
        _displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

}
