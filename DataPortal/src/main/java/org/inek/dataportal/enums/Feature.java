package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum Feature {

    ADMIN(1, "Administration", false),
    USER_MAINTENANCE(2, "Stammdaten", false),
    NUB(3, "Neue Untersuchungs- und Behandlungsmethoden", false),
    DROPBOX(5, "DropBox", true),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren", false),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren", false),
    COOPERATION(8, "Kooperation", false),
    MODEL_INTENTION(9, "Modellvorhaben Psy", false),
    DOCUMENTS(10, "Dokumente", false),
    CERT(11, "Zertifizierung", true), 
    AGENCY(12, "Behörde", true),
    INSURANCE(13, "Krankenkasse", true),
    CALCULATION_HOSPITAL(14, "Teilnahme Kostenkalkulation", true),
    SPECIFIC_FUNCTION(15, "Besondere Aufgaben", true);

    Feature(int id, String description, boolean needsApproval) {
        _id = id;
        _description = description;
        _needsApproval = needsApproval;
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

}
