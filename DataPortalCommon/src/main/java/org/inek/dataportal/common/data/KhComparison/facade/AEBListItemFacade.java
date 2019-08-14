/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_1;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_2;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE2;

/**
 *
 * @author lautenti
 */
@Stateless
public class AEBListItemFacade extends AbstractDataAccess {

    public List<SelectItem> getStructureCategorie() {
        String sql = "select scId, scName from psy.listStructureCategorie";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object[]> result = query.getResultList();

        List<SelectItem> items = new ArrayList<>();
        for (Object[] resultElement : result) {
            items.add(new SelectItem(resultElement[0].toString(), resultElement[1].toString()));
        }
        return items;
    }

    public Boolean existPageCombinationInYear(AEBPageE1_1 page, int year) {
        String sql = "select 1 from psy.listPepp\n" +
                "where lpPepp = '" + page.getPepp().toUpperCase() + "'\n" +
                "and lpCompensationClass = " + page.getCompensationClass() + "\n" +
                "and lpDataYear = " + year + "\n" +
                "and lpValuationRadioDay = " + page.getValuationRadioDay() + "";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object> result = query.getResultList();
        return !result.isEmpty();
    }

    public Boolean existPageCombinationInYear(AEBPageE1_2 page, int year) {
        String sql = "select 1 from psy.listEt\n" +
                "where leEt = '" + page.getEt().toUpperCase() + "'\n" +
                "and leDataYear = " + year + "\n" +
                "and leValuationRadioDay = " + page.getValuationRadioDay() + "";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object> result = query.getResultList();
        return !result.isEmpty();
    }

    public Boolean existPageCombinationInYear(AEBPageE2 page, int year) {
        String sql = "select 1 from psy.listZe\n" +
                "where lzZe = '" + page.getZe().toUpperCase() + "'\n" +
                "and lzDataYear = " + year + "\n" +
                "and lzValuationRadioDay = " + page.getValuationRadioDay() + "";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object> result = query.getResultList();
        return !result.isEmpty();
    }

    public double getValuationRadioDaysByPepp(String pepp, int compensationClass, int dataYear) {
        String sql = "select lpValuationRadioDay from psy.listPepp\n"
                + "where lpPepp = '" + pepp.toUpperCase() + "'\n"
                + "and lpCompensationClass = " + compensationClass + "\n"
                + "and lpDataYear = " + dataYear + "";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<BigDecimal> result = query.getResultList();
        if (result.isEmpty()) {
            return 0;
        } else {
            return result.get(0).doubleValue();
        }
    }

    public double getValuationRadioDaysByEt(String et, int dataYear) {
        String sql = "select leValuationRadioDay from psy.listEt\n"
                + "where leEt = '" + et.toUpperCase() + "'\n"
                + "and leDataYear = " + dataYear + "";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<BigDecimal> result = query.getResultList();
        if (result.isEmpty()) {
            return 0;
        } else {
            return result.get(0).doubleValue();
        }
    }

    public double getValuationRadioDaysByZe(String ze, int dataYear) {
        String sql = "select lzValuationRadioDay from psy.listZe\n"
                + "where lzZe = '" + ze.toUpperCase() + "'\n"
                + "and lzDataYear = " + dataYear + "";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<BigDecimal> result = query.getResultList();
        if (result.isEmpty()) {
            return 0;
        } else {
            return result.get(0).doubleValue();
        }
    }

}
