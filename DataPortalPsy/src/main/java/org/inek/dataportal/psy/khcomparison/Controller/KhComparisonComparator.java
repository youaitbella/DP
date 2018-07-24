package org.inek.dataportal.psy.khcomparison.Controller;

import org.inek.dataportal.common.data.KhComparison.entities.AEBPageB1;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE3_3;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE3_2;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_2;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_1;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE2;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE3_1;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author lautenti
 */
@SuppressWarnings("LineLength")
public class KhComparisonComparator {

    private static final double TOLERANCE = 0;

    private String _result = "";

    public String getResult() {
        return _result;
    }

    public void setResult(String result) {
        this._result = result;
    }

    public KhComparisonComparator() {
    }

    public void compare(AEBBaseInformation info1, AEBBaseInformation info2) {
        compareAEBPageE1_1(info1.getAebPageE1_1(), info2.getAebPageE1_1());
        compareAEBPageE1_2(info1.getAebPageE1_2(), info2.getAebPageE1_2());
        compareAEBPageE2(info1.getAebPageE2(), info2.getAebPageE2());
        compareAEBPageE3_1(info1.getAebPageE3_1(), info2.getAebPageE3_1());
        compareAEBPageE3_2(info1.getAebPageE3_2(), info2.getAebPageE3_2());
        compareAEBPageE3_3(info1.getAebPageE3_3(), info2.getAebPageE3_3());
        compareAEBPageB1(info1.getAebPageB1(), info2.getAebPageB1());
    }

    public void compareAEBPageE1_1(List<AEBPageE1_1> listPage1, List<AEBPageE1_1> listPage2) {
        String pageTitel = "Blatt E1.1";
        compareListSize(listPage1, listPage2, pageTitel);
        for (AEBPageE1_1 page : listPage1) {
            try {
                AEBPageE1_1 page2 = listPage2.stream()
                        .filter(c -> c.getPepp().equals(page.getPepp())
                        && c.getCompensationClass() == page.getCompensationClass())
                        .findFirst()
                        .get();

                compareInt(page.getCaseCount(), page2.getCaseCount(), pageTitel + " - Spalte 3 - " + page.getPepp() + "-" + page.getCompensationClass());
                compareInt(page.getCalculationDays(), page2.getCalculationDays(), pageTitel + " - Spalte 4 - " + page.getPepp() + "-" + page.getCompensationClass());
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: PEPP: " + page.getPepp()
                        + " | " + page.getCompensationClass());
            }
        }

