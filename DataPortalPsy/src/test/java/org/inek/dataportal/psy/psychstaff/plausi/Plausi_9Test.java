package org.inek.dataportal.psy.psychstaff.plausi;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;
import org.inek.dataportal.psy.psychstaff.entity.ExclusionFact;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class Plausi_9Test {


    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactTest(int wrongId) {
        Plausi_9 plausi = new Plausi_9();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 1, 2, 3, 4, 5, 7, 8 })
    void isPlausiCheckOkWithWrongExceptionFactAndForKidsTest(int wrongId) {
        Plausi_9 plausi = new Plausi_9();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setForAdults(false);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(wrongId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCorrectExceptionFactTest(int correctId) {
        Plausi_9 plausi = new Plausi_9();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isFalse();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCorrectExceptionFactAndIsForKidsTest(int correctId) {
        Plausi_9 plausi = new Plausi_9();

        StaffProof staffProof = new StaffProof();
        staffProof.setForKids(true);
        staffProof.setForAdults(false);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 1, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Kids, 2, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCompleteTest(int correctId) {
        Plausi_9 plausi = new Plausi_9();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 1, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 0, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithBudgetTest(int correctId) {
        Plausi_9 plausi = new Plausi_9();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 0, 0, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 0, 1, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( ints= { 0 })
    void isPlausiCheckOkWithCompleteAndBudgetTest(int correctId) {
        Plausi_9 plausi = new Plausi_9();

        StaffProof staffProof = new StaffProof();
        staffProof.setForAdults(true);
        staffProof.setExclusionFact1(PlausiTestHelper.getExclusionFact(correctId));

        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 1, 1, 1, 0));
        staffProof.addStaffProofAgreed(PlausiTestHelper.createStaffProofAgreed(PsychType.Adults, 2, 1, 1, 0));

        Assertions.assertThat(plausi.isPlausiCheckOk(staffProof)).isTrue();
    }
}