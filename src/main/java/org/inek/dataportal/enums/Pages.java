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
    ListNub("/NUB/ListNUB"),
    NubSummary("/NUB/NubSummary"),
    NubFromTemplate("/NUB/NubFromTemplate"),
    NubEdit("/NUB/NubEdit"),
    NubEditAddress("/NUB/NubEditAddress"),
    NubEditPage1("/NUB/NubEditPage1"),
    NubEditPage2("/NUB/NubEditPage2"),
    NubEditPage3("/NUB/NubEditPage3"),
    NubEditPage4("/NUB/NubEditPage4"),
    NubEditPageDocuments("/NUB/NubEditPageDocuments"),
    //
    PartDropBox("/DropBox/PartDropBox"),
    ListDropBox("/DropBox/ListDropBox"),
    DropBoxSummary("/DropBox/DropBoxSummary"),
    DropBoxUpload("/DropBox/DropBoxUpload"),
    DropBoxCreate("/DropBox/CreateDropBox"),
    //
    PartRequest("/RequestSystem/PartRequest"),
    ListRequest("/RequestSystem/ListRequest"),
    RequestSummary("/RequestSystem/RequestSummary"),
    RequestEdit("/RequestSystem/RequestEdit"),
    RequestEditAddress("/RequestSystem/RequestEditAddress"),
    RequestEditProblem("/RequestSystem/RequestEditProblem"),
    RequestEditSolutions("/RequestSystem/RequestEditSolutions"),
    RequestEditBackground("/RequestSystem/RequestEditBackground"),
    RequestEditRelevance("/RequestSystem/RequestEditRelevance"),
    RequestEditDocuments("/RequestSystem/RequestEditDocuments"),
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
    ListCert("/Certification/ListCert"),
    CertSummary("/Certification/CertSummary"),
    CertSystemManagement("/Certification/CertSystemManagement"),
    CertMail("/Certification/CertMail"),
    CertMailTemplate("/Certification/CertMailTemplate"),
    CertCertification("/Certification/CertCertification"),
    CertGrouperResults("/Certification/CertGrouperResults"),
    //
    NotAllowed("/common/NotAllowed"),
    PrintView("/common/PrintView"),
    ErrorRedirector("/common/ErrorRedirector"),
    Error("/common/Error"),
    DataErrorRedirector("/common/DataErrorRedirector"),
    DataError("/common/DataError"),
    SessionTimeoutRedirector("/common/TimeOutRedirector"),
    SessionTimeout("/common/TimeOut"),
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
    //
    DocumentsEdit("/Documents/DocumentsEdit"),
    DocumentsSummary("/Documents/DocumentsSummary"),
    ListDocuments("/Documents/ListDocuments");

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
