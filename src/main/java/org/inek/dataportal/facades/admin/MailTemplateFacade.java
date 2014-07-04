package org.inek.dataportal.facades.admin;

import org.inek.dataportal.facades.*;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.admin.MailTemplate;

@Stateless
public class MailTemplateFacade extends AbstractFacade<MailTemplate> {

    public MailTemplateFacade (){
        super(MailTemplate.class);
    }
    
}
