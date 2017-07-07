/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.i68.I68;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Stateless
public class I68Facade extends AbstractDataAccess {
    
    public I68 findI68(int id) {
        return super.find(I68.class, id);
    }

    public I68 findFreshI68(int id) {
        return super.findFresh(I68.class, id);
    }
    
    public List<I68> getI68s(int accountId, DataSet dataSet) {
        String sql = "SELECT n FROM I68 n "
                + "WHERE n._accountId = :accountId and n._workflowStatusId BETWEEN :minStatus AND :maxStatus ORDER BY n._dataYear, n._id";
        TypedQuery<I68> query = getEntityManager().createQuery(sql, I68.class);
        int minStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.New.getId() : WorkflowStatus.Provided.getId();
        int maxStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.Provided.getId()-1 : WorkflowStatus.Retired.getId();
        query.setParameter("accountId", accountId);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        return query.getResultList();
    }
    
    public I68 saveI68(I68 i68) {
        if (i68.getId() == -1) {
            persist(i68);
        } 
        return i68;
    }
    
    public void delete(I68 i68) {
        remove(i68);
    }
}
