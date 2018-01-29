package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum Feature {

    ADMIN(1, "Administration", NeedApproval.No, PortalType.DRG, IkReference.None, Shareable.No),
    USER_MAINTENANCE(2, "Stammdaten", NeedApproval.No, PortalType.COMMON, IkReference.None, Shareable.No),
    NUB(3, "Neue Untersuchungs- und Behandlungsmethoden", NeedApproval.No, PortalType.DRG, IkReference.Hospital, Shareable.Yes),
    REQUEST_SYSTEM(4, "Anfrageverfahren", NeedApproval.No, PortalType.DRG, IkReference.None, Shareable.No),
    DROPBOX(5, "DropBox", NeedApproval.Yes, PortalType.COMMON, IkReference.Hospital, Shareable.No),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren", NeedApproval.No, PortalType.PSY, IkReference.None, Shareable.Yes),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren", NeedApproval.No, PortalType.DRG, IkReference.None, Shareable.Yes),
    COOPERATION(8, "Kooperation", NeedApproval.No, PortalType.COMMON, IkReference.None, Shareable.No),
    MODEL_INTENTION(9, "Modellvorhaben Psy", NeedApproval.No, PortalType.PSY, IkReference.None, Shareable.Yes),
    DOCUMENTS(10, "Dokumente", NeedApproval.No, PortalType.COMMON, IkReference.None, Shareable.No), // todo: make shareable
    CERT(11, "Zertifizierung", NeedApproval.Yes, PortalType.DRG, IkReference.None, Shareable.No),
    AGENCY(12, "Behörde", NeedApproval.Yes, PortalType.DRG, IkReference.None, Shareable.No),
    INSURANCE(13, "Krankenkasse", NeedApproval.Yes, PortalType.DRG, IkReference.None, Shareable.No),
    CALCULATION_HOSPITAL(14, "Teilnahme Kostenkalkulation", NeedApproval.Yes, PortalType.COMMON, IkReference.Hospital, Shareable.Yes),
    SPECIFIC_FUNCTION(15, "Besondere Aufgaben", NeedApproval.Yes, PortalType.DRG, IkReference.Hospital, Shareable.Yes),
    ADDITIONAL_COST(16, "Finanzierung von Mehrkosten", NeedApproval.Yes, PortalType.DRG, IkReference.Hospital, Shareable.No), // todo: make shareable
    PSYCH_STAFF(17, "Psych-Personalnachweis-Vereinbarung", NeedApproval.Yes, PortalType.PSY, IkReference.Hospital, Shareable.No), // todo: shareable
    VALUATION_RATIO(18, "Gezielte Absenkung", NeedApproval.Yes, PortalType.DRG, IkReference.Hospital, Shareable.No), // todo: make shareable
    IK_ADMIN(19, "IK-Administration", NeedApproval.No, PortalType.COMMON, IkReference.None, Shareable.No);

    Feature(int id, String description, NeedApproval needApproval, PortalType portalType, IkReference ikReference, Shareable shareable) {
        _id = id;
        _description = description;
        _needApproval = needApproval;
        _portalType = portalType;
        _ikReference = ikReference;
        _shareable = shareable;
    }

    private final int _id;

    public int getId() {
        return _id;
    }

    private final String _description;

    public String getDescription() {
        return _description;
    }

    private final NeedApproval _needApproval;

    public boolean needsApproval() {
        return _needApproval == NeedApproval.Yes;
    }

    private final PortalType _portalType;

    public PortalType getPortalType() {
        return _portalType;
    }

    private final IkReference _ikReference;

    public IkReference getIkReference() {
        return _ikReference;
    }

    private final Shareable _shareable;

    public boolean isShareable() {
        return _shareable == Shareable.Yes;
    }

    public static Feature getFeatureFromId(int id) {
        for (Feature feature : Feature.values()) {
            if (feature.getId() == id) {
                return feature;
            }
        }
        throw new IllegalArgumentException("Failed to obtain feature. Unknown id " + id);
    }

}
