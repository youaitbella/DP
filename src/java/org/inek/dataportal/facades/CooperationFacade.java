
package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.Cooperation;

/**
 *
 * @author muellermi
 */
@Stateless
public class CooperationFacade extends AbstractFacade<Cooperation> {
    public CooperationFacade(){
        super(Cooperation.class);
    }

    /**
     * returns a list all partner accounts of account identified with accountId
     *
     * @param accountId Id of interesting account
     * @return
     */
    public List<Account> getCooperationPartners(int accountId) {
        String query = "SELECT acId, acLastModified, acIsDeactivated, "
                + "acUser, acMail, acGender, acTitle, acFirstName, "
                + "acLastName, acInitials, acPhone, acRoleId, acCompany, acCustomerTypeId, "
                + "acIK, acStreet, acPostalCode, acTown, acCustomerPhone, acCustomerFax "
                + "from dbo.account "
                + "join usr.Cooperation on acId = coAccountId1 "
                + "where coAccountId2 = ?1 "
                + "union "
                + "SELECT acId, acLastModified, acIsDeactivated, "
                + "acUser, acMail, acGender, acTitle, acFirstName, "
                + "acLastName, acInitials, acPhone, acRoleId, acCompany, acCustomerTypeId, "
                + "acIK, acStreet, acPostalCode, acTown, acCustomerPhone, acCustomerFax "
                + "from dbo.account "
                + "join usr.Cooperation on acId = coAccountId2 "
                + "where coAccountId1 = ?1";
        return getEntityManager().createNativeQuery(query, Account.class).setParameter(1, accountId).getResultList();
    }

    public void createCooperation(int partner1Id, int partner2Id) {
        if (partner1Id > partner2Id){
            int tmp=partner1Id;
            partner1Id=partner2Id;
            partner2Id=tmp;
        }
        if (existsCooperation(partner1Id, partner2Id)) {
            return;
        }
        Cooperation cooperation = Cooperation.create(partner1Id, partner2Id);
        persist(cooperation);
    }

    public Cooperation findCooperation(int partner1Id, int partner2Id) {
        if (partner1Id > partner2Id){
            int tmp=partner1Id;
            partner1Id=partner2Id;
            partner2Id=tmp;
        }
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Cooperation> cq = cb.createQuery(Cooperation.class);
        Root request = cq.from(Cooperation.class);
        Predicate criteria = cb.conjunction();
        criteria = cb.and(criteria, cb.equal(request.get("_accountId1"), partner1Id));
        criteria = cb.and(criteria, cb.equal(request.get("_accountId2"), partner2Id));
        cq.select(request).where(criteria);
        List<Cooperation> cooperations = getEntityManager().createQuery(cq).getResultList();
        return cooperations.isEmpty() ? null : cooperations.get(0);
    }
    
    public boolean existsCooperation(int partner1Id, int partner2Id) {
        return findCooperation(partner1Id, partner2Id) != null;
    }
    
    public void finishCooperation(int partner1Id, int partner2Id) {
        Cooperation cooperation = findCooperation(partner1Id, partner2Id);
        if (cooperation == null){return;}
        remove(cooperation); // todo: remove any config
    }

    
}
