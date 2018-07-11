/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.backingbean;

import java.io.Serializable;
import java.util.List;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.MailTemplateFacade;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author lanzrama
 */
@Named
@FeatureScoped
public class AdminMailTemplate implements Serializable {

    @Inject
    private MailTemplateFacade _mailTemplateFacade;
    @Inject
    private DialogController _dialogController;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<SelectItem> getMailTemplates() {
        List<SelectItem> l = _mailTemplateFacade.getMailTemplateInfos();
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemNewEntry"));
        emptyItem.setNoSelectionOption(true);
        l.add(emptyItem);
        return l;
    }

    public List<SelectItem> getMailTemplatesCert() {
        List<SelectItem> l = _mailTemplateFacade.getMailTemplateInfosByFeature(Feature.CERT);
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
            setTemplateChanged(false);
        }
    }

    private boolean _templateChanged = false;

    public boolean isTemplateChanged() {
        return _templateChanged;
    }

    public void setTemplateChanged(boolean isChanged) {
        _templateChanged = isChanged;
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
        setTemplateChanged(false);
        _dialogController.showWarningDialog("Das Template wurde gelöscht.", "Gelöscht");
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public String saveMailTemplate() {

        _mailTemplate = _mailTemplateFacade.save(_mailTemplate);
        setTemplateChanged(false);
        _dialogController.showWarningDialog("Das Template wurde gespeichert.", "Gespeichert");
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public void mailTemplateChangeListener(AjaxBehaviorEvent event) {
        setTemplateChanged(true);
    }

}
