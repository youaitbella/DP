package org.inek.dataportal.facades;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.PasswordRequest;

/**
 *
 * @author muellermi
 */
@Stateless
public class PasswordRequestFacade extends AbstractFacade<PasswordRequest> {

    public PasswordRequestFacade (){
        super(PasswordRequest.class);
    }
    public List<PasswordRequest> findRequestsOlderThan(Date date) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PasswordRequest> cq = cb.createQuery(PasswordRequest.class);
        Root request = cq.from(PasswordRequest.class);
        cq.select(request).where(cb.lessThan(request.get("_creationDate"), date));
        return getEntityManager().createQuery(cq).getResultList();
    }
    
}
