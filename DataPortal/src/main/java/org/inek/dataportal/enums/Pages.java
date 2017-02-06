/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum Pages {

    Login("/login/Login"),
    LoginFinishRegister("/login/FinishRegister"),
    LoginRegister("/login/Register"),
    LoginActivate("/login/Activate"),
    LoginActivateAccount("/login/ActivateAccount"),
    LoginRequestPwd("/login/RequestPassword"),
    LoginActivatePwd("/login/ActivatePassword"),
    LoginFinishRequestPwd("/login/FinishRequestPassword"),
    MainApp("/common/MainApp"),
    //
    UserMaintenanceMasterData("/UserMaintenance/UserMaintenanceMasterData"),
    UserMaintenanceAdditionalIKs("/UserMaintenance/UserMaintenanceAdditionalIKs"),
    UserMaintenanceFeatures("/UserMaintenance/UserMaintenanceFeatures"),
    UserMaintenanceOther("/UserMaintenance/UserMaintenanceOther"),
    UserMaintenanceConfig("/UserMaintenance/UserMaintenanceConfig"),
    //
    PartNub("/NUB/PartNUB"),
    NubSummary("/NUB/NubSummary"),
    NubFromTemplate("/NUB/NubFromTemplate"),
    NubEditAddress("/NUB/NubEditAddress"),
    NubEditPage1("/NUB/NubEditPage1"),
    NubEditPage2("/NUB/NubEditPage2"),
    NubEditPage3("/NUB/NubEditPage3"),
    NubEditPage4("/NUB/NubEditPage4"),
    NubEditPageDocuments("/NUB/NubEditPageDocuments"),
    NubRequestCorrection("/NUB/NubRequestCorrection"),
    NubMethodInfo("/NUB/NubMethodInfo"),
    //
    PartDropBox("/DropBox/PartDropBox"),
    ListDropBox("/DropBox/ListDropBox"),
    DropBoxSummary("/DropBox/DropBoxSummary"),
    DropBoxUpload("/DropBox/DropBoxUpload"),
    DropBoxCreate("/DropBox/CreateDropBox"),
    //
    PartCalculationHospital("/CalcHospital/PartCalculationHospital"),
    CalculationHospitalSummary("/CalcHospital/CalculationHospitalSummary"),
    StatementOfParticipanceEditAddress("/CalcHospital/StatementOfParticipance/EditAddress"),
    StatementOfParticipanceEditStatements("/CalcHospital/StatementOfParticipance/EditStatements"),
    // DRG calculation
    CalcDrgEdit("/CalcHospital/Drg/Edit"),
    CalcDrgBasics("/CalcHospital/Drg/Basics"),
    CalcDrgBasicExplanation("/CalcHospital/Drg/BasicExplanation"),
    CalcDrgExternalServiceProvision("/CalcHospital/Drg/ExternalServiceProvision"),
    CalcDrgOperation("/CalcHospital/Drg/Operation"),
    CalcDrgAnaestesia("/CalcHospital/Drg/Anaestesia"),
    CalcDrgMaternityRoom("/CalcHospital/Drg/MaternityRoom"),
    CalcDrgCardiology("/CalcHospital/Drg/Cardiology"),
    CalcDrgEndoscopy("/CalcHospital/Drg/Endoscopy"),
    CalcDrgRadiology("/CalcHospital/Drg/Radiology"),
    CalcDrgLaboratory("/CalcHospital/Drg/Laboratory"),
    CalcDrgDiagnosticScope("/CalcHospital/Drg/DiagnosticScope"),
    CalcDrgTherapeuticScope("/CalcHospital/Drg/TherapeuticScope"),
    CalcDrgPatientAdmission("/CalcHospital/Drg/PatientAdmission"),
    CalcrgNormalWard("/CalcHospital/Drg/NormalWard"),
    CalcDrgIntensiveCare("/CalcHospital/Drg/IntensiveCare"),
    CalcDrgStrokeUnit("/CalcHospital/Drg/StrokeUnit"),
    CalcDrgMedicalInfrastructure("/CalcHospital/Drg/MedicalInfrastructure"),
    CalcDrgNonMedicalInfrastructure("/CalcHospital/Drg/NonMedicalInfrastructure"),
    CalcDrgStaffCost("/CalcHospital/Drg/StaffCost"),
    CalcDrgValvularIntervention("/CalcHospital/Drg/ValvularIntervention"),
    CalcDrgNeonatology("/CalcHospital/Drg/Neonat/Neonatology"),
    // PEPP calculation
    CalcPeppEdit("/CalcHospital/Pepp/EditPepp"),
    CalcPeppBasics("/CalcHospital/Pepp/BasicsPepp"),
    CalcPeppBasicExplanation("/CalcHospital/Pepp/BasicExplanationPepp"),
    CalcPeppExternalServiceProvision("/CalcHospital/Pepp/ExternalServiceProvisionPepp"),
    CalcPeppTherapyScope("/CalcHospital/Pepp/TherapyPepp"),
    CalcPeppRadiology("/CalcHospital/Pepp/RadiologyPepp"),
    CalcPeppLaboratory("/CalcHospital/Pepp/LaboratoryPepp"),
    CalcPeppDiagnosticScope("/CalcHospital/Pepp/DiagnosticScopePepp"),
    CalcPeppTherapeuticScope("/CalcHospital/Pepp/TherapeuticScopePepp"),
    CalcPeppPatientAdmission("/CalcHospital/Pepp/PatientAdmissionPepp"),
    CalcPeppStation("/CalcHospital/Pepp/StationPepp"),
    CalcPeppMedicalInfrastructure("/CalcHospital/Pepp/MedicalInfrastructurePepp"),
    CalcPeppNonMedicalInfrastructure("/CalcHospital/Pepp/NonMedicalInfrastructurePepp"),
    CalcPeppStaffCost("/CalcHospital/Pepp/StaffCostPepp"),
    
    //
    PartPeppProposal("/PeppProposal/PartPeppProposal"),
    ListPeppProposal("/PeppProposal/ListPeppProposal"),
    PeppProposalSummary("/PeppProposal/PeppProposalSummary"),
    PeppProposalInek("/PeppProposal/PeppProposalInek"),
    PeppProposalEdit("/PeppProposal/PeppProposalEdit"),
    PeppProposalEditAddress("/PeppProposal/PeppProposalEditAddress"),
    PeppProposalEditPolicy("/PeppProposal/PeppProposalEditPolicy"),
    PeppProposalEditProblem("/PeppProposal/PeppProposalEditProblem"),
    PeppProposalEditSolution("/PeppProposal/PeppProposalEditSolution"),
    PeppProposalEditCoding("/PeppProposal/PeppProposalEditCoding"),
    PeppProposalEditDocuments("/PeppProposal/PeppProposalEditDocuments"),
    PeppProposalEditComments("/PeppProposal/PeppProposalEditComments"),
    PeppProposalRequestCorrection("/PeppProposal/PeppProposalRequestCorrection"),
    //
    PartDrgProposal("/DrgProposal/PartDrgProposal"),
    ListDrgProposal("/DrgProposal/ListDrgProposal"),
    DrgProposalSummary("/DrgProposal/DrgProposalSummary"),
    DrgProposalInek("/DrgProposal/DrgProposalInek"),
    DrgProposalEdit("/DrgProposal/DrgProposalEdit"),
    DrgProposalEditAddress("/DrgProposal/DrgProposalEditAddress"),
    DrgProposalEditPolicy("/DrgProposal/DrgProposalEditPolicy"),
    DrgProposalEditProblem("/DrgProposal/DrgProposalEditProblem"),
    DrgProposalEditSolution("/DrgProposal/DrgProposalEditSolution"),
    DrgProposalEditCoding("/DrgProposal/DrgProposalEditCoding"),
    DrgProposalEditDocuments("/DrgProposal/DrgProposalEditDocuments"),
    DrgProposalEditComments("/DrgProposal/DrgProposalEditComments"),
    DrgProposalRequestCorrection("/DrgProposal/DrgProposalRequestCorrection"),
    //
    PartCooperation("/Cooperation/PartCooperation"),
    ListCooperation("/Cooperation/ListCooperation"),
    CooperationSummary("/Cooperation/CooperationSummary"),
    CooperationEditPartner("/Cooperation/CooperationEditPartner"),
    CooperationEditMessage("/Cooperation/CooperationEditMessage"),
    CooperationEditNub("/Cooperation/CooperationEditNub"),
    CooperationEditOther("/Cooperation/CooperationEditOther"),
    //
    PartModelIntention("/ModelIntention/PartModelIntention"),
    ListModelIntention("/ModelIntention/ListModelIntention"),
    ModelIntentionSummary("/ModelIntention/ModelIntentionSummary"),
    ModelIntentionTypeAndNumPat("/ModelIntention/ModelIntentionTypeAndNumPat"),
    ModelIntentionGoals("/ModelIntention/ModelIntentionGoals"),
    ModelIntentionTreatAreaAndCosts("/ModelIntention/ModelIntentionTreatAreaAndCosts"),
    ModelIntentionStructure("/ModelIntention/ModelIntentionStructure"),
    ModelIntentionQuality("/ModelIntention/ModelIntentionQuality"),
    FragmentModelLife("/ModelIntention/fragment/ModelLife"),
    FragmentRemuneration("/ModelIntention/fragment/Remuneration"),
    FragmentCost("/ModelIntention/fragment/Cost"),
    FragmentAdjustment("/ModelIntention/fragment/Adjustment"),
    FragmentAgreedPatients("/ModelIntention/fragment/AgreedPatients"),
    FragmentAcademicSupervision("/ModelIntention/fragment/AcademicSupervision"),
    FragmentQuality("/ModelIntention/fragment/Quality"),
    //
    Cert("/Certification/Cert"),
    PartCert("/Certification/PartCert"),
    CertSystemManagement("/Certification/CertSystemManagement"),
    CertMail("/Certification/CertMail"),
    CertMailTemplate("/Certification/CertMailTemplate"),
    CertCertification("/Certification/CertCertification"),
    CertGrouperResults("/Certification/CertGrouperResults"),
    //
    PartAgency("/Agency/PartAgency"),
    //
    PartInsurance("/Insurance/PartInsurance"),
    InsuranceSummary("/Insurance/InsuranceSummary"),
    InsuranceNubNoticeEditAddress("/Insurance/InsuranceNubNoticeEdit"),
    InsuranceNubNoticeEditList("/Insurance/InsuranceNubNoticeEdit"),
    
    //
    PartSpecificFunction("/SpecificFunction/PartSpecificFunction"),
    SpecificFunctionSummary("/SpecificFunction/SpecificFunctionSummary"),

    //
    NotAllowed("/common/NotAllowed"),
    PrintView("/common/PrintView"),
    PrintMultipleView("/common/PrintMultipleView"),
    Error("/common/Error"),
    DataError("/common/DataError"),
    SessionTimeout("/common/TimeOut"),
    DoubleWindow("/common/DoubleWindow"),
    InvalidConversation("/common/InvalidConversation"),
    SearchCode("/common/SearchCode"),
    //
    AdminError("/Admin/error"),
    AdminApproved("/Admin/approvalcompleted"),
    AdminApproval("/Admin/approval"),
    AdminApprovalNone("/Admin/approvalNone"),
    AdminTaskSystemStatus("/Admin/AdminTaskSystemStatus"),
    AdminTaskInekRoles("/Admin/AdminTaskInekRoles"),
    AdminTaskRoleMapping("/Admin/AdminTaskRoleMapping"),
    AdminTaskMailTemplate("/Admin/AdminTaskMailtTemplate"),
    AdminTaskIkSupervisor("/Admin/AdminTaskIkSupervisor"),
    AdminTaskChangeNub("/Admin/AdminTaskChangeNub"),
    AdminTaskUploadDoc("/Admin/AdminTaskUploadDoc"),
    //
    DocumentsUpload("/Documents/Upload"),
    DocumentsApproval("/Documents/Approval"),
    DocumentsList("/Documents/List"),
    DocumentsSummary("/Documents/DocumentsSummary"),
    ListDocuments("/Documents/ListDocuments"),
    DocumentsViewer("/Documents/Viewer");

    private final String _url;

    private Pages(String url) {
        _url = url;
    }

    public String URL() {
        return _url + ".xhtml";
    }

    public String RedirectURL() {
        return _url + ".xhtml?faces-redirect=true";
    }

}
