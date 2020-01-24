package org.inek.dataportal.common.data.KhComparison.checker;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageB1;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AebCheckerTest {

    @Test
    void checkPageB1Test() {
        AEBBaseInformation baseInfo = new AEBBaseInformation();

        AEBPageE1_1 pageE1 = new AEBPageE1_1();
        pageE1.setCalculationDays(1);
        pageE1.setValuationRadioDay(42292.6681);
        baseInfo.addAebPageE1_1(pageE1);

        AEBPageB1 pageb1 = new AEBPageB1();
        pageb1.setSumValuationRadioRenumeration(9745620.2300);
        pageb1.setSumEffectivValuationRadio(42292.6681);
        pageb1.setBasisRenumerationValueCompensation(230.4300);


        baseInfo.setAebPageB1(pageb1);

        AebChecker checker = new AebChecker(null, false, false);
        checker.checkPageB1(baseInfo);
        
        Assertions.assertThat(checker.getMessage()).isNotEmpty();

    }
}