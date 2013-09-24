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
    UserMaintenance("/UserMaintenance/UserMaintenance"),
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
    PartCooperation("/Cooperation/PartCooperation"),
    ListCooperation("/Cooperation/ListCooperation"),
    CooperationSummary("/Cooperation/CooperationSummary"),
    CooperationEditPartner("/Cooperation/CooperationEditPartner"),
    CooperationEditNub("/Cooperation/CooperationEditNub"),
    //
    PartModelIntention("/ModelIntention/PartModelIntention"),
    ModelIntentionSummary("/ModelIntention/ModelIntentionSummary"),
    //
    PrintView("/common/PrintView"),
    ErrorRedirector("/common/ErrorRedirector"),
    Error("/common/Error"),
    SessionTimeoutRedirector("/common/TimeOutRedirector"),
    SessionTimeout("/common/TimeOut"),
    InvalidConversation("/common/InvalidConversation"),
    //
    AdminError("/Admin/error"),
    AdminApproved("/Admin/approvalcompleted"),
    AdminApproval("/Admin/approval");
    
    private final String _url;
    private Pages(String url){
        _url = url;
    }

    public String URL (){
        return _url + ".xhtml";
    }

}
