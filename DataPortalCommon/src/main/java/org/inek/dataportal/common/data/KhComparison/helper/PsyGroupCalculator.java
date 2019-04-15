package org.inek.dataportal.common.data.KhComparison.helper;

import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_1;
import org.inek.dataportal.common.data.KhComparison.enums.PsyGroup;

public class PsyGroupCalculator {

    private static final String MATCHER_KJP = "(?i)^PK.+|P002Z|^TK.+";
    private static final String MATCHER_GENNERAL = "(?i)^PA.+|^P003.+|^TA.+";
    private static final String MATCHER_PSY = "(?i)^PP.+|^TP.+";

    private static final double BORDER_PERCENT = 70;

    public static PsyGroup findPsyGroup(AEBBaseInformation baseInformation) {
        double sumCalculationDays = sumCalculationDays(baseInformation);
        double sumKJP = sumForMatcher(baseInformation, MATCHER_KJP);
        double sumGeneral = sumForMatcher(baseInformation, MATCHER_GENNERAL);
        double sumPsy = sumForMatcher(baseInformation, MATCHER_PSY);

        if (sumKJP / sumCalculationDays * 100 >= BORDER_PERCENT) {
            return PsyGroup.KiJu;
        } else if (sumGeneral / sumCalculationDays * 100 >= BORDER_PERCENT) {
            return PsyGroup.GenerallyPsy;
        }
        else if (sumPsy / sumCalculationDays * 100 >= BORDER_PERCENT) {
            return PsyGroup.Psychosomatic;
        }
        else {
            return PsyGroup.Other;
        }
    }

    private static double sumForMatcher(AEBBaseInformation baseInformation, String matcher) {
        return baseInformation.getAebPageE1_1().stream().filter(c -> c.getPepp().matches(matcher)).mapToInt(AEBPageE1_1::getCalculationDays).sum();
    }

    private static double sumCalculationDays(AEBBaseInformation baseInformation) {
        return baseInformation.getAebPageE1_1().stream().mapToInt(AEBPageE1_1::getCalculationDays).sum();
    }
}
