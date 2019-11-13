/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.utils.CareDeptStationHelper;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
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

    public List<DeptWard> findWardsByIkAndDate(int ik, Date date) {
        String sql = "select bi from DeptBaseInformation bi where bi._ik = :ik and " +
                "bi._statusId in (10, 200) order by bi._year desc, bi._send desc";
        TypedQuery<DeptBaseInformation> query = getEntityManager().createQuery(sql, DeptBaseInformation.class);
        query.setParameter("ik", ik);
        List<DeptBaseInformation> resultList = query.getResultList();
        DeptBaseInformation deptBaseInformation = resultList.get(0);
        return CareDeptStationHelper.getStationsByDate(deptBaseInformation.getAllWards(), date);
    }

    public List<SelectItem> findDeleteReasons() {
        String sql = "select scdtId, scdtText\n" +
                "from care.listStructuralChangesDetailType\n" +
                "where scdtCategorieId = 1";

        Query query = getEntityManager().createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();

        List<SelectItem> items = new ArrayList<>();

        for (Object[] obj : objects) {
            items.add(new SelectItem((int) obj[0], (String) obj[1]));
        }

        return items;
    }

    public List<StructuralChangesBaseInformation> findBaseInformationsByIk(int ik) {
        String sql = "select sbi from StructuralChangesBaseInformation sbi where sbi._ik = :ik order by sbi._requestedAt desc";
        TypedQuery<StructuralChangesBaseInformation> query = getEntityManager().createQuery(sql, StructuralChangesBaseInformation.class);
        query.setParameter("ik", ik);

        return query.getResultList();
    }

    public StructuralChangesBaseInformation findBaseInformationsById(int id) {
        String sql = "select sbi from StructuralChangesBaseInformation sbi where sbi._id = :id order by sbi._requestedAt desc";
        TypedQuery<StructuralChangesBaseInformation> query = getEntityManager().createQuery(sql, StructuralChangesBaseInformation.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }
}
