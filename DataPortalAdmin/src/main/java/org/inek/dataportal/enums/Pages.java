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

    Login("/Login/Login"),
    LoginFinishRegister("/Login/FinishRegister"),
    LoginRegister("/Login/Register"),
    LoginActivate("/Login/Activate"),
    LoginActivateAccount("/Login/ActivateAccount"),
    LoginRequestPwd("/Login/RequestPassword"),
    LoginActivatePwd("/Login/ActivatePassword"),
    LoginFinishRequestPwd("/Login/FinishRequestPassword"),
    MainApp("/Common/MainApp"),
    //
    UserMaintenanceMasterData("/UserMaintenance/UserMaintenanceMasterData"),
    UserMaintenanceAdditionalIKs("/UserMaintenance/UserMaintenanceAdditionalIKs"),
    UserMaintenanceFeatures("/UserMaintenance/UserMaintenanceFeatures"),
    UserMaintenanceOther("/UserMaintenance/UserMaintenanceOther"),
    UserMaintenanceConfig("/UserMaintenance/UserMaintenanceConfig"),
    //
    PartDropBox("/DropBox/PartDropBox"),
    ListDropBox("/DropBox/ListDropBox"),
    DropBoxSummary("/DropBox/DropBoxSummary"),
    DropBoxUpload("/DropBox/DropBoxUpload"),
    DropBoxCreate("/DropBox/CreateDropBox"),
    //
    PartCooperation("/Cooperation/PartCooperation"),
    ListCooperation("/Cooperation/ListCooperation"),
    CooperationSummary("/Cooperation/CooperationSummary"),
    CooperationEditPartner("/Cooperation/CooperationEditPartner"),
    CooperationEditMessage("/Cooperation/CooperationEditMessage"),
    CooperationEditIk("/Cooperation/CooperationEditIk"),
    CooperationEditOther("/Cooperation/CooperationEditOther"),
    //
    Cert("/Certification/Cert"),
    PartCert("/Certification/PartCert"),
    CertSystemManagement("/Certification/CertSystemManagement"),
    CertMail("/Certification/CertMail"),
    CertMailTemplate("/Certification/CertMailTemplate"),
    CertCertification("/Certification/CertCertification"),
    CertGrouperResults("/Certification/CertGrouperResults"),
    CertGrouperCc("/Certification/CertGrouperCc"),
    //
    PartAdditionalCost("/AdditionalCost/PartAdditionalCost"),
    AdditionalCostSummary("/AdditionalCost/AdditionalCostSummary"),
    AdditionalCostEdit("/AdditionalCost/AdditionalCostEdit"),
    //
    NotAllowed("/Common/NotAllowed"),
    PrintView("/Common/PrintView"),
    PrintMultipleView("/Common/PrintMultipleView"),
    Error("/Common/Error"),
    DataError("/Common/DataError"),
    SessionTimeout("/Common/TimeOut"),
    DoubleWindow("/Common/DoubleWindow"),
    InvalidConversation("/Common/InvalidConversation"),
    SearchCode("/Common/SearchCode"),
    //
    AdminError("/Admin/error"),
    AdminApproved("/Admin/approvalcompleted"),
    AdminApproval("/Admin/approval"),
    AdminApprovalNone("/Admin/approvalNone"),
    AdminTaskSystemStatus("/Admin/AdminTaskSystemStatus"),
    AdminTaskInekRoles("/Admin/AdminTaskInekRoles"),
    AdminTaskRoleMapping("/Admin/AdminTaskRoleMapping"),
    AdminTaskMailTemplate("/Admin/AdminTaskMailTemplate"),
    AdminTaskIkSupervisor("/Admin/AdminTaskIkSupervisor"),
    AdminTaskIkAdmin("/Admin/AdminTaskIkAdmin"),
    AdminTaskChangeNub("/Admin/AdminTaskChangeNub"),
    AdminTaskUploadDoc("/Admin/AdminTaskUploadDoc"),
    //
    IkAdminSummary("/IkAdmin/IkAdminSummary"),
    PartIkAdmin("/IkAdmin/PartIkAdmin"),
    IkAdminList("/IkAdmin/fragment/IkList"),
    IkAdminTasks("/IkAdmin/IkAdminTasks"),
    IkAdminUser("/IkAdmin/IkAdminUser"),
    //
    RequestSummary("/RequestSystem/RequestSummary"),
    PartRequest("/RequestSystem/PartRequest"),
    RequestEdit("/RequestSystem/RequestEdit"),
    RequestEditAddress("/RequestSystem/RequestEditAddress"),
    RequestEditProblem("/RequestSystem/RequestEditProblem"),
    RequestEditSolutions("/RequestSystem/RequestEditSolutions"),
    RequestEditBackground("/RequestSystem/RequestEditBackground"),
    RequestEditRelevance("/RequestSystem/RequestEditRelevance"),
    RequestEditDocuments("/RequestSystem/RequestEditDocuments"),
    //
    DocumentsSummary("/Documents/DocumentsSummary"),
    DocumentUploadFromInek("/Documents/UploadFromInek"),
    DocumentUploadToInek("/Documents/UploadToInek"),
    DocumentsApproval("/Documents/Approval"),
    ListDocuments("/Documents/ListDocuments"),
    ListDocumentsInek("/Documents/ListDocumentsInek"),
    DocumentsViewer("/Documents/Viewer");

    private final String _url;

    Pages(String url) {
        _url = url;
    }

    public String URL() {
        return _url + ".xhtml";
    }

    public String RedirectURL() {
        return _url + ".xhtml?faces-redirect=true";
    }

}
