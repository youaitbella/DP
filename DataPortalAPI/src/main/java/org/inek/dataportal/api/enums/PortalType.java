package org.inek.dataportal.api.enums;

/**
 *
 * @author muellermi
 */
public enum PortalType {
    COMMON("Allgemein", "Allgemein"),
    BASE("Zu den Basisfunktionen", "Basisfunktionen"),
    DRG("Zum DRG-Portal", "DRG-Portal"),
    PSY("Zum PSY-Portal", "PSY-Portal"),
    INSURANCE("Zu den Krankenkassen-Funktionen", "Krankenkasse"),
    CERT("Zur Zertifizierung", "Zertifizierung"),
    CALC("Zur Teilnahme Kostenkalkulation", "Teilnahme Kostenkalkulation"),
    INEK("Zu den Internen Funktionen", "InEK Intern"),
    ADMIN("Zum Admin-Bereich", "Admin");

    private final String _displayName;
    private final String _area;

    PortalType(String displayName, String area) {
        _displayName = displayName;
        _area = area;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public String getArea() {
        return _area;
    }

}
