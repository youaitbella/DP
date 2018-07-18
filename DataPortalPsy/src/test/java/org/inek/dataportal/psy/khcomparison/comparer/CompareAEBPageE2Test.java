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
import org.inek.dataportal.psy.khcomparison.entity.AEBPageE2;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CompareAEBPageE2Test {

    public CompareAEBPageE2Test() {

    }

    @Test
    public void compareDifferentCountEntrysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE2> list1 = new ArrayList<>();
        List<AEBPageE2> list2 = new ArrayList<>();

        list1.add(createNewPage("", 0));
        list1.add(createNewPage("", 0));
        list2.add(createNewPage("", 0));

        com.compareAEBPageE2(list1, list2);
        Assertions.assertThat(com.getResult()).contains("Blatt E2: Unterschiedliche Anzahl von Datensätzen");

        com.setResult("");

        list2.add(createNewPage("", 0));
        Assertions.assertThat(com.getResult()).isEmpty();
    }

    @Test
    public void compareDifferentZeTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE2> list1 = new ArrayList<>();
        List<AEBPageE2> list2 = new ArrayList<>();

        list1.add(createNewPage("ZP2018-30", 0));
        list1.add(createNewPage("ZP2018-31", 0));
        list2.add(createNewPage("ZP2018-30", 0));
        list2.add(createNewPage("ZP2018-33", 0));

        com.compareAEBPageE2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E2: Element nicht vorhanden: Ze: ZP2018-31\nBlatt E2: Element nicht vorhanden: Ze: ZP2018-33");
        com.setResult("");

        list1.add(createNewPage("ZP2018-33", 0));
        list2.add(createNewPage("ZP2018-34", 0));

        com.compareAEBPageE2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E2: Element nicht vorhanden: Ze: ZP2018-31\nBlatt E2: Element nicht vorhanden: Ze: ZP2018-34\n");

    }

    @Test
    public void compareDifferentZeCountTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE2> list1 = new ArrayList<>();
        List<AEBPageE2> list2 = new ArrayList<>();

        list1.add(createNewPage("ZP2018-30", 10));
        list2.add(createNewPage("ZP2018-30", 10));

        com.compareAEBPageE2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("ZP2018-32", 10));
        list2.add(createNewPage("ZP2018-32", 15));

        com.compareAEBPageE2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E2 - Spalte 2 - ZP2018-32: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("ZP2018-33", 10));
        list2.add(createNewPage("ZP2018-33", 30));

        com.compareAEBPageE2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E2 - Spalte 2 - ZP2018-32: Unterschied von ", "Blatt E2 - Spalte 2 - ZP2018-33: Unterschied von");
    }

    public AEBPageE2 createNewPage(String ze, int calculationDays) {
        AEBPageE2 page = new AEBPageE2();
        page.setZe(ze);
        page.setZeCount(calculationDays);
        return page;
    }

}
