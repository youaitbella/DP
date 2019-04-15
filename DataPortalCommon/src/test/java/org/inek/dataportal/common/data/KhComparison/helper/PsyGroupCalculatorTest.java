package org.inek.dataportal.common.data.KhComparison.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageE1_1;
import org.inek.dataportal.common.data.KhComparison.enums.PsyGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PsyGroupCalculatorTest {

    @Test
    void findPsyGroupWithEmptyListTest() {
        AEBBaseInformation baseInformation = new AEBBaseInformation();

        Assertions.assertThat(PsyGroupCalculator.findPsyGroup(baseInformation)).isEqualTo(PsyGroup.Other);
    }

    @Test
    void findPsyGroupWithOtherMatchTest() {
        AEBBaseInformation baseInformation = new AEBBaseInformation();
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002Z", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 200));
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002Z", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002Z", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002Z", 50));
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002Z", 30));

        Assertions.assertThat(PsyGroupCalculator.findPsyGroup(baseInformation)).isEqualTo(PsyGroup.Other);
    }

    @Test
    void findPsyGroupWithKjpMatchTest() {
        AEBBaseInformation baseInformation = new AEBBaseInformation();
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002z", 5000));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));

        Assertions.assertThat(PsyGroupCalculator.findPsyGroup(baseInformation)).isEqualTo(PsyGroup.KiJu);
    }

    @Test
    void findPsyGroupWithGenerallMatchTest() {
        AEBBaseInformation baseInformation = new AEBBaseInformation();
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002z", 5000));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("P0032", 10000));
        baseInformation.addAebPageE1_1(createNewPageE1_1("PA034", 10000));

        Assertions.assertThat(PsyGroupCalculator.findPsyGroup(baseInformation)).isEqualTo(PsyGroup.GenerallyPsy);
    }

    @Test
    void findPsyGroupWithPsyMatchTest() {
        AEBBaseInformation baseInformation = new AEBBaseInformation();
        baseInformation.addAebPageE1_1(createNewPageE1_1("P002z", 5000));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TA123", 10));
        baseInformation.addAebPageE1_1(createNewPageE1_1("TPsss", 10000));
        baseInformation.addAebPageE1_1(createNewPageE1_1("tp234", 10000));

        Assertions.assertThat(PsyGroupCalculator.findPsyGroup(baseInformation)).isEqualTo(PsyGroup.Psychosomatic);
    }


    private AEBPageE1_1 createNewPageE1_1(String pepp, int calcDays) {
        AEBPageE1_1 page = new AEBPageE1_1();
        page.setPepp(pepp);
        page.setCalculationDays(calcDays);
        return page;
    }

}