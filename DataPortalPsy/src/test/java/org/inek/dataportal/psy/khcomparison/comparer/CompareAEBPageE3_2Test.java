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
import org.inek.dataportal.psy.khcomparison.entity.AEBPageE3_2;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CompareAEBPageE3_2Test {

    public CompareAEBPageE3_2Test() {

    }

    @Test
    public void compareDifferentCountEntrysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_2> list1 = new ArrayList<>();
        List<AEBPageE3_2> list2 = new ArrayList<>();

        list1.add(createNewPage("ZP2018-31", "", "", 0, 0));
        list1.add(createNewPage("ZP2018-31", "", "", 0, 0));
        list2.add(createNewPage("ZP2018-32", "", "", 0, 0));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).contains("Blatt E3.2: Unterschiedliche Anzahl von Datensätzen");

        com.setResult("");

        list2.add(createNewPage("ZP2018-32", "", "", 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();
    }

    @Test
    public void compareDifferentZeTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_2> list1 = new ArrayList<>();
        List<AEBPageE3_2> list2 = new ArrayList<>();

        list1.add(createNewPage("ZP2018-31", "EP01", "6-007.15", 0, 0));
        list1.add(createNewPage("ZP2018-31", "EP02", "6-007.15", 0, 0));
        list2.add(createNewPage("ZP2018-31", "EP02", "6-007.15", 0, 0));
        list2.add(createNewPage("ZP2018-31", "EP01", "6-007.15", 0, 0));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).isEmpty();

        list1.add(createNewPage("ZP2018-31", "EP02", "6-007.14", 0, 0));
        list2.add(createNewPage("ZP2018-31", "EP02", "6-007.15", 0, 0));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.2: Element nicht vorhanden: Zusatzentgeld: ZP2018-31 - EP02 - 6-007.14");
        com.setResult("");

        list1.add(createNewPage("ZP2018-33", "EP02", "6-007.15*", 0, 0));
        list2.add(createNewPage("ZP2018-33", "EP02", "6-007.16", 0, 0));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.2: Element nicht vorhanden: Zusatzentgeld: ZP2018-31 - EP02 - 6-007.14",
                "Blatt E3.2: Element nicht vorhanden: Zusatzentgeld: ZP2018-33 - EP02 - 6-007.15*",
                "Blatt E3.2: Element nicht vorhanden: Zusatzentgeld: ZP2018-33 - EP02 - 6-007.16");
    }

    @Test
    public void compareDifferentCountTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_2> list1 = new ArrayList<>();
        List<AEBPageE3_2> list2 = new ArrayList<>();

        list1.add(createNewPage("ZP2018-31", "EP01", "6-007.15", 10, 0));
        list2.add(createNewPage("ZP2018-31", "EP01", "6-007.15", 10, 0));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("ZP2018-32", "EP01", "6-007.15", 10, 0));
        list2.add(createNewPage("ZP2018-32", "EP01", "6-007.15", 15, 0));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.2 - Spalte 4 - ZP2018-32 - EP01 - 6-007.15: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("ZP2018-32", "EP01", "6-007.16", 10, 0));
        list2.add(createNewPage("ZP2018-32", "EP01", "6-007.16", 115, 0));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.2 - Spalte 4 - ZP2018-32 - EP01 - 6-007.15: Unterschied von ",
                "Blatt E3.2 - Spalte 4 - ZP2018-32 - EP01 - 6-007.16: Unterschied von ");
    }

    @Test
    public void compareDifferentRenumerationValueTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_2> list1 = new ArrayList<>();
        List<AEBPageE3_2> list2 = new ArrayList<>();

        list1.add(createNewPage("ZP2018-31", "EP01", "6-007.15", 0, 10));
        list2.add(createNewPage("ZP2018-31", "EP01", "6-007.15", 0, 10));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("ZP2018-32", "EP01", "6-007.15", 0, 10));
        list2.add(createNewPage("ZP2018-32", "EP01", "6-007.15", 0, 15));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.2 - Spalte 5 - ZP2018-32 - EP01 - 6-007.15: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("ZP2018-32", "EP01", "6-007.16", 0, 10));
        list2.add(createNewPage("ZP2018-32", "EP01", "6-007.16", 0, 1250));

        com.compareAEBPageE3_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.2 - Spalte 5 - ZP2018-32 - EP01 - 6-007.15: Unterschied von ",
                "Blatt E3.2 - Spalte 5 - ZP2018-32 - EP01 - 6-007.16: Unterschied von ");
    }

    public AEBPageE3_2 createNewPage(String ze,
            String renumerationKey,
            String ops,
            int count,
            double renumerationValue) {
        AEBPageE3_2 page = new AEBPageE3_2();
        page.setZe(ze);
        page.setRenumerationKey(renumerationKey);
        page.setOps(ops);
        page.setCount(count);
        page.setRenumerationValue(renumerationValue);
        return page;
    }

}
