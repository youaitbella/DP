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
import org.inek.dataportal.psy.khcomparison.entity.AEBPageE1_1;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CompareAEBPageE1_1Test {

    public CompareAEBPageE1_1Test() {

    }

    @Test
    public void compareDifferentCountEntrysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE1_1> list1 = new ArrayList<>();
        List<AEBPageE1_1> list2 = new ArrayList<>();

        list1.add(createNewPage("", 0, 0, 0));
        list1.add(createNewPage("", 0, 0, 0));
        list2.add(createNewPage("", 0, 0, 0));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).contains("Blatt E1.1: Unterschiedliche Anzahl von Datensätzen");

        com.setResult("");

        list2.add(createNewPage("", 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();
    }

    @Test
    public void compareDifferentCountPeppsTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE1_1> list1 = new ArrayList<>();
        List<AEBPageE1_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P00A2", 0, 0, 0));
        list1.add(createNewPage("P00A2", 1, 0, 0));
        list2.add(createNewPage("P00A2", 0, 0, 0));
        list2.add(createNewPage("P00A3", 0, 0, 0));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.1: Element nicht vorhanden: PEPP: P00A2 | 1/nBlatt E1.1: Element nicht vorhanden: PEPP: P00A3 | 0");
        com.setResult("");

        list1.add(createNewPage("P00A2", 2, 0, 0));
        list2.add(createNewPage("P00A3", 1, 0, 0));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.1: Element nicht vorhanden: PEPP: P00A2 | 1/nBlatt E1.1: Element nicht vorhanden: PEPP: P00A2 | 2/nBlatt E1.1: Element nicht vorhanden: PEPP: P00A3 | 0");

    }

    @Test
    public void compareDifferentCaseCountTest() {
        KhComparisonComparator com = new KhComparisonComparator(10);

        List<AEBPageE1_1> list1 = new ArrayList<>();
        List<AEBPageE1_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P00A2", 1, 10, 0));
        list2.add(createNewPage("P00A2", 1, 11, 0));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P00A2", 2, 10, 0));
        list2.add(createNewPage("P00A2", 2, 15, 0));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.1 - Spalte 3 - P00A2-2: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P00A3", 2, 10, 0));
        list2.add(createNewPage("P00A3", 2, 30, 0));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.1 - Spalte 3 - P00A2-2: Unterschied von ", "Blatt E1.1 - Spalte 3 - P00A3-2: Unterschied von");
    }

    @Test
    public void compareDifferentCalculationDaysTest() {
        KhComparisonComparator com = new KhComparisonComparator(10);

        List<AEBPageE1_1> list1 = new ArrayList<>();
        List<AEBPageE1_1> list2 = new ArrayList<>();

        list1.add(createNewPage("P00A2", 1, 0, 10));
        list2.add(createNewPage("P00A2", 1, 0, 11));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P00A2", 2, 0, 10));
        list2.add(createNewPage("P00A2", 2, 0, 15));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.1 - Spalte 4 - P00A2-2: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P00A3", 2, 0, 10));
        list2.add(createNewPage("P00A3", 2, 0, 30));

        com.compareAEBPageE1_1(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.1 - Spalte 4 - P00A2-2: Unterschied von ", "Blatt E1.1 - Spalte 4 - P00A3-2: Unterschied von");
    }

    public AEBPageE1_1 createNewPage(String pepp, int compensationClass, int caseCount, int calculationDays) {
        AEBPageE1_1 page = new AEBPageE1_1();
        page.setPepp(pepp);
        page.setCompensationClass(compensationClass);
        page.setCaseCount(caseCount);
        page.setCalculationDays(calculationDays);
        return page;
    }

}
