package org.inek.dataportal.api.enums;

import org.inek.dataportal.api.helper.FeatureMessageHandler;

public enum Feature {

    ADMIN(1, "Administration", ManagedBy.None, PortalType.ADMIN, IkReference.None, Selectable.No, Shareable.No),
    USER_MAINTENANCE(2, "Stammdaten", ManagedBy.None, PortalType.BASE, IkReference.None, Selectable.No, Shareable.No),
    NUB(3, "Neue Untersuchungs- und Behandlungsmethoden", 
            ManagedBy.None, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.Yes),
    REQUEST_SYSTEM(4, "Anfrageverfahren", ManagedBy.None, PortalType.BASE, IkReference.None, Selectable.Yes, Shareable.No),
    DROPBOX(5, "DropBox", ManagedBy.InekOrIkAdmin, PortalType.BASE, IkReference.Hospital, Selectable.Yes, Shareable.No),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren", ManagedBy.None, PortalType.PSY, IkReference.None, Selectable.Yes, Shareable.Yes),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren", ManagedBy.None, PortalType.DRG, IkReference.None, Selectable.Yes, Shareable.Yes),
    COOPERATION(8, "Kooperation", ManagedBy.None, PortalType.BASE, IkReference.None, Selectable.Yes, Shareable.No),
    MODEL_INTENTION(9, "Modellvorhaben Psy", ManagedBy.None, PortalType.PSY, IkReference.None, Selectable.Yes, Shareable.Yes),
    DOCUMENTS(10, "Dokumente", ManagedBy.None, PortalType.BASE, IkReference.None, Selectable.No, Shareable.No),
    CERT(11, "Zertifizierung", ManagedBy.InekOrIkAdmin, PortalType.CERT, IkReference.None, Selectable.Yes, Shareable.No),
    AGENCY(12, "Behörde", ManagedBy.InekOrIkAdmin, PortalType.BASE, IkReference.None, Selectable.Yes, Shareable.No),
    INSURANCE(13, "Krankenkasse", ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, Selectable.Yes, Shareable.No),
    CALCULATION_HOSPITAL(14, "Teilnahme Kostenkalkulation", 
            ManagedBy.InekOrIkAdmin, PortalType.CALC, IkReference.Hospital, Selectable.Yes, Shareable.Yes),
    SPECIFIC_FUNCTION(15, "Besondere Aufgaben", ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.Yes),
    // todo: make ADDITIONAL_COST shareable
    ADDITIONAL_COST(16, "Finanzierung von Mehrkosten", ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.No),
    // todo: make PSYCH_STAFF shareable
    PSYCH_STAFF(17, "Psych-Personalnachweis-Vereinbarung", 
            ManagedBy.InekOrIkAdmin, PortalType.PSY, IkReference.Hospital, Selectable.Yes, Shareable.No),
    // todo: make VALUATION_RATIO shareable
    VALUATION_RATIO(18, "Gezielte Absenkung", ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, Selectable.Yes, Shareable.No),
    IK_ADMIN(19, "IK-Administration", ManagedBy.None, PortalType.BASE, IkReference.None, Selectable.No, Shareable.No),
    HC_HOSPITAL(20, "Krankenhausvergleich (Funktionen für Krankenhaus)",
            ManagedBy.InekOrIkAdmin, PortalType.PSY, IkReference.Hospital, Selectable.Yes, Shareable.No),
    HC_INSURANCE(21, "Krankenhausvergleich (Funktionen für Krankenkassen)",
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.Insurance, Selectable.Yes, Shareable.No);

    Feature(int id, String description, ManagedBy managedBy, PortalType portalType,
            IkReference ikReference, Selectable selectable, Shareable shareable) {
        _id = id;
        _description = description;
        _managedBy = managedBy;
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

    private final ManagedBy _managedBy;
    
    /**
     * Approval will be replaced by rights management
     * @return 
     * @deprecated
     */
    @Deprecated
    public boolean getNeedsApproval() {
        return _managedBy == ManagedBy.InekOrIkAdmin;
    }

    public ManagedBy getManagedBy(){
        return _managedBy;
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

    public String getDescriptionText() {
        return FeatureMessageHandler.getMessageOrEmpty("description" + this.name());
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
