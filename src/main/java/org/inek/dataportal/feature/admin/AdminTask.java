/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin;

import org.inek.dataportal.feature.cooperation.*;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.feature.AbstractEditController;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class AdminTask extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("AdminTask");

    @Inject
    private SessionController _sessionController;
    @Inject
    MailTemplateFacade _mailTemplateFacade;
    @Inject
    private Conversation _conversation;

    @Override
    protected void addTopics() {
        addTopic(AdminTaskTabs.tabAdminTaskInekRoles.name(), Pages.AdminTaskInekRoles.URL());
        addTopic(AdminTaskTabs.tabAdminTaskRoleMapping.name(), Pages.AdminTaskRoleMapping.URL());
        addTopic(AdminTaskTabs.tabAdminTaskMailTemplate.name(), Pages.AdminTaskMailtTemplate.URL());
    }

    enum AdminTaskTabs {

        tabAdminTaskInekRoles,
        tabAdminTaskRoleMapping,
        tabAdminTaskMailTemplate;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    // </editor-fold>
    @PostConstruct
    private void init() {
        if (!_sessionController.isInekUser(Feature.ADMIN)) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nav = fc.getApplication().getNavigationHandler();
            nav.handleNavigation(fc, null, Pages.MainApp.URL());
        }
        _sessionController.beginConversation(_conversation);
    }

    @PreDestroy
    private void destroy() {
    }

    private CooperationController getCooperationController() {
        return (CooperationController) _sessionController.getFeatureController(Feature.COOPERATION);
    }

    // <editor-fold defaultstate="collapsed" desc="tab XXX">
    // </editor-fold>
}
