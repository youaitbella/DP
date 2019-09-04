package org.inek.dataportal.common.data.KhComparison.helper;

import org.inek.dataportal.common.data.KhComparison.checker.RenumerationChecker;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_1;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_2;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE2;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;

import java.util.stream.Collectors;

/**
 * @author lautenti
 */
public class AebCheckerHelper {

    public static Boolean baseInfoisComplete(AEBBaseInformation info) {
        if (info.getIk() != 0 && info.getYear() != 0) {
            return true;
        }
        return false;
    }

    public static void ensureValuationRadios(AEBBaseInformation info, AEBListItemFacade facade) {
        for (AEBPageE1_1 page : info.getAebPageE1_1().stream().filter(c -> !c.isIsOverlyer()).collect(Collectors.toList())) {
            if (!RenumerationChecker.isPseudoPepp(page.getPepp())) {
                page.setValuationRadioDay(facade.getValuationRadioDaysByPepp(page.getPepp(),
                        page.getCompensationClass(), info.getYear()));
            }
        }

        for (AEBPageE1_2 page : info.getAebPageE1_2().stream().filter(c -> !c.isIsOverlyer()).collect(Collectors.toList())) {
            page.setValuationRadioDay(facade.getValuationRadioDaysByEt(page.getEt(),
                    info.getYear()));
        }

        for (AEBPageE2 page : info.getAebPageE2().stream().filter(c -> !c.isIsOverlyer()).collect(Collectors.toList())) {
            page.setValuationRadioDay(facade.getValuationRadioDaysByZe(page.getZe(),
                    info.getYear()));
        }
    }

}
