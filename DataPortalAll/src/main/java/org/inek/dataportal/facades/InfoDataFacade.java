/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.inek.dataportal.entities.ListFeature;
import org.inek.dataportal.entities.ListWorkflowStatus;
import org.inek.dataportal.entities.TrashMail;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 * This is a facade to various informative data
 * @author muellermi
 */
@Stateless
public class InfoDataFacade extends AbstractDataAccess {

    public boolean isTrashMailDomain(String domain) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TrashMail> cq = cb.createQuery(TrashMail.class);
        Root<TrashMail> request = cq.from(TrashMail.class);
        cq.select(request).where(cb.equal(request.get("_domain"), domain));
        return getEntityManager().createQuery(cq).getResultList().size() > 0;
    }

    public List<ListFeature> findAllListFeature(){
        return findAllFresh(ListFeature.class);
    }

    public void saveListFeature(ListFeature listFeature) {
        persist(listFeature);
    }

    public List<ListWorkflowStatus> findAllListWorkflowStatus(){
        return findAllFresh(ListWorkflowStatus.class);
    }

    public void saveListWorkflowStatus(ListWorkflowStatus listWorkflowStatus) {
        persist(listWorkflowStatus);
    }
}
