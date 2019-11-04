/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.utils.CareDeptStationHelper;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lautenti
 */
@Stateless
public class StructuralChangesFacade extends AbstractDataAccessWithActionLog {

    @Transactional
    public StructuralChangesBaseInformation save(StructuralChangesBaseInformation baseInfo) {
        if (baseInfo.getId() == -1) {
            persist(baseInfo);
            return baseInfo;
        }
        return merge(baseInfo);
    }

    public List<DeptStation> findWardsByIkAndDate(int ik, Date date) {
        String sql = "select bi from DeptBaseInformation bi where bi._ik = :ik and " +
                "bi._statusId in (10, 200) order by bi._year desc, bi._send desc";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(sql, DeptBaseInformation.class);
        query.setParameter("ik", ik);
        List<DeptBaseInformation> resultList = query.getResultList();
        DeptBaseInformation deptBaseInformation = resultList.get(0);
        return CareDeptStationHelper.getStationsByDate(deptBaseInformation.getAllWards(), date);
    }
}
