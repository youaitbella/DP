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
import org.inek.dataportal.psy.khcomparison.entity.AEBPageE1_2;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CompareAEBPageE1_2Test {

    public CompareAEBPageE1_2Test() {

    }

    @Test
    public void compareDifferentCountEntrysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE1_2> list1 = new ArrayList<>();
        List<AEBPageE1_2> list2 = new ArrayList<>();

        list1.add(createNewPage("", 0));
        list1.add(createNewPage("", 0));
        list2.add(createNewPage("", 0));

        com.compareAEBPageE1_2(list1, list2);
        Assertions.assertThat(com.getResult()).contains("Blatt E1.2: Unterschiedliche Anzahl von Datensätzen");

        com.setResult("");

        list2.add(createNewPage("", 0));
        Assertions.assertThat(com.getResult()).isEmpty();
    }

    @Test
    public void compareDifferentEtTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE1_2> list1 = new ArrayList<>();
        List<AEBPageE1_2> list2 = new ArrayList<>();

        list1.add(createNewPage("ET01", 0));
        list1.add(createNewPage("ET02", 0));
        list2.add(createNewPage("ET01", 0));
        list2.add(createNewPage("ET03", 0));

        com.compareAEBPageE1_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.2: Element nicht vorhanden: ET: ET02\nBlatt E1.2: Element nicht vorhanden: ET: ET03");
        com.setResult("");

        list1.add(createNewPage("ET03", 0));
        list2.add(createNewPage("ET04", 0));

        com.compareAEBPageE1_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.2: Element nicht vorhanden: ET: ET02\nBlatt E1.2: Element nicht vorhanden: ET: ET04\n");

    }

    @Test
    public void compareDifferentCalculationDaysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE1_2> list1 = new ArrayList<>();
        List<AEBPageE1_2> list2 = new ArrayList<>();

        list1.add(createNewPage("ET01", 10));
        list2.add(createNewPage("ET01", 10));

        com.compareAEBPageE1_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("ET02", 10));
        list2.add(createNewPage("ET02", 15));

        com.compareAEBPageE1_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.2 - Spalte 2 - ET02: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("ET03", 10));
        list2.add(createNewPage("ET03", 30));

        com.compareAEBPageE1_2(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E1.2 - Spalte 2 - ET02: Unterschied von ", "Blatt E1.2 - Spalte 2 - ET03: Unterschied von");
    }

    public AEBPageE1_2 createNewPage(String et, int calculationDays) {
        AEBPageE1_2 page = new AEBPageE1_2();
        page.setEt(et);
        page.setCalculationDays(calculationDays);
        return page;
    }

}
