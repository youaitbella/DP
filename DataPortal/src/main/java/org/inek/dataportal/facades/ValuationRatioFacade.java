/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.valuationratio.ValuationRatio;
import org.inek.dataportal.entities.valuationratio.ValuationRatioMedian;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;

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
        for(ValuationRatioMedian vrm : list) {
            if(vrm.getDrg().equals(drg) && vrm.getDataYear() == dataYear)
                return vrm;
        }
        return null;
    }
    
    private Integer getLatestDataYear() {
        List<ValuationRatioMedian> vrms = super.findAll(ValuationRatioMedian.class);
        List<Integer> dataYears = new ArrayList<>();
        for(ValuationRatioMedian vrm : vrms) {
            if(dataYears.contains(vrm.getDataYear()))
                continue;
            dataYears.add(vrm.getDataYear());
        }
        Collections.sort(dataYears);
        return dataYears.get(dataYears.size()-1);
    }
    
    public List<ValuationRatio> getValuationRatios(int accountId, DataSet dataSet) {
        String sql = "SELECT n FROM ValuationRatio n "
                + "WHERE n._accountId = :accountId and n._status BETWEEN :minStatus AND :maxStatus ORDER BY n._dataYear, n._id";
        TypedQuery<ValuationRatio> query = getEntityManager().createQuery(sql, ValuationRatio.class);
        int minStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.New.getId() : WorkflowStatus.Provided.getId();
        int maxStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.Provided.getId()-1 : WorkflowStatus.Retired.getId();
        query.setParameter("accountId", accountId);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        return query.getResultList();
    }
    
    public boolean isNewValuationRationEnabled(int accountId) {
        List<ValuationRatio> vrs = getValuationRatios(accountId, DataSet.AllOpen);
        vrs.addAll(getValuationRatios(accountId, DataSet.AllSealed));
        if(vrs.isEmpty())
            return true;
        
        int latestYear = getLatestDataYear();
        for(ValuationRatio vr : vrs) {
            if(vr.getDataYear() < latestYear)
                continue;
            if(vr.getDataYear() == latestYear)
                return false;
        }
        return true;
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
