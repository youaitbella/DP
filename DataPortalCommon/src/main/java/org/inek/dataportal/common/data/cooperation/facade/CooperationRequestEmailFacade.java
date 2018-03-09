package org.inek.dataportal.common.data.cooperation.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRequestEmail;
import org.inek.dataportal.common.data.AbstractFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class CooperationRequestEmailFacade extends AbstractFacade<CooperationRequestEmail> {
    
    @Inject private CooperationRequestFacade _coopFacade;
    
    public CooperationRequestEmailFacade() {
        super(CooperationRequestEmail.class);
    }
    
    public boolean exists(int creatorId, String requestEmail) {
        return findAll().stream().anyMatch((request) -> (request.getCreatorId() == creatorId && request.getRequestEmail().equals(requestEmail)));
    }
    
    public void createRealCooperationRequests(Account account) {
        List<CooperationRequestEmail> requests = findRequestsByEmail(account.getEmail());
        for (CooperationRequestEmail request : requests) {
            _coopFacade.createCooperationRequest(request.getCreatorId(), account.getId());
            remove(request);
        }
    }

    private List<CooperationRequestEmail> findRequestsByEmail(String email) {
        String jpql = "select r from CooperationRequestEmail r where r._requestEmail = :email";
        TypedQuery<CooperationRequestEmail> query = getEntityManager().createQuery(jpql, CooperationRequestEmail.class);
        query.setParameter("email", email);
        return query.getResultList();
    }
    
    public void createCooperation(int creatorId, String requestEmail) {
        if(exists(creatorId, requestEmail)) {
            return;
        }
        CooperationRequestEmail cooperation = CooperationRequestEmail.create(creatorId, requestEmail);
        persist(cooperation);
    }

}
