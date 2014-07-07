
package org.inek.dataportal.feature.admin;

import java.util.List;
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
import org.inek.dataportal.entities.admin.MailTemplate;
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

    @PostConstruct
    private void init() {
        if (!_sessionController.isInekUser(Feature.ADMIN)) {
                _sessionController.logMessage("Non-authorized access to admin task.");
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();
                nav.handleNavigation(fc, null, Pages.NotAllowed.URL());
                return;
        }
        _sessionController.beginConversation(_conversation);
    }

    @PreDestroy
    private void destroy() {
    }

    private CooperationController getCooperationController() {
        return (CooperationController) _sessionController.getFeatureController(Feature.COOPERATION);
    }

    // <editor-fold defaultstate="collapsed" desc="tab MailTemplate">
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    private List<MailTemplate> _mailTemplates;

    public List<MailTemplate> getMailTemplates() {
        if (_mailTemplates == null){
            _mailTemplates = _mailTemplateFacade.findAll();
        }
        return _mailTemplates;
    }

    public void setMailTemplates(List<MailTemplate> mailTemplates) {
        _mailTemplates = mailTemplates;
    }
    
    private MailTemplate _mailTemplate;

    public MailTemplate getMailTemplate() {
        return _mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        _mailTemplate = mailTemplate;
    }
    // </editor-fold>
    
    public String newMailTemplate() {
        _mailTemplate = new MailTemplate();
        return "";
    }
    
    public String saveMailTemplate() {
        _mailTemplateFacade.save(_mailTemplate);
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tab XXX">
    // </editor-fold>
}
