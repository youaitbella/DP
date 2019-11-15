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
import org.inek.dataportal.common.enums.WorkflowStatus;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<SelectItem> findTmpCloseReasons() {
        String sql = "select scdtId, scdtText\n" +
                "from care.listStructuralChangesDetailType\n" +
                "where scdtCategorieId = 3";

        return getSelectItems(sql);
    }

    public List<SelectItem> findCloseReasons() {
        String sql = "select scdtId, scdtText\n" +
                "from care.listStructuralChangesDetailType\n" +
                "where scdtCategorieId = 1";

        return getSelectItems(sql);
    }

    public List<SelectItem> findChangeReasons() {
        String sql = "select scdtId, scdtText\n" +
                "from care.listStructuralChangesDetailType\n" +
                "where scdtCategorieId = 2";

        return getSelectItems(sql);
    }

    private List<SelectItem> getSelectItems(String sql) {
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

    public Optional<StructuralChangesBaseInformation> findOpenBaseInformationsByIk(int ik) {
        List<StructuralChangesBaseInformation> baseInfo = findBaseInformationsByIk(ik);

        for (StructuralChangesBaseInformation structuralChangesBaseInformation : baseInfo) {
            if (structuralChangesBaseInformation.getStatus().equals(WorkflowStatus.New)) {
                return Optional.of(structuralChangesBaseInformation);
            }
        }
        return Optional.empty();
    }

    public List<StructuralChangesBaseInformation> findSendBaseInformationsByIk(int ik) {
        List<StructuralChangesBaseInformation> baseInfo = findBaseInformationsByIk(ik);

        List<StructuralChangesBaseInformation> tmpList = new ArrayList<>();
        for (StructuralChangesBaseInformation structuralChangesBaseInformation : baseInfo) {
            if(structuralChangesBaseInformation.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
                tmpList.add(structuralChangesBaseInformation);
            }
        }
        return tmpList;
    }

    public StructuralChangesBaseInformation findBaseInformationsById(int id) {
        String sql = "select sbi from StructuralChangesBaseInformation sbi where sbi._id = :id order by sbi._requestedAt desc";
        TypedQuery<StructuralChangesBaseInformation> query = getEntityManager().createQuery(sql, StructuralChangesBaseInformation.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }

    public void deleteBaseInformation(StructuralChangesBaseInformation info) {
        remove(info);
    }
}
