package org.inek.dataportal.common.enums;


/**
 *
 * @author muellermi
 */
public enum Feature {

    ADMIN(1, "Administration", NeedApproval.No, PortalType.ADMIN, IkReference.None, Selectable.No, Shareable.No),
    USER_MAINTENANCE(2, "Stammdaten", NeedApproval.No, PortalType.COMMON, IkReference.None, Selectable.No, Shareable.No),
    NUB(3, "Neue Untersuchungs- und Behandlungsmethoden", NeedApproval.No, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.Yes),
    REQUEST_SYSTEM(4, "Anfrageverfahren", NeedApproval.No, PortalType.DRG, IkReference.None, Selectable.Yes, Shareable.No),
    DROPBOX(5, "DropBox", NeedApproval.Yes, PortalType.COMMON, IkReference.None, Selectable.Yes, Shareable.No),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren", NeedApproval.No, PortalType.PSY, IkReference.None, Selectable.Yes, Shareable.Yes),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren", NeedApproval.No, PortalType.DRG, IkReference.None, Selectable.Yes, Shareable.Yes),
    COOPERATION(8, "Kooperation", NeedApproval.No, PortalType.COMMON, IkReference.None, Selectable.Yes, Shareable.No),
    MODEL_INTENTION(9, "Modellvorhaben Psy", NeedApproval.No, PortalType.PSY, IkReference.None, Selectable.Yes, Shareable.Yes),
    DOCUMENTS(10, "Dokumente", NeedApproval.No, PortalType.COMMON, IkReference.None, Selectable.No, Shareable.No),
    CERT(11, "Zertifizierung", NeedApproval.Yes, PortalType.CERT, IkReference.None, Selectable.Yes, Shareable.No),
    AGENCY(12, "Behörde", NeedApproval.Yes, PortalType.COMMON, IkReference.None, Selectable.Yes, Shareable.No),
    INSURANCE(13, "Krankenkasse", NeedApproval.Yes, PortalType.INSURANCE, IkReference.None, Selectable.Yes, Shareable.No),
    CALCULATION_HOSPITAL(14, "Teilnahme Kostenkalkulation", NeedApproval.Yes, PortalType.COMMON, IkReference.Hospital, Selectable.Yes, Shareable.Yes),
    SPECIFIC_FUNCTION(15, "Besondere Aufgaben", NeedApproval.Yes, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.Yes),
    // todo: make ADDITIONAL_COST shareable
    ADDITIONAL_COST(16, "Finanzierung von Mehrkosten", NeedApproval.Yes, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.No),
    // todo: make PSYCH_STAFF shareable
    PSYCH_STAFF(17, "Psych-Personalnachweis-Vereinbarung", NeedApproval.Yes, PortalType.PSY, IkReference.Hospital, Selectable.Yes, Shareable.No),
    // todo: make VALUATION_RATIO shareable
    VALUATION_RATIO(18, "Gezielte Absenkung", NeedApproval.Yes, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.No),
    IK_ADMIN(19, "IK-Administration", NeedApproval.No, PortalType.COMMON, IkReference.None, Selectable.No, Shareable.No),
    PORTAL_TYPE_HOLDER(20, "Portal-Navigation", NeedApproval.No, PortalType.COMMON, IkReference.None, Selectable.No, Shareable.No);

    Feature(int id, String description, NeedApproval needApproval, PortalType portalType, 
            IkReference ikReference, Selectable selectable, Shareable shareable) {
        _id = id;
        _description = description;
        _needApproval = needApproval;
        _portalType = portalType;
        _ikReference = ikReference;
        _selectable = selectable;
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

    private final Selectable _selectable;

    public boolean isSelectable() {
        return _selectable == Selectable.Yes;
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
