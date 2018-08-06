/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.khcomparison.comparer;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.KhComparison.checker.AebComparer;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageB1;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CompareAEBPageB1Test {

    public CompareAEBPageB1Test() {

    }

    @Test
    public void compareDifferentEqualsTest() {
        AebComparer com = new AebComparer();

        com.compareAEBPageB1(createNewPage(0, 0, 0, 0, 0, 0, 0),
                createNewPage(0, 0, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();

        com.compareAEBPageB1(createNewPage(0, 0, 10, 0, 0, 0, 0),
                createNewPage(0, 0, 10, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();

        com.compareAEBPageB1(createNewPage(0, 0, 10, 0, 0, 25, 0),
                createNewPage(0, 0, 10, 0, 0, 25, 0));
        Assertions.assertThat(com.getResult()).isEmpty();
    }

    @Test
    public void compareDifferentTotalAgreementTest() {
        AebComparer com = new AebComparer();

        com.compareAEBPageB1(createNewPage(10, 0, 0, 0, 0, 0, 0),
                createNewPage(10, 0, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();

        com.compareAEBPageB1(createNewPage(10, 0, 0, 0, 0, 0, 0),
                createNewPage(15, 0, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Gesamtbetrag für den Vereinbarungszeitraum: Unterschied von ");
        com.setResult("");

        com.compareAEBPageB1(createNewPage(1224425, 0, 0, 0, 0, 0, 0),
                createNewPage(422782572, 0, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Gesamtbetrag für den Vereinbarungszeitraum: Unterschied von ");
    }

    @Test
    public void compareDifferentChangedTotalTest() {
        AebComparer com = new AebComparer();

        com.compareAEBPageB1(createNewPage(0, 10, 0, 0, 0, 0, 0),
                createNewPage(0, 10, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();

        com.compareAEBPageB1(createNewPage(0, 10, 0, 0, 0, 0, 0),
                createNewPage(0, 15, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Veränderter Gesamtbetrag: Unterschied von ");
        com.setResult("");

        com.compareAEBPageB1(createNewPage(0, 12244250, 0, 0, 0, 0, 0),
                createNewPage(0, 1224425123, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Veränderter Gesamtbetrag: Unterschied von ");
    }

    @Test
    public void compareDifferentChangedProceedsBudgetTest() {
        AebComparer com = new AebComparer();

        com.compareAEBPageB1(createNewPage(0, 0, 10, 0, 0, 0, 0),
                createNewPage(0, 0, 10, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();

        com.compareAEBPageB1(createNewPage(0, 0, 10, 0, 0, 0, 0),
                createNewPage(0, 0, 15, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Verändertes Erlösbudget: Unterschied von ");
        com.setResult("");

        com.compareAEBPageB1(createNewPage(0, 0, 12244250, 0, 0, 0, 0),
                createNewPage(0, 0, 1224425123, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Verändertes Erlösbudget: Unterschied von ");
    }

    @Test
    public void compareDifferentsetSumValuationRadioRenumerationTest() {
        AebComparer com = new AebComparer();

        com.compareAEBPageB1(createNewPage(0, 0, 0, 10, 0, 0, 0),
                createNewPage(0, 0, 0, 10, 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();

        com.compareAEBPageB1(createNewPage(0, 0, 0, 10, 0, 0, 0),
                createNewPage(0, 0, 0, 15, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Summe mit Bewertungsrelationen bewertete Entgelte: Unterschied von ");
        com.setResult("");

        com.compareAEBPageB1(createNewPage(0, 0, 0, 4141410, 0, 0, 0),
                createNewPage(0, 0, 0, 11414140, 0, 0, 0));
        Assertions.assertThat(com.getResult()).contains("Blatt B1 - Summe mit Bewertungsrelationen bewertete Entgelte: Unterschied von ");
    }

    public AEBPageB1 createNewPage(double total,
            double changedTotal,
            double changedBudget,
            double sumWith,
            double sumEffectiv,
            double basis,
            double nonBasis) {
        AEBPageB1 page = new AEBPageB1();
        page.setTotalAgreementPeriod(total);
        page.setChangedTotal(changedTotal);
        page.setChangedProceedsBudget(changedBudget);
        page.setSumValuationRadioRenumeration(sumWith);
        page.setSumEffectivValuationRadio(sumEffectiv);
        page.setBasisRenumerationValueCompensation(basis);
        page.setBasisRenumerationValueNoCompensation(nonBasis);
        return page;
    }

}
