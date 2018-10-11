/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.helper;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.enums.StructureInformationCategorie;

/**
 *
 * @author lautenti
 */
public class StructureinformationHelperTest {

    public StructureinformationHelperTest() {
    }

    @Test
    public void newDateChangeOrderNoOrderChange() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(1, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isFalse();
    }

    @Test
    public void newDateChangeOrderWithOrderChange() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(1, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(4, 1, 2018), structureInformations)).isTrue();
    }

    @Test
    public void newDateChangeOrderNoOrderChangeDoubleDate() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isFalse();
    }

    @Test
    public void newDateChangeOrderWithOrderChangeDoubleDateIsOldDate() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(1, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(10, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isTrue();
    }

    @Test
    public void newDateChangeOrderWithOrderChangeDoubleDate() {
        List<StructureInformation> structureInformations = new ArrayList<>();
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(5, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(10, 1, 2018), ""));
        structureInformations.add(createNewInfo(createDate(8, 1, 2018), ""));

        Assertions.assertThat(StructureinformationHelper.newDateChangeOrder(createDate(5, 1, 2018),
                createDate(11, 1, 2018), structureInformations)).isTrue();
    }

    @Test
    public void checkForDuplicatedDatesWithNoDuplicates() {
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
    public void checkForDuplicatedDatesWithDuplicates() {
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
    public void checkForDuplicatedDatesWithDuplicatesIn2Categories() {
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
