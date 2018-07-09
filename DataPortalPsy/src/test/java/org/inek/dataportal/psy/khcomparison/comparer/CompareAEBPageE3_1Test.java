/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.comparer;

import org.assertj.core.api.Assertions;
import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.psy.khcomparison.Controller.KhComparisonComparator;
import org.inek.dataportal.psy.khcomparison.entity.AEBPageE3_1;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CompareAEBPageE3_1Test {

    public CompareAEBPageE3_1Test() {

    }

    @Test
    public void compareDifferentCountEntrysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "", 0, 0, 0, 0, 0, 0, 0, 0));
        list1.add(createNewPage("P001V", "", 0, 0, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "", 0, 0, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).contains("Blatt E3.1: Unterschiedliche Anzahl von Datensätzen");

        com.setResult("");

        list2.add(createNewPage("P001V", "", 0, 0, 0, 0, 0, 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();
    }

    @Test
    public void compareDifferentRenumerationTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 0, 0, 0, 0));
        list1.add(createNewPage("P001V", "P01", 0, 0, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001V", "P01", 0, 0, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1: Element nicht vorhanden: Entgeld: P001Z - P02\nBlatt E3.1: Element nicht vorhanden: Entgeld: P001Z - P03");
        com.setResult("");

        list1.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001A", "", 0, 0, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1: Element nicht vorhanden: Entgeld: P001A - ");
    }

    @Test
    public void compareDifferentCaseCountTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 10, 0, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P02", 11, 0, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 10, 0, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 15, 0, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 3 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 10, 0, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001A", "P03", 15, 0, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 3 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 3 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentRenumerationValueTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 10, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P02", 0, 11, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 10, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 15, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 4 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 10, 0, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001A", "P03", 0, 15, 0, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 4 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 4 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentCaseCountDeductionTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 10, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P02", 0, 0, 11, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 0, 10, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 15, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 6 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 0, 10, 0, 0, 0, 0, 0));
        list2.add(createNewPage("P001A", "P03", 0, 0, 15, 0, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 6 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 6 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentDaysCountDeductionTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 0, 10, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P02", 0, 0, 0, 11, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 0, 0, 10, 0, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 0, 15, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 7 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 0, 0, 10, 0, 0, 0, 0));
        list2.add(createNewPage("P001A", "P03", 0, 0, 0, 15, 0, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 7 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 7 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentDeductionDaysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 10, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 11, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 10, 0, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 15, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 8 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 10, 0, 0, 0));
        list2.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 15, 0, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 8 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 8 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentCaseCountSurchargesTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 0, 10, 0, 0));
        list2.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 0, 11, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 10, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 115, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 10 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 0, 10, 0, 0));
        list2.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 0, 15, 0, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 10 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 10 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentDayCountSurchargesTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 0, 0, 10, 0));
        list2.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 0, 0, 11, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 0, 10, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 0, 15, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 11 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 0, 0, 10, 0));
        list2.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 0, 0, 15, 0));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 11 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 11 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentSurchargesDaysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_1> list1 = new ArrayList<>();
        List<AEBPageE3_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 0, 0, 0, 10));
        list2.add(createNewPage("P001Z", "P02", 0, 0, 0, 0, 0, 0, 0, 11));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 0, 0, 10));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 0, 0, 0, 0, 0, 15));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 12 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 0, 0, 0, 10));
        list2.add(createNewPage("P001A", "P03", 0, 0, 0, 0, 0, 0, 0, 20));

        com.compareAEBPageE3_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.1 - Spalte 12 - P001Z - P03: Unterschied von ", "Blatt E3.1 - Spalte 12 - P001A - P03: Unterschied von ");
    }

    public AEBPageE3_1 createNewPage(String renumeration,
            String renumerationKey,
            int caseCount,
            double renumerationValue,
            int caseCountAbschlag,
            int daysCountAbschlag,
            double abchlagDay,
            int caseCountZuschlag,
            int daysCountZuschlag,
            double zuschlagDay) {
        AEBPageE3_1 page = new AEBPageE3_1();
        page.setRenumeration(renumeration);
        page.setRenumerationKey(renumerationKey);
        page.setCaseCount(caseCount);
        page.setRenumerationValue(renumerationValue);
        page.setCaseCountDeductions(caseCountAbschlag);
        page.setDayCountDeductions(daysCountAbschlag);
        page.setDeductionPerDay(abchlagDay);
        page.setCaseCountSurcharges(caseCountZuschlag);
        page.setDayCountSurcharges(daysCountZuschlag);
        page.setSurchargesPerDay(zuschlagDay);
        return page;
    }

}
