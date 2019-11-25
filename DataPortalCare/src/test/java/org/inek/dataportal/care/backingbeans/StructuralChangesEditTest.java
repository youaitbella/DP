package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChanges;
import org.inek.dataportal.care.entities.StructuralChanges.WardsToChange;
import org.inek.dataportal.care.enums.StructuralChangesType;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StructuralChangesEditTest {
    private static final int ID1 = 1;
    private static Date DATE_1 = DateUtils.createDate(2018, Month.JANUARY, 1);
    private static Date DATE_2 = DateUtils.createDate(2019, Month.JANUARY, 1);
    private static Date DATE_3 = DateUtils.createDate(2020, Month.JANUARY, 1);

    @Test
    void processChangesTest() {

        StructuralChangesEdit edit = new StructuralChangesEdit();
        List<DeptWard> wards = new ArrayList<>();
        DeptWard ward = new DeptWard();
        ward.setId(ID1);
        ward.setValidFrom(DATE_1);
        wards.add(ward);

        List<StructuralChanges> structuralChanges = new ArrayList<>();
        StructuralChanges change = new StructuralChanges();
        change.setStructuralChangesType(StructuralChangesType.CHANGE);
        WardsToChange changeWard = new WardsToChange();
        changeWard.setDeptWard(ward);
        changeWard.setValidFrom(DATE_2);
        change.setWardsToChange(changeWard);
        structuralChanges.add(change);

        edit.processChanges(wards, structuralChanges);
        assertThat(wards.size()).isEqualTo(2);
        assertThat(wards.get(0).getValidFrom()).isEqualTo(DATE_1);
        assertThat(wards.get(0).getValidTo()).isEqualTo(DateUtils.addDays(DATE_2, -1));
        assertThat(wards.get(1).getValidFrom()).isEqualTo(DATE_2);
        assertThat(wards.get(1).getValidTo()).isEqualTo(DateUtils.getMaxDate());
    }

    @Test
    void processDeletions() {

        StructuralChangesEdit edit = new StructuralChangesEdit();
        List<DeptWard> wards = new ArrayList<>();
        DeptWard ward = new DeptWard();
        ward.setId(ID1);
        ward.setValidFrom(DATE_1);
        ward.setValidTo(DateUtils.addDays(DATE_3, -1));
        wards.add(ward);
        ward = new DeptWard();
        ward.setId(ID1);
        ward.setValidFrom(DATE_3);
        wards.add(ward);

        List<StructuralChanges> structuralChanges = new ArrayList<>();
        StructuralChanges change = new StructuralChanges();
        change.setStructuralChangesType(StructuralChangesType.CLOSE);
        WardsToChange changeWard = new WardsToChange();
        changeWard.setDeptWard(ward);
        changeWard.setValidFrom(DATE_2);
        change.setWardsToChange(changeWard);
        structuralChanges.add(change);

        edit.processDeletions(wards, structuralChanges);
        assertThat(wards.size()).isEqualTo(1);
        assertThat(wards.get(0).getValidFrom()).isEqualTo(DATE_1);
        assertThat(wards.get(0).getValidTo()).isEqualTo(DateUtils.addDays(DATE_2, -1));
    }

    @Test
    void processTemporaryDeletions() {

        StructuralChangesEdit edit = new StructuralChangesEdit();
        List<DeptWard> wards = new ArrayList<>();
        DeptWard ward = new DeptWard();
        ward.setId(ID1);
        ward.setValidFrom(DATE_1);
        ward.setValidTo(DateUtils.addDays(DATE_2, -1));
        wards.add(ward);

        ward = new DeptWard();
        ward.setId(ID1);
        ward.setValidFrom(DATE_2);
        ward.setValidTo(DateUtils.addDays(DATE_3, -1));
        wards.add(ward);

        ward = new DeptWard();
        ward.setId(ID1);
        ward.setValidFrom(DATE_3);
        wards.add(ward);

        List<StructuralChanges> structuralChanges = new ArrayList<>();
        StructuralChanges change = new StructuralChanges();
        change.setStructuralChangesType(StructuralChangesType.CLOSE_TEMP);
        WardsToChange changeWard = new WardsToChange();
        changeWard.setDeptWard(ward);
        changeWard.setValidFrom(DATE_2);
        changeWard.setValidTo(DATE_3);
        change.setWardsToChange(changeWard);
        structuralChanges.add(change);

        edit.processTempoaryDeletions(wards, structuralChanges);
        assertThat(wards.size()).isEqualTo(2);
        assertThat(wards.get(0).getValidFrom()).isEqualTo(DATE_1);
        assertThat(wards.get(0).getValidTo()).isEqualTo(DateUtils.addDays(DATE_2, -1));
        assertThat(wards.get(1).getValidFrom()).isEqualTo(DateUtils.addDays(DATE_3, 1));
        assertThat(wards.get(1).getValidTo()).isEqualTo(DateUtils.getMaxDate());
    }
}