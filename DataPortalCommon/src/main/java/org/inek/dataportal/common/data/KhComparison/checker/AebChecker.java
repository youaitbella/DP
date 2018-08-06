/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.checker;

import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;

/**
 *
 * @author lautenti
 */
public class AebChecker {

    private AEBListItemFacade _aebListItemFacade;

    private String _message = "";

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        this._message = message;
    }

    public AebChecker(AEBListItemFacade facade) {
        _aebListItemFacade = facade;
    }

    public Boolean checkAeb(AEBBaseInformation info) {
        checkPageE1_1(info);
        checkPageE1_2(info);
        checkPageE2(info);
        return getMessage().isEmpty();
    }

    private void checkPageE1_1(AEBBaseInformation info) {
        for (AEBPageE1_1 page : info.getAebPageE1_1()) {
            double value = _aebListItemFacade.getValuationRadioDaysByPepp(page.getPepp(),
                    page.getCompensationClass(),
                    info.getYear());
            if (page.getValuationRadioDay() != value) {
                addMessage("Pepp: " + page.getPepp() + " - "
                        + page.getCompensationClass()
                        + ": Unterschiedliche Werte. Eingetragen: "
                        + page.getValuationRadioDay()
                        + " Katalog: "
                        + value
                );
                page.setValuationRadioDay(value);
            }
        }
    }

    private void checkPageE1_2(AEBBaseInformation info) {
        for (AEBPageE1_2 page : info.getAebPageE1_2()) {
            double value = _aebListItemFacade.getValuationRadioDaysByEt(page.getEt(),
                    info.getYear());
            if (page.getValuationRadioDay() != value) {
                addMessage("ET: " + page.getEt()
                        + ": Unterschiedliche Werte. Eingetragen: "
                        + page.getValuationRadioDay()
                        + " Katalog: "
                        + value
                );
                page.setValuationRadioDay(value);
            }
        }
    }

    private void checkPageE2(AEBBaseInformation info) {
        for (AEBPageE2 page : info.getAebPageE2()) {
            double value = _aebListItemFacade.getValuationRadioDaysByZe(page.getZe(),
                    info.getYear());
            if (page.getValuationRadioDay() != value) {
                addMessage("Ze: " + page.getZe()
                        + ": Unterschiedliche Werte. Eingetragen: "
                        + page.getValuationRadioDay()
                        + " Katalog: "
                        + value
                );
                page.setValuationRadioDay(value);
            }
        }
    }

    private void addMessage(String message) {
        setMessage(getMessage() + message + "\n");
    }

}
