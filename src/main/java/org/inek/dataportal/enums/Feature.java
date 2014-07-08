package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum Feature {

    ADMIN(1, "Administration"),
    USER_MAINTENANCE(2, "Stammdaten"),
    NUB(3, "Neue Untersuchungs- und Behandlungsmethoden"),
    REQUEST_SYSTEM(4, "Anfrageverfahren"),
    DROPBOX(5, "DropBox"),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren"),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren"),
    COOPERATION(8, "Kooperation"),
    MODEL_INTENTION(9, "Modellvorhaben Psy"),
    DOCUMENTS(10, "Dokumente"),
    ZERTI(11, "Zertifizierung");

    private Feature(int id, String description) {
        _id = id;
        _description = description;
    }

    private final int _id;

    public int getId() {
        return _id;
    }

    private final String _description;

    public String getDescription() {
        return _description;
    }

}
