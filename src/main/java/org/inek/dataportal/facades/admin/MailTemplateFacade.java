package org.inek.dataportal.facades.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.inek.dataportal.facades.*;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
        String statement = "SELECT m._id, m._name FROM MailTemplate m";
        Query query = getEntityManager().createQuery(statement);
        List data = query.getResultList();
        List<SelectItem> result = new ArrayList<>();
        for (Object x : data){
            Object[] info = (Object[]) x;
            result.add(new SelectItem( (int)info[0], (String) info[1] ));
        }
        return result;
    }

    public MailTemplate findByName(String name) {
        String statement = "SELECT m FROM MailTemplate m WHERE m._name = :name";
        TypedQuery<MailTemplate> query = getEntityManager().createQuery(statement, MailTemplate.class);
        try {
            return query.setParameter("name", name).getSingleResult();
        } catch (Exception ex){
            _logger.log(Level.WARNING, "MailTemplate not found: {0}", name);
        }
        return null;
    }
    
    public List<MailTemplate> findTemplatesByFeature(Feature f) {
        String statement = "SELECT m FROM MailTemplate m WHERE m._feature = :feature";
        TypedQuery<MailTemplate> query = getEntityManager().createQuery(statement, MailTemplate.class);
        try {
            return query.setParameter("feature", f).getResultList();
        } catch (Exception ex){
            _logger.log(Level.WARNING, "MailTemplate not found: {0}", f);
        }
        return null;
    }
}
