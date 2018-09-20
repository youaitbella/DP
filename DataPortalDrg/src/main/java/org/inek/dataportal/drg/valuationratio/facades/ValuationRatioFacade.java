/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.valuationratio.facades;

import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.drg.valuationratio.entities.ValuationRatio;
import org.inek.dataportal.drg.valuationratio.entities.ValuationRatioDrgCount;
import org.inek.dataportal.drg.valuationratio.entities.ValuationRatioMedian;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Stateless
public class ValuationRatioFacade extends AbstractDataAccessWithActionLog {

    public ValuationRatio findValuationRatio(int id) {
        return super.find(ValuationRatio.class, id);
    }

    public ValuationRatio findFreshValuationRatio(int id) {
        return super.findFresh(ValuationRatio.class, id);
    }

    public ValuationRatioMedian findValuationRatioMedian(int id) {
        return super.find(ValuationRatioMedian.class, id);
    }

    public ValuationRatioMedian findFreshValuationRatioMedian(int id) {
        return super.findFresh(ValuationRatioMedian.class, id);
    }

    public ValuationRatioMedian findMedianByDrgAndDataYear(String drg, int dataYear) {
        List<ValuationRatioMedian> list = findAll(ValuationRatioMedian.class);
        for (ValuationRatioMedian vrm : list) {
            if (vrm.getDrg().equals(drg) && vrm.getDataYear() == dataYear) {
                return vrm;
            }
        }
        return null;
    }
    
    public ValuationRatioDrgCount findValuationRatioDrgCount(int ik, int dataYear, String drg) {
        List<ValuationRatioDrgCount> counts = super.findAll(ValuationRatioDrgCount.class);     
        return counts.stream()
                .filter(vr -> vr.getIk() == ik)
                .filter(vr -> vr.getDataYear() == dataYear)
                .filter(vr -> vr.getDrg().equals(drg))
                .findFirst()
                .orElse(new ValuationRatioDrgCount());
    }

    public boolean existsValuationRatio(int ik, int year) {
        String jpql = "SELECT n FROM ValuationRatio n "
                + "WHERE n._dataYear = :dtYear and n._ik = :IK and n._status <= :status";
        TypedQuery<ValuationRatio> query = getEntityManager().createQuery(jpql, ValuationRatio.class);
        query.setParameter("dtYear", year);
        query.setParameter("IK", ik);
        query.setParameter("status", WorkflowStatus.Taken);
        if (query.getResultList().isEmpty()) {
            return false;
        }
        return true;
    }

    public List<ValuationRatio> getValuationRatios(int accountId, DataSet dataSet) {
        String sql = "SELECT n FROM ValuationRatio n "
                + "WHERE n._accountId = :accountId and n._status BETWEEN :minStatus AND :maxStatus ORDER BY n._dataYear, n._id";
        TypedQuery<ValuationRatio> query = getEntityManager().createQuery(sql, ValuationRatio.class);
        WorkflowStatus minStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.New : WorkflowStatus.Provided;
        WorkflowStatus maxStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Retired;
        query.setParameter("accountId", accountId);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        return query.getResultList();
    }

    /**
     * A new valuation ration entry is allowed, if there is no entry for any of the iks in the given year
     * @param iks
     * @param year
     * @return 
     */
    public boolean isNewValuationRationEnabled(Set<Integer> iks, int year) {
        return iks.stream().anyMatch(ik -> !existsValuationRatio(ik, year));

    }

    public ValuationRatio saveValuationRatio(ValuationRatio vr) {
        if (vr.getId() == -1) {
            persist(vr);
        } else {
            vr = merge(vr);
        }
        return vr;
    }

    public ValuationRatio merge(ValuationRatio entity) {
        return super.merge(entity);
    }
   
    public void delete(ValuationRatio vr) {
        remove(vr);
    }
    
}
