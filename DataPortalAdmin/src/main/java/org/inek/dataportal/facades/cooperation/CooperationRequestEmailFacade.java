package org.inek.dataportal.facades.cooperation;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.entities.cooperation.CooperationRequestEmail;
import org.inek.dataportal.facades.AbstractFacade;
import org.inek.dataportal.facades.account.AccountFacade;

/**
 *
 * @author muellermi
 */
@Stateless
public class CooperationRequestEmailFacade extends AbstractFacade<CooperationRequestEmail> {
    
    @Inject private AccountFacade _accFacade;
    @Inject private CooperationRequestFacade _coopFacade;
    
    public CooperationRequestEmailFacade() {
        super(CooperationRequestEmail.class);
    }
    
    public boolean exists(int creatorId, String requestEmail) {
        return findAll().stream().anyMatch((request) -> (request.getCreatorId() == creatorId && request.getRequestEmail().equals(requestEmail)));
    }
    
    public void createRealCooperationRequests(String email) {
        List<CooperationRequestEmail> requests = findAll();
        requests.stream().filter((request) -> !(!request.getRequestEmail().equals(email))).map((request) -> {
            Account newAccount = _accFacade.findByMail(email);
            _coopFacade.createCooperationRequest(request.getCreatorId(), newAccount.getId());
            return request;
        }).forEachOrdered((request) -> {
            remove(request);
        });
    }

    public void createCooperation(int creatorId, String requestEmail) {
        if(exists(creatorId, requestEmail)) {
            return;
        }
        CooperationRequestEmail cooperation = CooperationRequestEmail.create(creatorId, requestEmail);
        persist(cooperation);
    }
}
