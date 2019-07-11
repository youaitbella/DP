package org.inek.dataportal.psy.psychstaff.plausi;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Plausi_12Test {


    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactTest(int wrongId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactAndForAdultsTest(int wrongId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(false);
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 1, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCorrectExceptionFactTest(int correctId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCorrectExceptionFactAndIsForKidsTest(int correctId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(false);
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 1, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 2, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCompleteTest(int correctId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 1, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 0, 0, 1));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithBudgetNoCostTest(int correctId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 0, 1, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isFalse();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCompleteAndBudgetNoCostsTest(int correctId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 1, 1, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 1, 1, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isFalse();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCompleteAndBudgetCostsTest(int correctId) {
        Plausi_12 plausi = new Plausi_12();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 1, 1, 1));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 1, 1, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }
}