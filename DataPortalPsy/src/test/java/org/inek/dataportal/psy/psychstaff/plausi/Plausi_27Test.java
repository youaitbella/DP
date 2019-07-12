package org.inek.dataportal.psy.psychstaff.plausi;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Plausi_27Test {


    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactTest(int wrongId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(wrongId));
        staffProof.setAdultsEffectiveCosts(500);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 0, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 0, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 2, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactAndForKidsTest(int wrongId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setForAdults(false);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(wrongId));
        staffProof.setAdultsEffectiveCosts(500);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 0, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 4, 0, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 2, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithOneWrongCorrectExceptionFactTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(2));
        staffProof.setAdultsEffectiveCosts(500);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 0, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 3, 0, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 2, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithNoSumAgreedCompleteOrBudgetTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(500);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 0, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithNoSumCompleteAgreedNoAvgTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(500);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 1, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 1, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithNoSumBudgetAgreedAndAvgOkTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(11955623);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 29.50, 90366));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 135.03, 60707));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 3, 0, 3.31, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 4, 0, 7.35, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 5, 0, 2.70, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 7, 0, 12.25, 55.937));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithNoSumBudgetAgreedAndAvgNotOkTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(119552262);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 29.50, 90366));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 135.03, 60707));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 3, 0, 3.31, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 4, 0, 7.35, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 5, 0, 2.70, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 7, 0, 12.25, 55.937));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isFalse();
    }


    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithSumCompleteAgreedAndAvgOkTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(11955623);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1,  29.50, 0,90366));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2,  135.03,0, 60707));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 3,  3.31,0, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 4,  7.35, 0,65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 5,  2.70, 0,65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 7,  12.25, 0,55.937));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithSumCompleteAgreedAndAvgNotOkTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(119556236);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 29.50,0, 90366));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 135.03,0, 60707));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 3, 3.31,0, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 4, 7.35,0, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 5, 2.70,0, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 7, 12.25,0, 55.937));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isFalse();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithSumCompleteAndBudgetOkTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(11955623);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 529.50,29.50, 90366));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 135.03,135.03, 60707));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 3, 3.31,3.31, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 4, 7.35,7.35, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 5, 2.70,2.70, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 7, 12.25,12.25, 55.937));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithWrongStateTest(int correctId) {
        Plausi_27 plausi = new Plausi_27();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setAdultsEffectiveCosts(1195562);
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(0);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 29.50,0, 90366));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 135.03,0, 60707));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 3, 3.31,0, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 4, 7.35,0, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 5, 2.70,0, 65675));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 7, 12.25,0, 55.937));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

}