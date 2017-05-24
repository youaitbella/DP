package org.inek.dataportal.facades.certification;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.facades.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class GrouperFacade extends AbstractFacade<Grouper> {

    public GrouperFacade() {
        super(Grouper.class);
    }

    
    public List<Grouper> findBySystemId(int systemId) {
        String query = "SELECT g FROM Grouper g WHERE g._systemId = :id";
        return getEntityManager().createQuery(query, Grouper.class).setParameter("id", systemId).getResultList();
    }

    
    public List<Grouper> findByAccountId(int accountId) {
        String query = "SELECT g FROM Grouper g WHERE g._accountId = :id";
        return getEntityManager().createQuery(query, Grouper.class).setParameter("id", accountId).getResultList();
    }

    
    public Grouper findByAccountAndSystemId(int accountId, int systemId) {
        String jpql = "SELECT g FROM Grouper g WHERE g._accountId = :accId and g._systemId = :sysId";
        try {
            TypedQuery<Grouper> query = getEntityManager().createQuery(jpql, Grouper.class);
            query.setParameter("accId", accountId).setParameter("sysId", systemId);
            return query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No grouper found for account {0} and system {1}", new Object[]{accountId, systemId});
            return new Grouper();
        }
    }

}
