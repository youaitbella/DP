package org.inek.dataportal.psy.psychstaff.plausi;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Plausi_22Test {


    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactTest(int wrongId) {
        Plausi_22 plausi = new Plausi_22();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(wrongId));

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 0, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactAndForKidsTest(int wrongId) {
        Plausi_22 plausi = new Plausi_22();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(false);
        staffProof.setForAdults(true);
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(wrongId));

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 0, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCorrectExceptionFactTest(int correctId) {
        Plausi_22 plausi = new Plausi_22();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 1, 0, 0,0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 2, 1, 0, 0,0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCorrectExceptionFact2Test(int correctId) {
        Plausi_22 plausi = new Plausi_22();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 0, 0, 0,0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 2, 1, 0, 0,0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCorrectExceptionFactAndIsForKidsTest(int correctId) {
        Plausi_22 plausi = new Plausi_22();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(false);
        staffProof.setForAdults(true);
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 1, 0, 0, 0,0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Adults, 2, 0, 0, 0,0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithNoCompleteTest(int correctId) {
        Plausi_22 plausi = new Plausi_22();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setExclusionFact2(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 1, 0, 0, 0,0));
        staffProof.addStaffProofEffective(PlausiTestHelper.createStaffProofEffective(PsychType.Kids, 2, 0, 0, 0,0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isFalse();
    }
}