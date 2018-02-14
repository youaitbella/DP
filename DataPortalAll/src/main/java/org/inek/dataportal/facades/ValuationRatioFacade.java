/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import org.inek.dataportal.common.data.AbstractDataAccess;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.valuationratio.ValuationRatio;
import org.inek.dataportal.entities.valuationratio.ValuationRatioDrgCount;
import org.inek.dataportal.entities.valuationratio.ValuationRatioMedian;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Stateless
public class ValuationRatioFacade extends AbstractDataAccess {

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
                .orElse(null);
    }

    public boolean existsValuationRatio(int ik, int year) {
        String sql = "SELECT n FROM ValuationRatio n "
                + "WHERE n._dataYear = :dtYear and n._ik = :IK and n._status <= 20";
        TypedQuery<ValuationRatio> query = getEntityManager().createQuery(sql, ValuationRatio.class);
        query.setParameter("dtYear", year);
        query.setParameter("IK", ik);
        if (query.getResultList().isEmpty()) {
            return false;
        }
        return true;
    }

    public List<ValuationRatio> getValuationRatios(int accountId, DataSet dataSet) {
        String sql = "SELECT n FROM ValuationRatio n "
                + "WHERE n._accountId = :accountId and n._status BETWEEN :minStatus AND :maxStatus ORDER BY n._dataYear, n._id";
        TypedQuery<ValuationRatio> query = getEntityManager().createQuery(sql, ValuationRatio.class);
        int minStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.New.getId() : WorkflowStatus.Provided.getId();
        int maxStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.Provided.getId() - 1 : WorkflowStatus.Retired.getId();
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
            merge(vr);
        }
        return vr;
    }

    public void delete(ValuationRatio vr) {
        remove(vr);
    }
}
