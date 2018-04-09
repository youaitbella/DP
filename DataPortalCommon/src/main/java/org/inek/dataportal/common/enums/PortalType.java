
package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum PortalType {
    COMMON("Allgemein"),
    BASE("zu den Basisfunktionen") ,
    DRG("zum DRG-Portal") ,
    PSY("zum PSY-Portal"),
    INSURANCE("zu Krankenkassen-Funktionen"),
    CERT("zur Zertifizierung"),
    CALC("zur Teilnahme Kostenkalkulation"),
    INEK("Interne Funktionen"),
    ADMIN("zum Admin-Bereich");
    
    private final String _displayName;

    PortalType(String displayName) {
        _displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

}
