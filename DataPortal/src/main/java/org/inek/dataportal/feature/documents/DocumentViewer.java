package org.inek.dataportal.feature.documents;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.cooperation.CooperationRequestFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.helper.structures.DocInfo;

@Named
@RequestScoped
public class DocumentViewer {

    @Inject AccountDocumentFacade _accountDocFacade;
    @Inject SessionController _sessionController;
    @Inject PortalMessageFacade _messageFacade;
    @Inject CooperationRequestFacade _cooperationRequestFacade;

    public List<DocInfo> getDocuments() {
        if (_sessionController.isInekUser(Feature.ADMIN)) {
            return _accountDocFacade.getSupervisedDocInfos();
        } else {
            return _accountDocFacade.getSupervisedDocInfos(_sessionController.getAccountId());
        }
    }

    public boolean renderDocList() {
        return getDocuments().size() > 0;
    }

}
