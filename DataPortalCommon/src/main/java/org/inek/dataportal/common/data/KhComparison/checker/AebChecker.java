/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.checker;

import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;
import org.inek.dataportal.common.helper.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lautenti
 */
public class AebChecker {

    private static final String MESSAGE_NOT_IN_CATALOG_PEPP = "%s: Pepp [%s] Vergütungsklasse [%s] Bewertungsrelation [%s] ist nicht im " +
            "Katalog %s oder im Katalog %s vorhanden";

    private static final String MESSAGE_NOT_IN_CATALOG_ET_ZE = "%s: Eintrag [%s] Bewertungsrelation [%s] ist nicht im Katalog %s oder " +
            "im Katalog %s vorhanden";

    private static final String MESSAGE_NO_VALID_PEPP = "%s: Eintrag [%s] ist keine gültige Pepp";
    private static final String MESSAGE_NO_VALID_ET = "%s: Eintrag [%s] ist kein gültiges ET";
    private static final String MESSAGE_NO_VALID_ZE = "%s: Eintrag [%s] ist kein gültiges ZE";

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
        checkPersonalHints(info);
        return getMessage().isEmpty();
    }

    private void checkPersonalHints(AEBBaseInformation info) {
        Map<String, Integer> allowedMaxValues = new HashMap<>();

        allowedMaxValues.put("1", 150000);
        allowedMaxValues.put("3", 120000);
        allowedMaxValues.put("2", 90000);
        allowedMaxValues.put("4", 90000);
        allowedMaxValues.put("5", 90000);
        allowedMaxValues.put("6", 90000);
        allowedMaxValues.put("3-6", 90000);
        allowedMaxValues.put("7", 90000);

        for (PersonalAgreed personalAgreed : info.getPersonalAgreed()) {
            Integer allowedMaxValue = allowedMaxValues.get(personalAgreed.getOccupationalCategory().getNumber());

            if (allowedMaxValue != null) {
                if (personalAgreed.getAverageCost() > allowedMaxValue) {
                    addMessage("Personalausstattung: In der Berufsgruppe [" + personalAgreed.getOccupationalCategory() + "] " +
                            "erscheint die Angabe zu den Durchschnittskosten je VK in Euro [" + personalAgreed.getAverageCost() + "] unplausibel.");
                }
            }
        }
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

        if (info.getAebPageB1().getBasisRenumerationValueNoCompensation() < 50 ||
                info.getAebPageB1().getBasisRenumerationValueNoCompensation() > 600) {
            addMessage("Blatt B1: Der Wert Nr. 19 erscheint unplausibel. Bitte prüfen und korrigieren Sie ggf. Ihre Eingabe.");
        }

        if (round(info.getAebPageB1().getSumValuationRadioRenumeration() / info.getAebPageB1().getSumEffectivValuationRadio())
                != round(info.getAebPageB1().getBasisRenumerationValueCompensation()) && (info.getAebPageB1().getSumEffectivValuationRadio() > 0)) {
            addMessage("Blatt B1: Nr. 18 ist ungleich Nr. 16 / Nr. 17");
        }
    }

    private double round(double value) {
        return MathHelper.round(value, 2);
    }

    private void checkPageE3_1(AEBBaseInformation info) {
        List<AEBPageE3_1> peppsForRemove = new ArrayList<>();

        for (AEBPageE3_1 page : info.getAebPageE3_1()) {
            if (!RenumerationChecker.isFormalValidPepp(page.getRenumeration())) {
                peppsForRemove.add(page);
                addMessage(createNoValidPeppMessage(page));
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
                addMessage(createNoValidZeMessage(page));
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
                addMessage(createNoValidPeppMessage(page));
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
        int puelCount = 0;
        int pkorCount = 0;
        for (AEBPageE1_1 page : info.getAebPageE1_1()) {
            if ("PUEL".equals(page.getPepp())) {
                page.setIsOverlyer(true);
                page.setCompensationClass(1);
                page.setCaseCount(1);
                page.setCalculationDays(1);
                puelCount++;
                continue;
            }

            if ("PKOR".equals(page.getPepp())) {
                page.setCompensationClass(1);
                page.setCaseCount(1);
                page.setCalculationDays(1);
                pkorCount++;
                continue;
            }

            if (!RenumerationChecker.isFormalValidPepp(page.getPepp())) {
                peppsForRemove.add(page);
                addMessage(createNoValidPeppMessage(page));
                continue;
            }
            if (!_aebListItemFacade.existPageCombinationInYear(page, info.getYear())) {
                if (!_aebListItemFacade.existPageCombinationInYear(page, info.getYear() - 1)) {
                    peppsForRemove.add(page);
                    addMessage(createNotInCatalogPeppMessage(info, page));
                } else {
                    page.setIsOverlyer(true);
                }
            }
        }
        if (_removeWrongEntries) {
            info.getAebPageE1_1().removeAll(peppsForRemove);
        }
        if (puelCount > 1) {
            addMessage("Die Pseudo-PEPP PUEL (Summe Überlieger) wurde mehrfach angegeben.");
        }

        if (pkorCount > 1) {
            addMessage("Die Pseudo-PEPP PKOR wurde mehrfach angegeben.");
        }
    }

    private void checkPageE1_2(AEBBaseInformation info) {

        List<AEBPageE1_2> etForRemove = new ArrayList<>();

        for (AEBPageE1_2 page : info.getAebPageE1_2()) {
            if (!RenumerationChecker.isFormalValidEt(page.getEt())) {
                etForRemove.add(page);
                addMessage(createNoValidEtMessage(page));
                continue;
            }
            if (!_aebListItemFacade.existPageCombinationInYear(page, info.getYear())) {
                if (!_aebListItemFacade.existPageCombinationInYear(page, info.getYear() - 1)) {
                    etForRemove.add(page);
                    addMessage(createNotInCatalogEtMessage(info, page));
                } else {
                    page.setIsOverlyer(true);
                }
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
                addMessage(createNoValidZeMessage(page));
                continue;
            }
            if (!_aebListItemFacade.existPageCombinationInYear(page, info.getYear())) {
                if (!_aebListItemFacade.existPageCombinationInYear(page, info.getYear() - 1)) {
                    zeForRemove.add(page);
                    addMessage(createNotInCatalogZeMessage(info, page));
                } else {
                    page.setIsOverlyer(true);
                }
            }
        }
        if (_removeWrongEntries) {
            info.getAebPageE2().removeAll(zeForRemove);
        }
    }

    private String createNotInCatalogPeppMessage(AEBBaseInformation info, AEBPageE1_1 page) {
        return String.format(MESSAGE_NOT_IN_CATALOG_PEPP, page.getImportetFrom(), page.getPepp(), page.getCompensationClass(),
                page.getValuationRadioDay(), info.getYear(), info.getYear() - 1);
    }

    private String createNotInCatalogEtMessage(AEBBaseInformation info, AEBPageE1_2 page) {
        return String.format(MESSAGE_NOT_IN_CATALOG_ET_ZE, page.getImportetFrom(), page.getEt(), page.getValuationRadioDay(),
                info.getYear(), info.getYear() - 1);
    }

    private String createNotInCatalogZeMessage(AEBBaseInformation info, AEBPageE2 page) {
        return String.format(MESSAGE_NOT_IN_CATALOG_ET_ZE, page.getImportetFrom(), page.getZe(),
                page.getValuationRadioDay(), info.getYear(), info.getYear() - 1);
    }

    private String createNoValidPeppMessage(AEBPageE1_1 page) {
        return String.format(MESSAGE_NO_VALID_PEPP, page.getImportetFrom(), page.getPepp());
    }

    private String createNoValidPeppMessage(AEBPageE3_1 page) {
        return String.format(MESSAGE_NO_VALID_PEPP, page.getImportetFrom(), page.getRenumeration());
    }

    private String createNoValidPeppMessage(AEBPageE3_3 page) {
        return String.format(MESSAGE_NO_VALID_PEPP, page.getImportetFrom(), page.getRenumeration());
    }

    private String createNoValidEtMessage(AEBPageE1_2 page) {
        return String.format(MESSAGE_NO_VALID_ET, page.getImportetFrom(), page.getEt());
    }

    private String createNoValidZeMessage(AEBPageE2 page) {
        return String.format(MESSAGE_NO_VALID_ZE, page.getImportetFrom(), page.getZe());
    }

    private String createNoValidZeMessage(AEBPageE3_2 page) {
        return String.format(MESSAGE_NO_VALID_ZE, page.getImportetFrom(), page.getZe());
    }


    private void addMessage(String message) {
        setMessage(getMessage() + message + "\n");
    }

}
