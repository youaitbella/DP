package org.inek.dataportal.api.enums;

import org.inek.dataportal.api.helper.FeatureMessageHandler;

public enum Feature {

    UNKNOWN(0, "<unknown>", ManagedBy.None, PortalType.COMMON, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No,
            255),
    ADMIN(1, "Administration", ManagedBy.None, PortalType.ADMIN, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No,
            255),
    USER_MAINTENANCE(2, "Stammdaten", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No,
            255),
    NUB(3, "(DRG) Neue Untersuchungs- und Behandlungsmethoden",
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes,
            255),
    REQUEST_SYSTEM(4, "Anfrageverfahren", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No,
            255),
    DROPBOX(5, "DropBox", ManagedBy.InekOrIkAdmin, PortalType.BASE, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.No,
            255),
    PEPP_PROPOSAL(6, "PEPP-Vorschlagsverfahren", ManagedBy.None, PortalType.PSY, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.Yes,
            255),
    DRG_PROPOSAL(7, "DRG-Vorschlagsverfahren", ManagedBy.None, PortalType.DRG, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.Yes,
            255),
    COOPERATION(8, "Kooperation", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No,
            255),
    MODEL_INTENTION(9, "Modellvorhaben Psy", ManagedBy.None, PortalType.PSY, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.Yes,
            255),
    DOCUMENTS(10, "Dokumente", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No,
            255),
    CERT(11, "Zertifizierung", ManagedBy.InekOrIkAdmin, PortalType.CERT, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No,
            255),
    AGENCY(12, "Behörde", ManagedBy.InekOrIkAdmin, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No,
            255),
    SPF_INSURANCE(13, "Besondere Aufgaben / Zentrumszuschlag (Funktion für Krankenkasse)",
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No, 255),
    CALCULATION_HOSPITAL(14, "Teilnahme Kostenkalkulation",
            ManagedBy.InekOrIkAdmin, PortalType.CALC, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes, 255),
    SPECIFIC_FUNCTION(15, "Besondere Aufgaben / Zentrumszuschlag (Funktionen für Krankenhaus)",
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes, 255),
    ADDITIONAL_COST(16, "Finanzierung von Mehrkosten",
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.Yes, 255),
    // todo: make PSYCH_STAFF shareable
    PSYCH_STAFF(17, "Psych-Personalnachweis-Vereinbarung",
            ManagedBy.InekOrIkAdmin, PortalType.PSY, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.No, 255),
    // todo: make VALUATION_RATIO shareable
    VALUATION_RATIO(18, "Gezielte Absenkung",
            ManagedBy.InekOrIkAdmin, PortalType.DRG, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.No, 255),
    IK_ADMIN(19, "IK-Administration", ManagedBy.None, PortalType.BASE, IkReference.None, IkUsage.Unknown, Selectable.No, Shareable.No,
            255),
    HC_HOSPITAL(20, "Krankenhausvergleich (Funktionen für Krankenhaus)",
            ManagedBy.IkAdminOnly, PortalType.PSY, IkReference.Hospital, IkUsage.Direct, Selectable.No, Shareable.No, 3),
    HC_INSURANCE(21, "Krankenhausvergleich (Funktionen für Krankenkasse)",
            ManagedBy.IkAdminOnly, PortalType.INSURANCE, IkReference.Insurance, IkUsage.ByResponsibilityAndCorrelation, Selectable.No, Shareable.No,
            3),
    CARE(22, "Pflegepersonaluntergrenzen", ManagedBy.IkAdminOnly, PortalType.CARE, IkReference.Hospital, IkUsage.Direct, Selectable.No,
            Shareable.No, 255),
    NUB_NOTICE(23, "NUB-Meldung (durch Krankenkasse)",
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No, 255),
    PSYCH_STAFF_INSURANCE(24, "Psych-PV Signaturprüfung (Funktion für Krankenkasse)",
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No, 255),
    CARE_INSURANCE_SIGNATURE_CHECK(25, "PPUGV-Nachweisvereinbarung Signaturprüfung (Funktion für Krankenkasse)",
            ManagedBy.InekOrIkAdmin, PortalType.INSURANCE, IkReference.None, IkUsage.Unknown, Selectable.Yes, Shareable.No, 255),
    NUB_PSY(26, "(PSY) Neue Untersuchungs- und Behandlungsmethoden",
            ManagedBy.InekOrIkAdmin, PortalType.PSY, IkReference.Hospital, IkUsage.Direct, Selectable.Yes, Shareable.No,
            255);

    private final int _id;
    private final String _description;
    private final ManagedBy _managedBy;
    private final PortalType _portalType;
    private final IkReference _ikReference;
    private final IkUsage _ikUsage;
    private final Selectable _selectable;
    private final Shareable _shareable;
    private final int _maxUsersWithAccess;

    Feature(int id, String description, ManagedBy managedBy, PortalType portalType,
            IkReference ikReference, IkUsage ikUsage,
            Selectable selectable, Shareable shareable, int maxUsersWithAccess) {
        _id = id;
        _description = description;
        _managedBy = managedBy;
        _portalType = portalType;
        _ikReference = ikReference;
        _ikUsage = ikUsage;
        _selectable = selectable;
        _shareable = shareable;
        if (ikUsage == IkUsage.Unknown && ikReference != ikReference.None) {
            throw new IllegalArgumentException();
        }
        _maxUsersWithAccess = maxUsersWithAccess;
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

    public int getId() {
        return _id;
    }

    public String getDescription() {
        return _description;
    }

    /**
     * Approval will be replaced by rights management
     *
     * @return
     * @deprecated
     */
    @Deprecated
    public boolean getNeedsApproval() {
        if (this == NUB || this == NUB_PSY) {
            return false;
        }
        return _managedBy == ManagedBy.InekOrIkAdmin;
    }

    public ManagedBy getManagedBy() {
        return _managedBy;
    }

    public PortalType getPortalType() {
        return _portalType;
    }

    public IkReference getIkReference() {
        return _ikReference;
    }

    public IkUsage getIkUsage() {
        return _ikUsage;
    }

    public boolean isSelectable() {
        return _selectable == Selectable.Yes;
    }

    public boolean isShareable() {
        return _shareable == Shareable.Yes;
    }

    public int getMaxUsersWithAccess() {
        return _maxUsersWithAccess;
    }

    public String getDescriptionText() {
        return FeatureMessageHandler.getMessageOrEmpty("description" + this.name());
    }

}
