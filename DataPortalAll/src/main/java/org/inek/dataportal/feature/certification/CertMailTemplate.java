package org.inek.dataportal.feature.certification;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.enums.CertMailType;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.feature.admin.facade.MailTemplateFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertMailTemplate implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("CertMail");

    @Inject
    private MailTemplateFacade _mailTemplateFacade;

    private int _emailType = 1;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<SelectItem> getMailTemplates() {
        List<SelectItem> l = _mailTemplateFacade.getMailTemplateInfosByFeature(Feature.CERT);
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemNewEntry"));
        emptyItem.setNoSelectionOption(true);
        l.add(emptyItem);
        return l;
    }

    public List<SelectItem> getEmailTypes() {
        return CertMailType.getSelectItems();
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
                _emailType = _mailTemplate.getType();
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

    public String getEmailType() {
        return CertMailType.getTypeFromId(_emailType);
    }

    public void setEmailType(String emailType) {
        _emailType = CertMailType.getTypeFromLabel(emailType);
    }

    // </editor-fold>
    public String newMailTemplate() {
        _mailTemplate = new MailTemplate();
        return "";
    }

    public String deleteMailTemplate() {
        if (_mailTemplate.getId() > 0) {
            _mailTemplateFacade.remove(_mailTemplate);
        }
        _mailTemplate = new MailTemplate();
        setTemplateChanged(false);
        return "";
    }

    public String saveMailTemplate() {
        _mailTemplate.setFeature(Feature.CERT);
        _mailTemplate.setType(_emailType);
        _mailTemplate = _mailTemplateFacade.save(_mailTemplate);
        setTemplateChanged(false);
        return "";
    }

    public void mailTemplateChangeListener(AjaxBehaviorEvent event) {
        setTemplateChanged(true);
    }

    // </editor-fold>
}
