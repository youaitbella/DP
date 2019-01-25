/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.checker;

import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lautenti
 */
public class AebChecker {

    private AEBListItemFacade _aebListItemFacade;

    private String _message = "";

    public AebChecker(AEBListItemFacade facade) {
        _aebListItemFacade = facade;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        this._message = message;
    }

    public Boolean checkAeb(AEBBaseInformation info) {
        checkPageE1_1(info);
        checkPageE1_2(info);
        checkPageE2(info);
        checkPageE3_1(info);
        checkPageE3_2(info);
        checkPageE3_3(info);
        return getMessage().isEmpty();
    }

    private void checkPageE3_1(AEBBaseInformation info) {
        List<AEBPageE3_1> peppsForRemove = new ArrayList<>();

        for (AEBPageE3_1 page : info.getAebPageE3_1()) {
            if (!RenumerationChecker.isFormalValidPepp(page.getRenumeration())) {
                peppsForRemove.add(page);
                addMessage("Eintrag " + page.getRenumeration() + " ist keine gültige Pepp");
                continue;
            }
        }
        info.getAebPageE3_1().removeAll(peppsForRemove);
    }

    private void checkPageE3_2(AEBBaseInformation info) {
        List<AEBPageE3_2> zeForRemove = new ArrayList<>();

        for (AEBPageE3_2 page : info.getAebPageE3_2()) {
            if (!RenumerationChecker.isFormalValidZe(page.getZe())) {
                zeForRemove.add(page);
                addMessage("Eintrag " + page.getZe() + " ist keine gültiges ZE");
                continue;
            }
        }
        info.getAebPageE3_2().removeAll(zeForRemove);
    }

    private void checkPageE3_3(AEBBaseInformation info) {
        List<AEBPageE3_3> zeForRemove = new ArrayList<>();

        for (AEBPageE3_3 page : info.getAebPageE3_3()) {
            if (!RenumerationChecker.isFormalValidPepp(page.getRenumeration())) {
                zeForRemove.add(page);
                addMessage("Eintrag " + page.getRenumeration() + " ist keine gültige Pepp");
                continue;
            }
        }
        info.getAebPageE3_3().removeAll(zeForRemove);
    }

    private void checkPageE1_1(AEBBaseInformation info) {

        List<AEBPageE1_1> peppsForRemove = new ArrayList<>();

        for (AEBPageE1_1 page : info.getAebPageE1_1()) {
            if (!RenumerationChecker.isFormalValidPepp(page.getPepp())) {
                peppsForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag " + page.getPepp() + " ist keine gültige Pepp");
                continue;
            }
            double value = _aebListItemFacade.getValuationRadioDaysByPepp(page.getPepp(),
                    page.getCompensationClass(),
                    info.getYear());
            if (page.getValuationRadioDay() != value) {
                addMessage(page.getImportetFrom() + ": Pepp: " + page.getPepp() + " - "
                        + page.getCompensationClass()
                        + ": Unterschiedliche Werte. Eingetragen: "
                        + page.getValuationRadioDay()
                        + " Katalog: "
                        + value
                );
                page.setValuationRadioDay(value);
            }
        }

        info.getAebPageE1_1().removeAll(peppsForRemove);
    }

    private void checkPageE1_2(AEBBaseInformation info) {

        List<AEBPageE1_2> etForRemove = new ArrayList<>();

        for (AEBPageE1_2 page : info.getAebPageE1_2()) {
            if (!RenumerationChecker.isFormalValidEt(page.getEt())) {
                etForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag " + page.getEt() + " ist kein gültiges ET");
                continue;
            }
            double value = _aebListItemFacade.getValuationRadioDaysByEt(page.getEt(),
                    info.getYear());
            if (page.getValuationRadioDay() != value) {
                addMessage(page.getImportetFrom() + ": ET: " + page.getEt()
                        + ": Unterschiedliche Werte. Eingetragen: "
                        + page.getValuationRadioDay()
                        + " Katalog: "
                        + value
                );
                page.setValuationRadioDay(value);
            }
        }

        info.getAebPageE1_2().removeAll(etForRemove);
    }

    private void checkPageE2(AEBBaseInformation info) {

        List<AEBPageE2> zeForRemove = new ArrayList<>();

        for (AEBPageE2 page : info.getAebPageE2()) {
            if (!RenumerationChecker.isFormalValidZe(page.getZe())) {
                zeForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag " + page.getZe() + " ist kein gültiges ZE");
                continue;
            }
            double value = _aebListItemFacade.getValuationRadioDaysByZe(page.getZe(),
                    info.getYear());
            if (page.getValuationRadioDay() != value) {
                addMessage(page.getImportetFrom() + ": Ze: " + page.getZe()
                        + ": Unterschiedliche Werte. Eingetragen: "
                        + page.getValuationRadioDay()
                        + " Katalog: "
                        + value
                );
                page.setValuationRadioDay(value);
            }
        }

        info.getAebPageE2().removeAll(zeForRemove);
    }

    private void addMessage(String message) {
        setMessage(getMessage() + message + "\n");
    }

}