        for (AEBPageE1_1 page : listPage2) {
            try {
                AEBPageE1_1 page2 = listPage1.stream()
                        .filter(c -> c.getPepp().equals(page.getPepp())
                        && c.getCompensationClass() == page.getCompensationClass())
                        .findFirst()
                        .get();
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: PEPP: " + page.getPepp()
                        + " | " + page.getCompensationClass());
            }
        }
    }

    public void compareAEBPageE1_2(List<AEBPageE1_2> listPage1, List<AEBPageE1_2> listPage2) {
        String pageTitel = "Blatt E1.2";
        compareListSize(listPage1, listPage2, pageTitel);
        for (AEBPageE1_2 page : listPage1) {
            try {
                AEBPageE1_2 page2 = listPage2.stream()
                        .filter(c -> c.getEt().equals(page.getEt()))
                        .findFirst()
                        .get();

                compareInt(page.getCalculationDays(), page2.getCalculationDays(), pageTitel + " - Spalte 2 - " + page.getEt());
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: ET: " + page.getEt());
            }
        }

        for (AEBPageE1_2 page : listPage2) {
            try {
                AEBPageE1_2 page2 = listPage1.stream()
                        .filter(c -> c.getEt().equals(page.getEt()))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: ET: " + page.getEt());
            }
        }
    }

    public void compareAEBPageE2(List<AEBPageE2> listPage1, List<AEBPageE2> listPage2) {
        String pageTitel = "Blatt E2";
        compareListSize(listPage1, listPage2, pageTitel);
        for (AEBPageE2 page : listPage1) {
            try {
                AEBPageE2 page2 = listPage2.stream()
                        .filter(c -> c.getZe().equals(page.getZe()))
                        .findFirst()
                        .get();
                compareInt(page.getZeCount(), page2.getZeCount(), pageTitel + " - Spalte 2 - " + page.getZe());
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Ze: " + page.getZe());
            }
        }

        for (AEBPageE2 page : listPage2) {
            try {
                AEBPageE2 page2 = listPage1.stream()
                        .filter(c -> c.getZe().equals(page.getZe()))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Ze: " + page.getZe());
            }
        }
    }

    public void compareAEBPageE3_1(List<AEBPageE3_1> listPage1, List<AEBPageE3_1> listPage2) {
        String pageTitel = "Blatt E3.1";
        compareListSize(listPage1, listPage2, pageTitel);
        for (AEBPageE3_1 page : listPage1) {
            try {
                AEBPageE3_1 page2 = listPage2.stream()
                        .filter(c -> c.getRenumeration().equals(page.getRenumeration())
                        && c.getRenumerationKey().equals(page.getRenumerationKey()))
                        .findFirst()
                        .get();
                compareInt(page.getCaseCount(), page2.getCaseCount(), pageTitel + " - Spalte 3 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareDouble(page.getRenumerationValue(), page2.getRenumerationValue(), pageTitel + " - Spalte 4 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareInt(page.getCaseCountDeductions(), page2.getCaseCountDeductions(), pageTitel + " - Spalte 6 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareInt(page.getDayCountDeductions(), page2.getDayCountDeductions(), pageTitel + " - Spalte 7 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareDouble(page.getDeductionPerDay(), page2.getDeductionPerDay(), pageTitel + " - Spalte 8 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareInt(page.getCaseCountSurcharges(), page2.getCaseCountSurcharges(), pageTitel + " - Spalte 10 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareInt(page.getDayCountSurcharges(), page2.getDayCountSurcharges(), pageTitel + " - Spalte 11 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareDouble(page.getSurchargesPerDay(), page2.getSurchargesPerDay(), pageTitel + " - Spalte 12 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Entgeld: " + page.getRenumeration() + " - " + page.getRenumerationKey());
            }
        }

        for (AEBPageE3_1 page : listPage2) {
            try {
                AEBPageE3_1 page2 = listPage1.stream()
                        .filter(c -> c.getRenumeration().equals(page.getRenumeration())
                        && c.getRenumerationKey().equals(page.getRenumerationKey()))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Entgeld: " + page.getRenumeration() + " - " + page.getRenumerationKey());
            }
        }
    }

    public void compareAEBPageE3_2(List<AEBPageE3_2> listPage1, List<AEBPageE3_2> listPage2) {
        String pageTitel = "Blatt E3.2";
        compareListSize(listPage1, listPage2, pageTitel);
        for (AEBPageE3_2 page : listPage1) {
            try {
                AEBPageE3_2 page2 = listPage2.stream()
                        .filter(c -> c.getZe().equals(page.getZe())
                        && c.getRenumerationKey().equals(page.getRenumerationKey())
                        && c.getOps().equals(page.getOps()))
                        .findFirst()
                        .get();
                compareInt(page.getCount(), page2.getCount(), pageTitel + " - Spalte 4 - " + page.getZe() + " - " + page.getRenumerationKey() + " - " + page.getOps());
                compareDouble(page.getRenumerationValue(), page2.getRenumerationValue(), pageTitel + " - Spalte 5 - " + page.getZe() + " - " + page.getRenumerationKey() + " - " + page.getOps());
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Zusatzentgeld: " + page.getZe() + " - " + page.getRenumerationKey() + " - " + page.getOps());
            }
        }

        for (AEBPageE3_2 page : listPage2) {
            try {
                AEBPageE3_2 page2 = listPage1.stream()
                        .filter(c -> c.getZe().equals(page.getZe())
                        && c.getRenumerationKey().equals(page.getRenumerationKey())
                        && c.getOps().equals(page.getOps()))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Zusatzentgeld: " + page.getZe() + " - " + page.getRenumerationKey() + " - " + page.getOps());
            }
        }
    }

    public void compareAEBPageE3_3(List<AEBPageE3_3> listPage1, List<AEBPageE3_3> listPage2) {
        String pageTitel = "Blatt E3.3";
        compareListSize(listPage1, listPage2, pageTitel);
        for (AEBPageE3_3 page : listPage1) {
            try {
                AEBPageE3_3 page2 = listPage2.stream()
                        .filter(c -> c.getRenumeration().equals(page.getRenumeration())
                        && c.getRenumerationKey().equals(page.getRenumerationKey()))
                        .findFirst()
                        .get();
                compareInt(page.getCaseCount(), page2.getCaseCount(), pageTitel + " - Spalte 3 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareInt(page.getDays(), page2.getDays(), pageTitel + " - Spalte 4 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
                compareDouble(page.getRenumerationValue(), page2.getRenumerationValue(), pageTitel + " - Spalte 5 - " + page.getRenumeration() + " - " + page.getRenumerationKey());
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Entgeld: " + page.getRenumeration() + " - " + page.getRenumerationKey());
            }
        }

        for (AEBPageE3_3 page : listPage2) {
            try {
                AEBPageE3_3 page2 = listPage1.stream()
                        .filter(c -> c.getRenumeration().equals(page.getRenumeration())
                        && c.getRenumerationKey().equals(page.getRenumerationKey()))
                        .findFirst()
                        .get();
            } catch (NoSuchElementException ex) {
                addMessage(pageTitel + ": Element nicht vorhanden: Entgeld: " + page.getRenumeration() + " - " + page.getRenumerationKey());
            }
        }
    }

    public void compareAEBPageB1(AEBPageB1 page1, AEBPageB1 page2) {
        String pageTitel = "Blatt B1";

        compareDouble(page1.getTotalAgreementPeriod(), page2.getTotalAgreementPeriod(), pageTitel + " - Gesamtbetrag für den Vereinbarungszeitraum");
        compareDouble(page1.getChangedTotal(), page2.getChangedTotal(), pageTitel + " - Veränderter Gesamtbetrag");
        compareDouble(page1.getChangedProceedsBudget(), page2.getChangedProceedsBudget(), pageTitel + " - Verändertes Erlösbudget");
        compareDouble(page1.getSumValuationRadioRenumeration(), page2.getSumValuationRadioRenumeration(), pageTitel + " - Summe mit Bewertungsrelationen bewertete Entgelte");
        compareDouble(page1.getSumEffectivValuationRadio(), page2.getSumEffectivValuationRadio(), pageTitel + " - Summe der effektiven Bewertungsrelationen");
        compareDouble(page1.getBasisRenumerationValueCompensation(), page2.getBasisRenumerationValueCompensation(), pageTitel + " - Krankenhausindividueller Basisentgeltwert mit Ausgleichen");
        compareDouble(page1.getBasisRenumerationValueNoCompensation(), page2.getBasisRenumerationValueNoCompensation(), pageTitel + " - Basisentgeltwert ohne Ausgleiche und Ausgleichsbeträge aus Berichtigungen");
    }

    private void addMessage(String message) {
        setResult(getResult() + message + "\n");
    }

    private void compareInt(int value1, int value2, String page) {
        double z = value1 - value2;
        z = Math.abs(z);
        double p = (value1 + value2) / 2;
        p = (z / p) * 100;
        p = Math.abs(p);
        p = Math.round(p * 100.0) / 100.0;
        if (p > TOLERANCE) {
            addMessage(page + ": Unterschied von " + p + " %");
        }
    }

    private void compareDouble(double value1, double value2, String page) {
        double z = value1 - value2;
        z = Math.abs(z);
        double p = (value1 + value2) / 2;
        p = (z / p) * 100;
        p = Math.abs(p);
        p = Math.round(p * 100.0) / 100.0;
        if (p > TOLERANCE) {
            addMessage(page + ": Unterschied von " + Math.round(p) + " %");
        }
    }

    private void compareString(String value1, String value2, String page) {
        if (!value1.toUpperCase().equals(value2.toUpperCase())) {
            addMessage(page + ": Unterschied von " + value1 + " - " + value2);
        }
    }

    private <T> void compareListSize(List<T> list1, List<T> list2, String pageTitel) {
        if (list1.size() != list2.size()) {
            addMessage(pageTitel + ": Unterschiedliche Anzahl von Datensätzen");
        }
    }
}
