package org.inek.dataportal.cert.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.cert.entities.AdditionalEmail;
import org.inek.dataportal.cert.entities.Grouper;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.AbstractDataAccess;

/**
 *
 * @author muellermi
 */
@Stateless
public class GrouperFacade extends AbstractDataAccess {

    public List<Grouper> findBySystemId(int systemId) {
        String query = "SELECT g FROM Grouper g WHERE g._systemId = :id";
        return getEntityManager().createQuery(query, Grouper.class).setParameter("id", systemId).getResultList();
    }

    public List<Grouper> findByAccountId(int accountId) {
        String query = "SELECT g FROM Grouper g WHERE g._accountId = :id";
        return getEntityManager().createQuery(query, Grouper.class).setParameter("id", accountId).getResultList();
    }

    public Grouper findByAccountAndSystemId(int accountId, int systemId) {
        String jpql = "SELECT g FROM Grouper g WHERE g._accountId = :accId and g._system._id = :sysId";
        try {
            TypedQuery<Grouper> query = getEntityManager().createQuery(jpql, Grouper.class);
            query.setParameter("accId", accountId).setParameter("sysId", systemId);
            return query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No grouper found for account {0} and system {1}", new Object[]{accountId, systemId});
            return new Grouper();
        }
    }

    public Grouper findFresh(int id) {
        return super.findFresh(Grouper.class, id);
    }

    public List<String> findGrouperEmailReceivers(Account grouperAccount) {
        List<String> tmpList = new ArrayList<>();
        if (grouperAccount == null) {
            return tmpList;
        }
        String query = "SELECT g FROM AdditionalEmail g WHERE g._accountId = :id";
        List<AdditionalEmail> additionalEmails = getEntityManager()
                .createQuery(query, AdditionalEmail.class)
                .setParameter("id", grouperAccount.getId())
                .getResultList();
        additionalEmails.stream().forEach(ae -> tmpList.add(ae.getEmail()));
        return tmpList;
    }

    public List<AdditionalEmail> findGrouperEmailReceivers(int accountId) {
        String query = "SELECT g FROM AdditionalEmail g WHERE g._accountId = :id";
        return getEntityManager()
                .createQuery(query, AdditionalEmail.class)
                .setParameter("id", accountId)
                .getResultList();
    }

    public void deleteAdditionalEmailCc(int id) {
        AdditionalEmail ae = super.find(AdditionalEmail.class, id);
        super.remove(ae);
    }

    public List<Grouper> getGrouperWithoutWebsideRealease() {
        String query = "SELECT g FROM Grouper g JOIN RemunerationSystem s on g._system._id = s._id WHERE "
                + "g._certStatus = 90 AND g._websiteRelease IS NULL AND s._active = 1";
        return getEntityManager().createQuery(query, Grouper.class).getResultList();
    }

    public boolean grouperHasApproveForSystem(int acId, int systemId, Date date) {
        String query = "SELECT g FROM Grouper g JOIN RemunerationSystem s on g._system._id = s._id WHERE "
                + "g._accountId = :acId AND "
                + "g._system._id = :sysId AND "
                + "s._active = 1 AND "
                + "g._approvedUntil >= :date";
        return !getEntityManager().createQuery(query, Grouper.class)
                .setParameter("acId", acId)
                .setParameter("sysId", systemId)
                .setParameter("date", date)
                .getResultList().isEmpty();
    }
}
