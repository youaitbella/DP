package org.inek.dataportal.psy.psychstaff.plausi;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Plausi_63Test {


    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactTest(int wrongId) {
        Plausi_63 plausi = new Plausi_63();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(wrongId));
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 0, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 0, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 2, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactAndForKidsTest(int wrongId) {
        Plausi_63 plausi = new Plausi_63();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(false);
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(wrongId));
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
        Plausi_63 plausi = new Plausi_63();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(2));
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 0, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 3, 0, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 2, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithNoSumAgreedCompleteOrBudgetTest(int correctId) {
        Plausi_63 plausi = new Plausi_63();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 50, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 0, 0, 0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 50, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithNoSumCompleteAgreedNoAvgTest(int correctId) {
        Plausi_63 plausi = new Plausi_63();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(10);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 50, 0, "Gruppe 1"));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 0, 1, 0, "Gruppe 2"));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 60000, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isFalse();

        String errorMessage = plausi.getErrorMessage();
        Assertions.assertThat(errorMessage).contains("Gruppe 1");
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithWrongStateTest(int correctId) {
        Plausi_63 plausi = new Plausi_63();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));
        staffProof.setStatusApx1(0);
        staffProof.setStatusApx2(0);

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 29.50,0, 90366));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 135.03,0, 60707));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 2, 2, 2, 2));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

}