package org.inek.dataportal.common.data.icmt.facade;

import org.inek.dataportal.common.data.AbstractFacade;
import org.inek.dataportal.common.data.icmt.entities.ContactRole;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Stateless
public class ContactRoleFacade extends AbstractFacade<ContactRole> implements Serializable {

    public ContactRoleFacade() {
        super(ContactRole.class);
    }

    
    public List<ContactRole> findAllExtern() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ContactRole> cq = cb.createQuery(ContactRole.class);
        Root<ContactRole> request = cq.from(ContactRole.class);
        cq.select(request).where(cb.isTrue(request.get("_externVisible")));
        return getEntityManager().createQuery(cq).getResultList();
    }

}
