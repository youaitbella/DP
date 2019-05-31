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

    private Boolean _removeWrongEntries;
    private Boolean _sendCheck;

    public AebChecker(AEBListItemFacade facade, Boolean removeWrongEntries, Boolean sendCheck) {
        _aebListItemFacade = facade;
        _removeWrongEntries = removeWrongEntries;
        _sendCheck = sendCheck;
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
        if (_sendCheck) {
            checkPageB1(info);
        }
        return getMessage().isEmpty();
    }

    public Boolean checkAebHints(AEBBaseInformation info) {
        checkPageE1_1Hints(info);
        return getMessage().isEmpty();
    }

    public void checkPageB1(AEBBaseInformation info) {
        int allowedDiffernz = 5;

        double sumE1_1 = 0;
        double sumE1_2 = 0;

        for (AEBPageE1_1 pageE1_1 : info.getAebPageE1_1()) {
            sumE1_1 += pageE1_1.getSumValuationRadio();
        }

        for (AEBPageE1_2 pageE1_2 : info.getAebPageE1_2()) {
            sumE1_2 += pageE1_2.getSumValuationRadio();
        }

        if (Math.abs(sumE1_1 + sumE1_2 - info.getAebPageB1().getSumEffectivValuationRadio()) > allowedDiffernz) {
            addMessage("Blatt B1: Nr. 17 ist ungleich Summe Bewertungsrelationen E1.1 + E1.2");
        }

        if (round(info.getAebPageB1().getSumValuationRadioRenumeration() / info.getAebPageB1().getSumEffectivValuationRadio())
                != round(info.getAebPageB1().getBasisRenumerationValueCompensation()) && (info.getAebPageB1().getSumEffectivValuationRadio() > 0)) {
            addMessage("Blatt B1: Nr. 18 ist ungleich Nr. 16 / Nr. 17");
        }
    }

    private double round(double value) {
        return (double)Math.round(value * 100d) / 100d;
    }

    private void checkPageE3_1(AEBBaseInformation info) {
        List<AEBPageE3_1> peppsForRemove = new ArrayList<>();

        for (AEBPageE3_1 page : info.getAebPageE3_1()) {
            if (!RenumerationChecker.isFormalValidPepp(page.getRenumeration())) {
                peppsForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getRenumeration() + "] ist keine gültige Pepp");
                continue;
            }
        }
        if (_removeWrongEntries) {
            info.getAebPageE3_1().removeAll(peppsForRemove);
        }
    }

    private void checkPageE3_2(AEBBaseInformation info) {
        List<AEBPageE3_2> zeForRemove = new ArrayList<>();

        for (AEBPageE3_2 page : info.getAebPageE3_2()) {
            if (!RenumerationChecker.isFormalValidZe(page.getZe())) {
                zeForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getZe() + "] ist keine gültiges ZE");
                continue;
            }
        }
        if (_removeWrongEntries) {
            info.getAebPageE3_2().removeAll(zeForRemove);
        }
    }

    private void checkPageE3_3(AEBBaseInformation info) {
        List<AEBPageE3_3> zeForRemove = new ArrayList<>();

        for (AEBPageE3_3 page : info.getAebPageE3_3()) {
            if (!RenumerationChecker.isFormalValidPepp(page.getRenumeration())) {
                zeForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getRenumeration() + "] ist keine gültige Pepp");
                continue;
            }
        }
        if (_removeWrongEntries) {
            info.getAebPageE3_3().removeAll(zeForRemove);
        }
    }

    private void checkPageE1_1Hints(AEBBaseInformation info) {
        for (AEBPageE1_1 page : info.getAebPageE1_1()) {
            if (page.getCompensationClass() * page.getCaseCount() > page.getCalculationDays()) {
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getPepp() + "] Vergütungsklasse [" + page.getCompensationClass() + "]: " +
                        "Berechnungstage [" + page.getCalculationDays() + "] sind weniger als berechnete Berechnungstage " +
                        "[" + (page.getCompensationClass() * page.getCaseCount()) + "] (Vergütungsklasse * Fallzahl).");
            }
        }
    }

    private void checkPageE1_1(AEBBaseInformation info) {

        List<AEBPageE1_1> peppsForRemove = new ArrayList<>();

        for (AEBPageE1_1 page : info.getAebPageE1_1()) {
            if (!RenumerationChecker.isFormalValidPepp(page.getPepp())) {
                peppsForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getPepp() + "] ist keine gültige Pepp");
                continue;
            }
            double value = _aebListItemFacade.getValuationRadioDaysByPepp(page.getPepp(),
                    page.getCompensationClass(),
                    info.getYear());
            if (value == 0) {
                peppsForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getPepp() + "] Vergütungsklasse " +
                        "[" + page.getCompensationClass() + "] ist nicht im Katalog " + info.getYear() + " vorhanden");
                continue;
            }
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
        if (_removeWrongEntries) {
            info.getAebPageE1_1().removeAll(peppsForRemove);
        }
    }

    private void checkPageE1_2(AEBBaseInformation info) {

        List<AEBPageE1_2> etForRemove = new ArrayList<>();

        for (AEBPageE1_2 page : info.getAebPageE1_2()) {
            if (!RenumerationChecker.isFormalValidEt(page.getEt())) {
                etForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getEt() + "] ist kein gültiges ET");
                continue;
            }
            double value = _aebListItemFacade.getValuationRadioDaysByEt(page.getEt(),
                    info.getYear());
            if (value == 0) {
                etForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getEt() + "] ist nicht im Katalog " + info.getYear() + " vorhanden");
                continue;
            }
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
        if (_removeWrongEntries) {
            info.getAebPageE1_2().removeAll(etForRemove);
        }
    }

    private void checkPageE2(AEBBaseInformation info) {

        List<AEBPageE2> zeForRemove = new ArrayList<>();

        for (AEBPageE2 page : info.getAebPageE2()) {
            if (!RenumerationChecker.isFormalValidZe(page.getZe())) {
                zeForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getZe() + "] ist kein gültiges ZE");
                continue;
            }
            double value = _aebListItemFacade.getValuationRadioDaysByZe(page.getZe(),
                    info.getYear());
            if (value == 0) {
                zeForRemove.add(page);
                addMessage(page.getImportetFrom() + ": Eintrag [" + page.getZe() + "] ist nicht im Katalog " + info.getYear() + " vorhanden");
                continue;
            }
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
        if (_removeWrongEntries) {
            info.getAebPageE2().removeAll(zeForRemove);
        }
    }

    private void addMessage(String message) {
        setMessage(getMessage() + message + "\n");
    }

}
