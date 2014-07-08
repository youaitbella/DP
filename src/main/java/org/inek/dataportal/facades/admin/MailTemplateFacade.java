package org.inek.dataportal.facades.admin;

import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.facades.*;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.Feature;

@Stateless
public class MailTemplateFacade extends AbstractFacade<MailTemplate> {

    public MailTemplateFacade() {
        super(MailTemplate.class);
    }

    public MailTemplate save(MailTemplate mailTemplate) {
        if (mailTemplate.getId() == -1) {
            persist(mailTemplate);
            return mailTemplate;
        }
        return merge(mailTemplate);
    }

    public List<SelectItem> getMailTemplateInfos() {
        String statement = "SELECT m._id, m._feature, m._subject FROM MailTemplate m";
        Query query = getEntityManager().createQuery(statement);
        List data = query.getResultList();
        List<SelectItem> result = new ArrayList<>();
        for (Object x : data){
            Object[] info = (Object[]) x;
            result.add(new SelectItem( (int)info[0], ((Feature)info[1]).getDescription() + ": " + info[2] ));
        }
        return result;
    }
    
}
