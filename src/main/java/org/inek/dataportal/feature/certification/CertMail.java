package org.inek.dataportal.feature.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.admin.MailTemplateFacade;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CertMail {

    private static final Logger _logger = Logger.getLogger("CertMail");
    private int _selectedTemplate = 0;

    @Inject
    private MailTemplateFacade _mailTemplateFacade;
    
    public SelectItem[] getEmailTemplates() {
        List<SelectItem> emailTemplates = new ArrayList<>();
        List<MailTemplate> mts = _mailTemplateFacade.findTemplatesByFeature(Feature.CERT);
        for(MailTemplate t : mts) {
           emailTemplates.add(new SelectItem("" + t.getId() + " - " + t.getName()));
        }
        return emailTemplates.toArray(new SelectItem[emailTemplates.size()]);
    }

    public int getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(int _selectedTemplate) {
        this._selectedTemplate = _selectedTemplate;
    }
}
