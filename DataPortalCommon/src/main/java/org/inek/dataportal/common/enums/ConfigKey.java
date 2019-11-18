package org.inek.dataportal.common.enums;

/**
 * @author muellermi
 */
public enum ConfigKey {

    TestMode(false),
    IsNubCreateEnabled(false),
    IsNubSendEnabled(false),
    IsPsyNubCreateEnabled(false),
    IsPsyNubSendEnabled(false),
    RemindNubSeal(false),
    RemindNubPeppSeal(true),
    IsCareCreateEnabled(false),
    IsCareSendEnabled(false),
    IsCareChangeEnabled(false),
    IsCareProofCreateEnabled(false),
    IsCareProofSendEnabled(false),
    IsCareProofChangeEnabled(false),
    IsDrgProposalCreateEnabled(false),
    IsDrgProposalSendEnabled(false),
    IsPeppProposalCreateEnabled(false),
    IsPeppProposalSendEnabled(false),
    IsAdditionalCostCreateEnabled(false),
    IsAdditionalRequestSendEnabled(false),
    IsModelIntentionSendEnabled(false),
    IsStatemenOfParticipanceCreateEnabled(false),
    IsCalculationBasicsDrgCreateEnabled(false),
    IsCalculationBasicsPsyCreateEnabled(false),
    IsCalculationBasicsInvCreateEnabled(false),
    IsCalculationBasicsTpgCreateEnabled(false),
    IsCalculationBasicsObdCreateEnabled(false),
    IsStatemenOfParticipanceSendEnabled(false),
    IsStatemenOfParticipanceResendEnabled(true),
    IsCalculationBasicsDrgSendEnabled(false),
    IsCalculationBasicsPsySendEnabled(false),
    IsCalculationBasicsObdSendEnabled(false),
    IsSpecificFunctionRequestCreateEnabled(false),
    IsSpecificFunctionRequestSendEnabled(false),
    IsSpecificFunctionAgreementCreateEnabled(false),
    IsSpecificFunctionAgreementSendEnabled(false),
    IsDistributionModelSendEnabled(false),
    IsDistributionModelDrgCreateEnabled(false),
    IsDistributionModelPeppCreateEnabled(false),
    IsPsychStaffCreateEnabled(false),
    IsPsychStaffSendEnabled(false),
    IsValuationRatioCreateEnabled(false),
    IsValuationRatioSendEnabled(false),
    IsPsychStaffParanoiacheckEnabled(true),
    DataServiceClientVersion("20180101.0"),
    FolderRoot("//vFileserver01/company$/EDV/Datenportal/"),
    CertiFolderRoot("//vFileserver01/company$/EDV/Projekte/Zertifizierung/Pruefung/"),
    DocumentScanDir(false),
    DropBoxTypeId(1),
    FolderUpload("upload"),
    FolderDocumentScanBase("documents"),
    ExceptionEmail("PortalAdmin@inek-drg.de"),
    ManagerEmail("dsportalanmeldungen@inek-drg.de"),
    ReportValidity(120),
    DocumentSetRead(false),
    CertCompareOnUpload(true),
    IkAdminEnable(true),
    KhComparisonJobSavePath("//vFileserver01/company$/EDV/Datenportal/kh-vergleich/auswertungen"),
    KhComparisonUploadPath("//vFileserver01/company$/EDV/Datenportal/documents/KH-Vergleich"),
    isKhComparisionInsuranceEnabled(false),
    ReportHostName("vreportserver01"),
    VzHost("localhost:8000"),
    VzRestCheckVzNumber("https://{vzHost}/api/intern/v1.0/LocationIdExists?hospital_ik={ik}&location_identifier={vzNumber}"),
    CareStructuralChangesEnable(true);
    //
    private final Object _defaultValue;

    ConfigKey(Object defaultValue) {
        _defaultValue = defaultValue;
    }

    public String getDefault() {
        return "" + _defaultValue;
    }

    public boolean isBool() {
        return _defaultValue instanceof Boolean;
    }

    public boolean getBoolDefault() {
        return (boolean) _defaultValue;
    }

    public boolean isInt() {
        return _defaultValue instanceof Integer;
    }

    public int getIntDefault() {
        return (int) _defaultValue;
    }

    //private getType
    @Override
    public String toString() {
        return this.name();
    }

}
