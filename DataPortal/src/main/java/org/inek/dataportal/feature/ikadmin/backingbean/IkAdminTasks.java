/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.ikadmin.backingbean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.ikadmin.entity.AccessRight;
import org.inek.dataportal.feature.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class IkAdminTasks extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("IkAdminTasks");
    private static final String TOPIC_USER = "topicUserManagement";

    @Inject private SessionController _sessionController;
    @Inject private IkAdminFacade _ikAdminFacade;
    @Inject private ApplicationTools _appTools;

    private int _ik;

    public int getIk() {
        return _ik;
    }

    private List<AccessRight> _accessRights;

    public List<AccessRight> getAccessRights() {
        if (_accessRights == null) {
            _accessRights = _ikAdminFacade.findAccessRights(_ik);
        }
        return _accessRights;
    }
    // </editor-fold>

    @Override
    protected void addTopics() {
        addTopic(TOPIC_USER, Pages.IkAdminUser.URL());
    }

    @Override
    protected void topicChanged() {
//        if (_sessionController.getAccount().isAutoSave() && !isReadOnly()) {
//            save(false);
//        }
    }

    @Override
    protected String getOutcome() {
        return "";
    }

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

}
