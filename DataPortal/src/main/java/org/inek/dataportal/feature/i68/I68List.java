package org.inek.dataportal.feature.i68;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.i68.I68;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.I68Facade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class I68List {

    private static final Logger LOGGER = Logger.getLogger("I68ListLogger");

    @Inject private I68Facade _i68Facade;
    @Inject private SessionController _sessionController;

    public List<I68> getOpenRelations() {
        return _i68Facade.getI68s(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<I68> getProvidedRelations() {
        return _i68Facade.getI68s(_sessionController.getAccountId(), DataSet.AllSealed);
    }

    public String editI68() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("Insurance");
        return Pages.InsuranceNubNoticeEditAddress.URL();
    }

    public String getConfirmMessage(int i68Id) {
        I68 i68 = _i68Facade.findFreshI68(i68Id);
        String msg = "Meldung für " + i68.getIk() + "\n"
                + (i68.getStatus().getId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }
    
    public String deleteI68(int i68Id) {
        I68 i68 = _i68Facade.findFreshI68(i68Id);
        if (i68 == null) {
            return "";
        }
        if (_sessionController.isMyAccount(i68.getAccountId())) {
            if (i68.getStatus().getId() < WorkflowStatus.Provided.getId()) {
                _i68Facade.delete(i68);
            } else {
                i68.setStatus(WorkflowStatus.Retired);
                _i68Facade.saveI68(i68);
            }
        }
        return "";
    }
    
    public String printI68(int i68Id) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameI68"));
        Utils.getFlash().put("targetPage", Pages.I68Summary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_i68Facade.findI68(i68Id)));
        return Pages.PrintView.URL();
    }

}
