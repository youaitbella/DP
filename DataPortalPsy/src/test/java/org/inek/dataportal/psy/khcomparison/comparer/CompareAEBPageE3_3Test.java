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
import org.inek.dataportal.psy.khcomparison.entity.AEBPageE3_3;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CompareAEBPageE3_3Test {

    public CompareAEBPageE3_3Test() {

    }

    @Test
    public void compareDifferentCountEntrysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_3> list1 = new ArrayList<>();
        List<AEBPageE3_3> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "", 0, 0, 0));
        list1.add(createNewPage("P001V", "", 0, 0, 0));
        list2.add(createNewPage("P001Z", "", 0, 0, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).contains("Blatt E3.3: Unterschiedliche Anzahl von Datensätzen");
        com.setResult("");

        list2.add(createNewPage("P001Z", "", 0, 0, 0));
        Assertions.assertThat(com.getResult()).isEmpty();
    }

    @Test
    public void compareDifferentRenumerationTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_3> list1 = new ArrayList<>();
        List<AEBPageE3_3> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 0));
        list1.add(createNewPage("P001V", "P01", 0, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 0));
        list2.add(createNewPage("P001V", "P01", 0, 0, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3: Element nicht vorhanden: Entgeld: P001Z - P02/nBlatt E3.3: Element nicht vorhanden: Entgeld: P001Z - P03");
        com.setResult("");

        list1.add(createNewPage("P001Z", "P03", 0, 0, 0));
        list2.add(createNewPage("P001A", "", 0, 0, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3: Element nicht vorhanden: Entgeld: P001A - ");
    }

    @Test
    public void compareDifferentCaseCountTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_3> list1 = new ArrayList<>();
        List<AEBPageE3_3> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 10, 0, 0));
        list2.add(createNewPage("P001Z", "P02", 11, 0, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 10, 0, 0));
        list2.add(createNewPage("P001Z", "P03", 15, 0, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3 - Spalte 3 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 10, 0, 0));
        list2.add(createNewPage("P001A", "P03", 15, 0, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3 - Spalte 3 - P001Z - P03: Unterschied von ", "Blatt E3.3 - Spalte 3 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentRenumerationValueTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_3> list1 = new ArrayList<>();
        List<AEBPageE3_3> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 10, 0));
        list2.add(createNewPage("P001Z", "P02", 0, 11, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 10, 0));
        list2.add(createNewPage("P001Z", "P03", 0, 15, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3 - Spalte 5 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 10, 0));
        list2.add(createNewPage("P001A", "P03", 0, 15, 0));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3 - Spalte 5 - P001Z - P03: Unterschied von ", "Blatt E3.3 - Spalte 5 - P001A - P03: Unterschied von ");
    }

    @Test
    public void compareDifferentDaysTest() {
        KhComparisonComparator com = new KhComparisonComparator();

        List<AEBPageE3_3> list1 = new ArrayList<>();
        List<AEBPageE3_3> list2 = new ArrayList<>();

        list1.add(createNewPage("P001Z", "P02", 0, 0, 10));
        list2.add(createNewPage("P001Z", "P02", 0, 0, 11));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).isEmpty();

        list1.add(createNewPage("P001Z", "P03", 0, 0, 10));
        list2.add(createNewPage("P001Z", "P03", 0, 0, 15));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3 - Spalte 4 - P001Z - P03: Unterschied von ");
        com.setResult("");

        list1.add(createNewPage("P001A", "P03", 0, 0, 10));
        list2.add(createNewPage("P001A", "P03", 0, 0, 15));

        com.compareAEBPageE3_3(list1, list2);
        Assertions.assertThat(com.getResult()).as(com.getResult()).contains("Blatt E3.3 - Spalte 4 - P001Z - P03: Unterschied von ", "Blatt E3.3 - Spalte 4 - P001A - P03: Unterschied von ");
    }

    public AEBPageE3_3 createNewPage(String renumeration,
            String renumerationKey,
            int caseCount,
            double renumerationValue,
            int days) {
        AEBPageE3_3 page = new AEBPageE3_3();
        page.setRenumeration(renumeration);
        page.setRenumerationKey(renumerationKey);
        page.setCaseCount(caseCount);
        page.setDays(days);
        page.setRenumerationValue(renumerationValue);
        return page;
    }

}
