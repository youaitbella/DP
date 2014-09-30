package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.ContactRole;

@Stateless
public class ContactRoleFacade extends AbstractFacade<ContactRole> {

    public ContactRoleFacade() {
        super(ContactRole.class);
    }

    public List<ContactRole> findAllExtern() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ContactRole> cq = cb.createQuery(ContactRole.class);
        Root request = cq.from(ContactRole.class);
        cq.select(request).where(cb.isTrue(request.get("_externVisible")));
        return getEntityManager().createQuery(cq).getResultList();
    }

}
