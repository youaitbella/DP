/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.enums.StructureInformationCategorie;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lautenti
 */
public class StructureinformationHelperTest {

    public StructureinformationHelperTest() {
    }

    @Test
    public void newDateChangeOrderNoOrderChangeTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(1, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isFalse();
    }

    @Test
    public void newDateChangeOrderWithOrderChangeTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(1, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(4, 1, 2018), structureInformations)).isTrue();
    }

    @Test
    public void newDateChangeOrderNoOrderChangeDoubleDateTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isFalse();
    }

    @Test
    public void newDateChangeOrderWithOrderChangeDoubleDateIsOldDateTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(1, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isTrue();
    }

    @Test
    public void newDateChangeOrderWithOrderChangeDoubleDateTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(5, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isTrue();
    }

    @Test
    public void checkForDuplicatedDatesWithNoDuplicatesTest() {
        StructureBaseInformation baseInfo = new StructureBaseInformation();
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), "", StructureInformationCategorie.DismissManagement));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), "", StructureInformationCategorie.SPCenterText));

        baseInfo.setStructureInformations(structureInformations);

        Assertions.assertThat(StructureinformationHelper.checkForDuplicatedDates(baseInfo)).isEmpty();
    }

    @Test
    public void checkForDuplicatedDatesWithDuplicatesTest() {
        StructureBaseInformation baseInfo = new StructureBaseInformation();
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), "", StructureInformationCategorie.DismissManagement));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), "", StructureInformationCategorie.SPCenterText));

        baseInfo.setStructureInformations(structureInformations);

        Assertions.assertThat(StructureinformationHelper.checkForDuplicatedDates(baseInfo))
                .isEqualTo(StructureInformationCategorie.RegionalCare.getArea());
    }

    @Test
    public void checkForDuplicatedDatesWithDuplicatesIn2CategoriesTest() {
        StructureBaseInformation baseInfo = new StructureBaseInformation();
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), "", StructureInformationCategorie.DismissManagement));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), "", StructureInformationCategorie.SPCenterText));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), "", StructureInformationCategorie.SPCenterText));

        baseInfo.setStructureInformations(structureInformations);

        Assertions.assertThat(StructureinformationHelper.checkForDuplicatedDates(baseInfo))
                .contains(StructureInformationCategorie.RegionalCare.getArea());

        Assertions.assertThat(StructureinformationHelper.checkForDuplicatedDates(baseInfo))
                .contains(StructureInformationCategorie.SPCenterText.getArea());
    }

    @Test
    public void structureInformationIsReadonlyWithNewEnryTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(6, 1, 2018), "", StructureInformationCategorie.RegionalCare));

        StructureInformation info1 = structureInformations.get(0);

        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info1, structureInformations, createDate(4, 1, 2018))).isFalse();
    }

    @Test
    public void structureInformationIsReadonlyWithExistEnryTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(3, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(6, 1, 2018), "", StructureInformationCategorie.RegionalCare));

        StructureInformation info01 = structureInformations.get(0);
        StructureInformation info1 = structureInformations.get(1);
        StructureInformation info2 = structureInformations.get(2);

        info01.setId(50);
        info1.setId(5);
        info2.setId(10);

        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info01, structureInformations, createDate(4, 1, 2018))).isTrue();
        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info1, structureInformations, createDate(4, 1, 2018))).isFalse();
        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info2, structureInformations, createDate(4, 1, 2018))).isFalse();
    }

    @Test
    public void structureInformationIsReadonlyWithExistEnrysAfterTodayTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(15, 1, 2018), "", StructureInformationCategorie.RegionalCare));

        StructureInformation info1 = structureInformations.get(0);
        StructureInformation info2 = structureInformations.get(1);

        info1.setId(5);
        info2.setId(100);

        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info1, structureInformations, createDate(4, 1, 2018))).isFalse();
        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info2, structureInformations, createDate(4, 1, 2018))).isFalse();
    }

    @Test
    public void structureInformationIsReadonlyWithExistEnrysAfterAndBeforeTodayTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(1, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(15, 1, 2018), "", StructureInformationCategorie.RegionalCare));

        StructureInformation info1 = structureInformations.get(0);
        StructureInformation info2 = structureInformations.get(1);

        info1.setId(5);
        info2.setId(100);

        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info1, structureInformations, createDate(4, 1, 2018))).isFalse();
        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info2, structureInformations, createDate(4, 1, 2018))).isFalse();
    }

    @Test
    public void structureInformationIsReadonlyWithExistEnrysTest() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare));
        structureInformations.add(createNewInfo(createDate(6, 1, 2018), "", StructureInformationCategorie.RegionalCare));

        StructureInformation info1 = structureInformations.get(0);
        StructureInformation info2 = structureInformations.get(1);

        info1.setId(5);

        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info1, structureInformations, createDate(4, 1, 2018))).isFalse();
        Assertions.assertThat(StructureinformationHelper.structureInformationIsReadonly(info2, structureInformations, createDate(4, 1, 2018))).isFalse();
    }

    @Test
    public void getStructureInformationsByStructureCategorieFilteredWithDateBeforeValidFromTest() {
        StructureBaseInformation baseInfo = new StructureBaseInformation();
        List<StructureInformation> structureInformations = new ArrayList<>();

        StructureInformation info1 = createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info2 = createNewInfo(createDate(4, 2, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info3 = createNewInfo(createDate(15, 2, 2018), "", StructureInformationCategorie.RegionalCare);

        structureInformations.add(info1);
        structureInformations.add(info2);
        structureInformations.add(info3);

        baseInfo.setStructureInformations(structureInformations);

        Date validFrom = createDate(1, 2, 2018);
        Date validUntil = createDate(2, 3, 2018);

        Assertions.assertThat(StructureinformationHelper.getStructureInformationsByStructureCategorieFiltered(baseInfo,
                StructureInformationCategorie.RegionalCare.name(), validFrom, validUntil)).isNotEmpty()
                .as(info1.getValidFrom().toString()).contains(info1)
                .as(info2.getValidFrom().toString()).contains(info2)
                .as(info3.getValidFrom().toString()).contains(info3)
                .size().isEqualTo(3);

        Assertions.assertThat(info1.getValidFrom()).isEqualTo(createDate(4, 1, 2018));
        Assertions.assertThat(info2.getValidFrom()).isEqualTo(createDate(4, 2, 2018));
        Assertions.assertThat(info3.getValidFrom()).isEqualTo(createDate(15, 2, 2018));
    }

    @Test
    public void getStructureInformationsByStructureCategorieFilteredWithDateEqualsValidFromTest() {
        StructureBaseInformation baseInfo = new StructureBaseInformation();
        List<StructureInformation> structureInformations = new ArrayList<>();

        StructureInformation info1 = createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info2 = createNewInfo(createDate(4, 2, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info3 = createNewInfo(createDate(15, 2, 2018), "", StructureInformationCategorie.RegionalCare);

        structureInformations.add(info1);
        structureInformations.add(info2);
        structureInformations.add(info3);

        baseInfo.setStructureInformations(structureInformations);

        Date validFrom = createDate(4, 2, 2018);
        Date validUntil = createDate(2, 3, 2018);

        Assertions.assertThat(StructureinformationHelper.getStructureInformationsByStructureCategorieFiltered(baseInfo,
                StructureInformationCategorie.RegionalCare.name(), validFrom, validUntil)).isNotEmpty()
                .as(info1.getValidFrom().toString()).doesNotContain(info1)
                .as(info2.getValidFrom().toString()).contains(info2)
                .as(info3.getValidFrom().toString()).contains(info3)
                .size().isEqualTo(2);

        Assertions.assertThat(info1.getValidFrom()).isEqualTo(createDate(4, 1, 2018));
        Assertions.assertThat(info2.getValidFrom()).isEqualTo(createDate(4, 2, 2018));
        Assertions.assertThat(info3.getValidFrom()).isEqualTo(createDate(15, 2, 2018));
    }

    @Test
    public void getStructureInformationsByStructureCategorieFilteredWithDateAftersValidToTest() {
        StructureBaseInformation baseInfo = new StructureBaseInformation();
        List<StructureInformation> structureInformations = new ArrayList<>();

        StructureInformation info1 = createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info2 = createNewInfo(createDate(4, 2, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info3 = createNewInfo(createDate(15, 2, 2018), "", StructureInformationCategorie.RegionalCare);

        structureInformations.add(info1);
        structureInformations.add(info2);
        structureInformations.add(info3);

        baseInfo.setStructureInformations(structureInformations);

        Date validFrom = createDate(4, 2, 2018);
        Date validUntil = createDate(10, 2, 2018);

        Assertions.assertThat(StructureinformationHelper.getStructureInformationsByStructureCategorieFiltered(baseInfo,
                StructureInformationCategorie.RegionalCare.name(), validFrom, validUntil)).isNotEmpty()
                .as(info1.getValidFrom().toString()).doesNotContain(info1)
                .as(info2.getValidFrom().toString()).contains(info2)
                .as(info3.getValidFrom().toString()).doesNotContain(info3)
                .size().isEqualTo(1);

        Assertions.assertThat(info1.getValidFrom()).isEqualTo(createDate(4, 1, 2018));
        Assertions.assertThat(info2.getValidFrom()).isEqualTo(createDate(4, 2, 2018));
        Assertions.assertThat(info3.getValidFrom()).isEqualTo(createDate(15, 2, 2018));
    }

    @Test
    public void getStructureInformationsByStructureCategorieFilteredWithDateAftersValidToLongRangeTest() {
        StructureBaseInformation baseInfo = new StructureBaseInformation();
        List<StructureInformation> structureInformations = new ArrayList<>();

        StructureInformation info1 = createNewInfo(createDate(4, 1, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info2 = createNewInfo(createDate(4, 2, 2018), "", StructureInformationCategorie.RegionalCare);
        StructureInformation info3 = createNewInfo(createDate(15, 2, 2019), "", StructureInformationCategorie.RegionalCare);

        structureInformations.add(info1);
        structureInformations.add(info2);
        structureInformations.add(info3);

        baseInfo.setStructureInformations(structureInformations);

        Date validFrom = createDate(1, 1, 2019);
        Date validUntil = createDate(31, 12, 2019);

        Assertions.assertThat(StructureinformationHelper.getStructureInformationsByStructureCategorieFiltered(baseInfo,
                StructureInformationCategorie.RegionalCare.name(), validFrom, validUntil)).isNotEmpty()
                .as(info1.getValidFrom().toString()).doesNotContain(info1)
                .as(info2.getValidFrom().toString()).contains(info2)
                .as(info3.getValidFrom().toString()).contains(info3)
                .size().isEqualTo(2);

        Assertions.assertThat(info1.getValidFrom()).isEqualTo(createDate(4, 1, 2018));
        Assertions.assertThat(info2.getValidFrom()).isEqualTo(createDate(4, 2, 2018));
        Assertions.assertThat(info3.getValidFrom()).isEqualTo(createDate(15, 2, 2019));
    }

    private StructureInformation createNewInfo(Date validFrom, String content, StructureInformationCategorie cat) {
        StructureInformation info = createNewInfo(validFrom, content);
        info.setStructureCategorie(cat);
        return info;
    }

    private StructureInformation createNewInfo(Date validFrom, String content) {
        StructureInformation info = new StructureInformation();
        info.setValidFrom(validFrom);
        info.setContent(content);
        return info;
    }

    private Date createDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, Month.of(month), day);

        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

}
