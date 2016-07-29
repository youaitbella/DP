/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.insurance.DosageForm;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.entities.insurance.Unit;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Stateless
public class InsuranceFacade extends AbstractDataAccess {

    public InsuranceNubNotice findNubNotice (int id){
        return super.find(InsuranceNubNotice.class, id);
    }
    
    public InsuranceNubNotice findFreshNubNotice (int id){
        return super.findFresh(InsuranceNubNotice.class, id);
    }
   
    public List<InsuranceNubNotice> getAccountNotices(int accountId, DataSet dataSet){
        String sql = "SELECT n FROM InsuranceNubNotice n WHERE n._accountId = :accountId and n._workflowStatusId BETWEEN :minStatus AND :maxStatus ORDER BY n._year, n._id";
        TypedQuery<InsuranceNubNotice> query = getEntityManager().createQuery(sql, InsuranceNubNotice.class);
        int minStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.New.getValue() : WorkflowStatus.Provided.getValue() -1;
        int maxStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.Provided.getValue() : WorkflowStatus.Retired.getValue() -1;
        query.setParameter("accountId", accountId);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        return query.getResultList();
        
    }
    public InsuranceNubNotice saveNubNotice(InsuranceNubNotice notice) {
        if (notice.getId() == -1) {
            persist(notice);
            return notice;
        }
        return merge(notice);
    }

    public List<DosageForm> getDosageForms() {
        return findAll(DosageForm.class);
    }

    public List<Unit> getUnits() {
        return findAll(Unit.class);
    }
    
}
