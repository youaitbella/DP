/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.TrashMail;

/**
 *
 * @author muellermi
 */
@Stateless
public class TrashMailFacade extends AbstractFacade<TrashMail> {

    public TrashMailFacade (){
        super(TrashMail.class);
    }

    public boolean exists(String domain) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TrashMail> cq = cb.createQuery(TrashMail.class);
        Root request = cq.from(TrashMail.class);
        cq.select(request).where(cb.equal(request.get("_domain"), domain));
        return getEntityManager().createQuery(cq).getFirstResult() > 0;
        
    }
    
}
