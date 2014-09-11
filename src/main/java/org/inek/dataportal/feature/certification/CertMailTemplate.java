package org.inek.dataportal.feature.certification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertMailTemplate implements Serializable {
    
    private static final Logger _logger = Logger.getLogger("CertMail");
    
    @Inject
    private MailTemplateFacade _mailTemplateFacade;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    
    public List<SelectItem> getMailTemplates() {
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
    
    public List<SelectItem> getAreas() {
        List<SelectItem> list = new ArrayList<>();
        list.add(new SelectItem(Feature.CERT.getDescription()));
        return list;
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
        _mailTemplate.setFeature(Feature.CERT);
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public String deleteMailTemplate() {
        if (_mailTemplate.getId() > 0) {
            _mailTemplateFacade.remove(_mailTemplate);
        }
        _mailTemplate = new MailTemplate();
        setTemplateChanged(false);
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public String saveMailTemplate() {
        _mailTemplate = _mailTemplateFacade.save(_mailTemplate);
        setTemplateChanged(false);
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public void mailTemplateChangeListener(AjaxBehaviorEvent event) {
        setTemplateChanged(true);
    }

    // </editor-fold>
}
