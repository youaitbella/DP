package org.inek.dataportal.api.enums;

import org.inek.dataportal.api.helper.FeatureMessageHandler;

public enum Feature {

    UNKNOWN(0, "<unknown>", ManagedBy.None, PortalType.COMMON, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No),
    ADMIN(1, "Administration", ManagedBy.None, PortalType.ADMIN, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No),
    USER_MAINTENANCE(2, "Stammdaten", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No),
    NUB(3, "Neue Untersuchungs- und Behandlungsmethoden", 
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes),
    REQUEST_SYSTEM(4, "Anfrageverfahren", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No),
    DROPBOX(5, "DropBox", ManagedBy.InekOrIkAdmin, PortalType.BASE, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.No),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren", ManagedBy.None, PortalType.PSY, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.Yes),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren", ManagedBy.None, PortalType.DRG, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.Yes),
    COOPERATION(8, "Kooperation", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No),
    MODEL_INTENTION(9, "Modellvorhaben Psy", ManagedBy.None, PortalType.PSY, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.Yes),
    DOCUMENTS(10, "Dokumente", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No),
    CERT(11, "Zertifizierung", ManagedBy.InekOrIkAdmin, PortalType.CERT, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No),
    AGENCY(12, "Behörde", ManagedBy.InekOrIkAdmin, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No),
    SPF_INSURANCE(13, "Besondere Aufgaben / Zentrumszuschlag (Funktion für Krankenkasse)", 
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No),
    CALCULATION_HOSPITAL(14, "Teilnahme Kostenkalkulation", 
            ManagedBy.InekOrIkAdmin, PortalType.CALC, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes),
    SPECIFIC_FUNCTION(15, "Besondere Aufgaben / Zentrumszuschlag (Funktionen für Krankenhaus)", 
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes),
    ADDITIONAL_COST(16, "Finanzierung von Mehrkosten", 
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes),
    // todo: make PSYCH_STAFF shareable
    PSYCH_STAFF(17, "Psych-Personalnachweis-Vereinbarung", 
            ManagedBy.InekOrIkAdmin, PortalType.PSY, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.No),
    // todo: make VALUATION_RATIO shareable
    VALUATION_RATIO(18, "Gezielte Absenkung", 
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.No),
    IK_ADMIN(19, "IK-Administration", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No),
    HC_HOSPITAL(20, "Krankenhausvergleich (Funktionen für Krankenhaus)",
            ManagedBy.IkAdminOnly, PortalType.PSY, IkReference.Hospital, IkUsage.Direct, Selectable.No, Shareable.No),
    HC_INSURANCE(21, "Krankenhausvergleich (Funktionen für Krankenkasse)",
            ManagedBy.IkAdminOnly, PortalType.INSURANCE, IkReference.Insurance, IkUsage.ByResponsibilityAndCorrelation, Selectable.No, Shareable.No),
    CARE(22, "Pflegepersonaluntergrenzen", ManagedBy.IkAdminOnly, PortalType.CARE, IkReference.Hospital, IkUsage.Direct, Selectable.No, Shareable.No),
    NUB_NOTICE(23, "NUB-Meldung (durch Krankenkasse)", 
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No),
    PSYCH_STAFF_INSURANCE(24, "Psych-PV Signaturprüfung (Funktion für Krankenkasse)",
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No),
    CARE_INSURANCE_SIGNATURE_CHECK(25, "PPUGV-Nachweisvereinbarung Signaturprüfung (Funktion für Krankenkasse)",
                          ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No);

    Feature(int id, String description, ManagedBy managedBy, PortalType portalType,
            IkReference ikReference, IkUsage ikUsage,
            Selectable selectable, Shareable shareable) {
        _id = id;
        _description = description;
        _managedBy = managedBy;
        _portalType = portalType;
        _ikReference = ikReference;
        _ikUsage = ikUsage;
        _selectable = selectable;
        _shareable = shareable;
        if (ikUsage == IkUsage.Unknown && ikReference != ikReference.None){
            throw new IllegalArgumentException();
        }
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

    private final IkUsage _ikUsage;

    public IkUsage getIkUsage() {
        return _ikUsage;
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

    public static Feature fromName(String name) {
        for (Feature feature : Feature.values()) {
            if (feature.name().equals(name)) {
                return feature;
            }
        }
        throw new IllegalArgumentException("Failed to obtain feature. Unknown name " + name);
    }

}
