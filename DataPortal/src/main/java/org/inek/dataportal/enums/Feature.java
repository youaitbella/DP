package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum Feature {

    ADMIN(1, "Administration", false, PortalType.DRG),
    USER_MAINTENANCE(2, "Stammdaten", false, PortalType.COMMON),
    NUB(3, "Neue Untersuchungs- und Behandlungsmethoden", false, PortalType.DRG),
    DROPBOX(5, "DropBox", true, PortalType.COMMON),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren", false, PortalType.PSY),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren", false, PortalType.DRG),
    COOPERATION(8, "Kooperation", false, PortalType.COMMON),
    MODEL_INTENTION(9, "Modellvorhaben Psy", false, PortalType.PSY),
    DOCUMENTS(10, "Dokumente", false, PortalType.COMMON),
    CERT(11, "Zertifizierung", true, PortalType.DRG), 
    AGENCY(12, "Behörde", true, PortalType.DRG),
    INSURANCE(13, "Krankenkasse", true, PortalType.DRG),
    CALCULATION_HOSPITAL(14, "Teilnahme Kostenkalkulation", true, PortalType.COMMON),
    SPECIFIC_FUNCTION(15, "Besondere Aufgaben", true, PortalType.DRG), 
    ADDITIONAL_COST(16, "Finanzierung von Mehrkosten", true, PortalType.DRG),
    PSYCH_STAFF(17, "Psych-Personalnachweis-Vereinbarung", true, PortalType.PSY),
    I68(18, "I68 Absenkung Bewertungsrelation", false, PortalType.DRG);

    Feature(int id, String description, boolean needsApproval, PortalType portalType) {
        _id = id;
        _description = description;
        _needsApproval = needsApproval;
        _portalType = portalType;
    }

    private final int _id;

    public int getId() {
        return _id;
    }

    private final String _description;

    public String getDescription() {
        return _description;
    }

    private final boolean _needsApproval;
    public boolean needsApproval() {
        return _needsApproval;
    }

    private final PortalType _portalType;

    public PortalType getPortalType() {
        return _portalType;
    }
}
