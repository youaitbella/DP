package org.inek.dataportal.feature.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.inek.dataportal.feature.cooperation.*;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class AdminTask extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("AdminTask");

    public AdminTask() {
        _logger.log(Level.INFO, "Ctor AdminTask");
    }

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
        addTopic(AdminTaskTabs.tabAdminTaskMailTemplate.name(), Pages.AdminTaskMailTemplate.URL());
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
    public List<SelectItem> getMailTemplates() {
        List<SelectItem> l = _mailTemplateFacade.getMailTemplateInfos();
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemNewEntry"));
        emptyItem.setNoSelectionOption(true);
        l.add(emptyItem);
        return l;
    }

    private MailTemplate _mailTemplate = new MailTemplate();

    public MailTemplate getMailTemplate() {
        return _mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        _mailTemplate = mailTemplate;
    }

    private int _templateId = -1;

    public int getTemplateId() {
        return _mailTemplate.getId();
    }

    public void setTemplateId(int templateId) {
        if (templateId != _mailTemplate.getId()) {
            if (templateId == -1) {
                _mailTemplate = new MailTemplate();
            } else {
                _mailTemplate = _mailTemplateFacade.find(templateId);
            }
            setChanged(false);
        }
    }

    private boolean _isChanged = false;

    public boolean isChanged() {
        return _isChanged;
    }

    public void setChanged(boolean isChanged) {
        _isChanged = isChanged;
    }

    // </editor-fold>
    public String newMailTemplate() {
        _mailTemplate = new MailTemplate();
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public String deleteMailTemplate() {
        if (_mailTemplate.getId() > 0) {
            _mailTemplateFacade.remove(_mailTemplate);
        }
        _mailTemplate = new MailTemplate();
        setChanged(false);
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public String saveMailTemplate() {
        _mailTemplate = _mailTemplateFacade.save(_mailTemplate);
        setChanged(false);
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public void changeListener(AjaxBehaviorEvent event) {
        setChanged(true);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="tab XXX">
    // </editor-fold>
}
