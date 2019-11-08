/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 *
 * @author lautenti
 */
public class CareValidatorTest {

    public CareValidatorTest() {
    }

    @Test
    public void testCheckForSendWithStations() {
        DeptBaseInformation info = new DeptBaseInformation();
        Dept dept1 = new Dept();
        Dept dept2 = new Dept();
        Dept dept3 = new Dept();

        dept1.setRequired(true);
        dept2.setRequired(true);
        dept3.setRequired(true);

        dept1.addNewDeptWard(new MapVersion(), new Date(), new Date());

        info.addDept(dept1);
        info.addDept(dept2);
        info.addDept(dept3);

        //Assertions.assertThat(CareValidator.checkDeptBaseinformationIsAllowedToSend(info)).isEmpty();
    }

    @Test
    public void testCheckForSendWithNoStations() {
        DeptBaseInformation info = new DeptBaseInformation();
        Dept dept1 = new Dept();
        Dept dept2 = new Dept();
        Dept dept3 = new Dept();

        dept1.setRequired(true);
        dept2.setRequired(true);
        dept3.setRequired(true);

        info.addDept(dept1);
        info.addDept(dept2);
        info.addDept(dept3);

        Assertions.assertThat(CareValidator.checkDeptBaseinformationIsAllowedToSend(info)).isNotEmpty();
    }

    @Test
    public void testCheckForSendWithNoStationsThatRequired() {
        DeptBaseInformation info = new DeptBaseInformation();
        Dept dept1 = new Dept();
        Dept dept2 = new Dept();
        Dept dept3 = new Dept();

        dept1.setRequired(false);
        dept2.setRequired(false);
        dept3.setRequired(false);

        info.addDept(dept1);
        info.addDept(dept2);
        info.addDept(dept3);

        Assertions.assertThat(CareValidator.checkDeptBaseinformationIsAllowedToSend(info)).isEmpty();
    }

    @Test
    public void testCheckForSendWithStationsThatRequired() {
        DeptBaseInformation info = new DeptBaseInformation();
        Dept dept1 = new Dept();
        Dept dept2 = new Dept();
        Dept dept3 = new Dept();

        dept1.setRequired(true);
        dept2.setRequired(false);
        dept3.setRequired(false);

        dept1.addNewDeptWard(new MapVersion(), new Date(), new Date());

        info.addDept(dept1);
        info.addDept(dept2);
        info.addDept(dept3);

        Assertions.assertThat(CareValidator.checkDeptBaseinformationIsAllowedToSend(info)).isEmpty();
    }

}
