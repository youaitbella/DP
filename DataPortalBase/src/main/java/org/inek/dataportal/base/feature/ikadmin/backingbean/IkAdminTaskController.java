package org.inek.dataportal.base.feature.ikadmin.backingbean;

import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named
@FeatureScoped(name = "IkAdminTask")
public class IkAdminTaskController extends AbstractEditController {

    @Inject
    private SessionController _sessionController;

    private int _ik;

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String ikParam = "" + params.get("ik");
        try {
            int ik = Integer.parseInt(ikParam);
            if (_sessionController.getAccount().getAdminIks().stream().anyMatch(a -> a.getIk() == ik)) {
                _ik = ik;
                return;
            }
        } catch (NumberFormatException ex) {
            // ignore here and handle after catch
        }
        Utils.navigate(Pages.NotAllowed.RedirectURL());
    }

    @Override
    protected void addTopics() {
        addTopic("tabRightManagement", Pages.IkAdminRightManagement.URL());
        addTopic("tabResponsibilityManagement", Pages.IkAdminResponsibilityManagement.URL());
    }

    public int getIk() {
        return _ik;
    }
}
