/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.aeb.facade;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import org.inek.dataportal.common.data.AbstractDataAccess;

/**
 *
 * @author lautenti
 */
@Stateless
public class AEBListItemFacade extends AbstractDataAccess {

    public SelectItem[] getAccommodationItems() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem("-1", "Bitte Eintrag wählen");
        items[1] = new SelectItem("1", "Bonn");
        items[2] = new SelectItem("2", "Nicht Bonn");
        return items;
    }

    public SelectItem[] getAmbulantItems() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem("-1", "Bitte Eintrag wählen");
        items[1] = new SelectItem("1", "Bonn");
        items[2] = new SelectItem("2", "Nicht Bonn");
        return items;
    }

    public double getValuationRadioDays(String pepp, int compensationClass, int dataYear) {
        String sql = "select lpValuationRadioDay from psy.listPepp\n"
                + "where lpPepp = '" + pepp.toUpperCase() + "'\n"
                + "and lpCompensationClass = " + compensationClass + "\n"
                + "and lpDataYear = " + dataYear + "";

        Query query = getEntityManager().createNativeQuery(sql);
        List<BigDecimal> result = query.getResultList();
        if (result.isEmpty()) {
            return 0;
        } else {
            return result.get(0).doubleValue();
        }
    }

}
