package org.inek.dataportal.psy.khcomparison.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.enums.StructureInformationCategorie;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

class StructureInformationCheckerTest {

    @Test
    public void checkStructureBaseInformation() {
        StructureBaseInformation baseInformation = new StructureBaseInformation();

        Assertions.assertThat(StructureInformationChecker.checkStructureBaseInformation(baseInformation, createDate(1, 1, 2018))).isNotEqualTo("");

        baseInformation.addStructureInformation(createNewStructureInformation(StructureInformationCategorie.BedCount,
                createDate(1, 1, 2019),
                "",
                ""));
        Assertions.assertThat(StructureInformationChecker.checkStructureBaseInformation(baseInformation, createDate(1, 1, 2018))).isNotEqualTo("");

        baseInformation.getStructureInformations().get(0).setContent("1");

        Assertions.assertThat(StructureInformationChecker.checkStructureBaseInformation(baseInformation, createDate(1, 1, 2018))).isNotEqualTo("");

        Assertions.assertThat(StructureInformationChecker.checkStructureBaseInformation(baseInformation, createDate(1, 1, 2019))).isEqualTo("");
    }

    @Test
    public void checkStructureBaseInformationNotValideToday() {
        StructureBaseInformation baseInformation = new StructureBaseInformation();

        baseInformation.addStructureInformation(createNewStructureInformation(StructureInformationCategorie.BedCount,
                createDate(1, 1, 2019),
                "1",
                ""));
        Assertions.assertThat(StructureInformationChecker.checkStructureBaseInformation(baseInformation, createDate(1, 1, 2019))).isEqualTo("");
    }

    private StructureInformation createNewStructureInformation(StructureInformationCategorie cat, Date validFrom, String value, String comment) {
        StructureInformation info = new StructureInformation();
        info.setStructureCategorie(cat);
        info.setContent(value);
        info.setValidFrom(validFrom);
        info.setComment(comment);
        return info;
    }

    private Date createDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, Month.of(month), day);

        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}