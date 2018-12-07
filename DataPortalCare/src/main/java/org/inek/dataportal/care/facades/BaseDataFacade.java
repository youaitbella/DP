/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.facades;

import org.inek.dataportal.care.entities.BaseData;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author lautenti
 */
@Stateless
public class BaseDataFacade extends AbstractDataAccessWithActionLog {
    public List<BaseData> getAllBaseData() {
        String jpql = "SELECT bd FROM BaseData bd";
        TypedQuery<BaseData> query = getEntityManager().createQuery(jpql, BaseData.class);
        return query.getResultList();
    }

    public List<BaseData> getAllBaseDataByYear(int year) {
        String jpql = "SELECT bd FROM BaseData bd \n" +
                " where bd._validFrom <= :year " +
                "and bd._validTo >= :year";
        TypedQuery<BaseData> query = getEntityManager().createQuery(jpql, BaseData.class);
        query.setParameter("year", year);
        return query.getResultList();
    }
}
