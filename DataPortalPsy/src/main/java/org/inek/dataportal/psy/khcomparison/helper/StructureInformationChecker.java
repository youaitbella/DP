package org.inek.dataportal.psy.khcomparison.helper;

import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.enums.StructureInformationCategorie;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class StructureInformationChecker {

    private static final String NEED_VALUE = "Geben Sie eine Anzahl Planbetten bzw. teilstationärer Therapieplätze an.";

    public static String checkStructureBaseInformation(StructureBaseInformation info) {
        return checkStructureBaseInformation(info, createCurrentDate());
    }

    public static String checkStructureBaseInformation(StructureBaseInformation info, Date currentDate) {
        String error = "";

        List<StructureInformation> structureInformations = info.getStructureInformations().stream()
                .filter(c -> c.getStructureCategorie().equals(StructureInformationCategorie.BedCount)
                || c.getStructureCategorie().equals(StructureInformationCategorie.TherapyPartCount))
                .collect(Collectors.toList());

        if (structureInformations.size() == 0 || allStructureInformationsEmpty(structureInformations)) {
            error = NEED_VALUE;
        }

        if (structureInformations.stream().allMatch(c -> c.getValidFrom().after(currentDate))) {
            error = NEED_VALUE;
        }

        return error;
    }

    private static boolean allStructureInformationsEmpty(List<StructureInformation> structureInformations) {
        boolean hasValue = false;
        for (StructureInformation structureInformation : structureInformations) {
            if ("".equals(structureInformation.getContent()) && "".equals(structureInformation.getComment())) {
                hasValue = true;
            }
        }
        return hasValue;
    }

    private static Date createCurrentDate() {
        LocalDate date = LocalDate.now();

        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
