
package org.inek.dataportal.common.enums;

/**
 *
 * @author muellermi
 */
public enum PortalType {
    COMMON("Allgemein"),
    BASE("Zu den Basisfunktionen") ,
    DRG("Zum DRG-Portal") ,
    PSY("Zum PSY-Portal"),
    INSURANCE("Zu den Krankenkassen-Funktionen"),
    CERT("Zur Zertifizierung"),
    CALC("Zur Teilnahme Kostenkalkulation"),
    INEK("Zu den Internen Funktionen"),
    ADMIN("Zum Admin-Bereich");
    
    private final String _displayName;

    PortalType(String displayName) {
        _displayName = displayName;
    }

    public String getDisplayName() {
        return _displayName;
    }

}
