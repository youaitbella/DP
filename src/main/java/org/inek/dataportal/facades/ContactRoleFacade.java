package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.inek.dataportal.entities.ContactRole;

@Stateless
public class ContactRoleFacade extends AbstractFacade<ContactRole> {

    public ContactRoleFacade() {
        super(ContactRole.class);
    }

    public List<ContactRole> findAllExtern() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ContactRole> cq = cb.createQuery(ContactRole.class);
        cq.select(cq.from(ContactRole.class));
        return getEntityManager().createQuery(cq).setHint("javax.persistence.cache.retrieveMode", "BYPASS").getResultList();
    }

}
