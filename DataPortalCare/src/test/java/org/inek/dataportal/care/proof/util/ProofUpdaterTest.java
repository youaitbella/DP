package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProofUpdaterTest {

    public static final int IK = 222222222;
    public static final Date EXTENSION_REQUESTED = DateUtils.createDate(1900, 01, 01);
    public static final int ACCOUNTID = 13668;
    public static final Date CREATED_DATE = DateUtils.createDate(2020, 01, 01);
    public static final int ID = 1;
    public static final Date LAST_CHANGED = DateUtils.createDate(2020, 02, 01);
    public static final Date SEND = DateUtils.createDate(2020, 03, 01);
    public static final Date VALID_TO = DateUtils.createDate(2020, 04, 01);
    public static final String FAB1 = "3600";
    public static final String FAB2 = "1700";
    public static final String FAB3 = "1600";
    public static final String DEPT_NAME1 = "Unfallchirugie";
    public static final String DEPT_NAME2 = "Neurochirurgie";
    public static final String STATION_1 = "Station 1";
    public static final String STATION_2 = "Station 2";
    public static final int LOCATION_CODE_VZ_P21 = 0;

    @Test
    void updateProof() {
        SessionController sessionController = mock(SessionController.class);
        ProofFacade proofFacade = mock(ProofFacade.class);
        when(proofFacade.retrieveCurrent(IK)).thenReturn(buildBaseInfo());

        List<Dept> depts = new ArrayList<>();
        List<DeptWard> deptWards = new ArrayList<>();

        DeptBaseInformation deptBaseInformation = new DeptBaseInformation();

        DeptWard deptWard1 = new DeptWard();
        DeptWard deptWard2 = new DeptWard();
        DeptWard deptWard3 = new DeptWard();
        Dept dept = new Dept(deptBaseInformation);
        deptWards.add(deptWard1);
        deptWards.add(deptWard2);
        deptWards.add(deptWard3);



        deptWard1.setValidFrom(SEND);
        deptWard1.setValidTo(VALID_TO);
        deptWard1.setDept(dept);
        deptWard1.setFab(FAB1);
        deptWard1.setIsInitial(true);
        deptWard1.setLocationCodeVz(LOCATION_CODE_VZ_P21);
        deptWard1.setLocationCodeP21(LOCATION_CODE_VZ_P21);
        deptWard1.setWardName(STATION_1);
        deptWard1.setDeptName(DEPT_NAME1);
        deptWard1.setBedCount(26);
        deptWard1.setCreatedAt(CREATED_DATE);
        deptWard1.setLocation2017("01");

        deptWard2.setValidFrom(SEND);
        deptWard2.setValidTo(VALID_TO);
        deptWard2.setDept(dept);
        deptWard2.setFab(FAB2);
        deptWard2.setIsInitial(true);
        deptWard2.setLocationCodeVz(LOCATION_CODE_VZ_P21);
        deptWard2.setLocationCodeP21(LOCATION_CODE_VZ_P21);
        deptWard2.setWardName(STATION_1);
        deptWard2.setDeptName(DEPT_NAME2);
        deptWard2.setBedCount(23);
        deptWard2.setCreatedAt(CREATED_DATE);
        deptWard2.setLocation2017("01");

        deptWard3.setValidFrom(SEND);
        deptWard3.setValidTo(VALID_TO);
        deptWard3.setDept(dept);
        deptWard3.setFab(FAB3);
        deptWard3.setIsInitial(true);
        deptWard3.setLocationCodeVz(LOCATION_CODE_VZ_P21);
        deptWard3.setLocationCodeP21(LOCATION_CODE_VZ_P21);
        deptWard3.setWardName(STATION_2);
        deptWard3.setDeptName(DEPT_NAME1);
        deptWard3.setBedCount(23);
        deptWard3.setCreatedAt(CREATED_DATE);
        deptWard3.setLocation2017("01");

        dept.setDeptArea(1);
        dept.setDeptName(DEPT_NAME1);
        dept.setDeptNumber(FAB3);
        //dept.setDeptWards();
        dept.setId(1);
        dept.setLocation("Station 10");
        //dept.setSensitiveArea();

        BaseDataFacade baseDataFacade = mock(BaseDataFacade.class);

        deptBaseInformation.setIk(IK);
        deptBaseInformation.setExtensionRequested(EXTENSION_REQUESTED);
        deptBaseInformation.setCreated(CREATED_DATE);
        deptBaseInformation.setCreatedBy(ACCOUNTID);
        deptBaseInformation.setCurrentVersion(new MapVersion(ACCOUNTID));
        deptBaseInformation.setDepts(depts);
        deptBaseInformation.setId(ID);
        deptBaseInformation.setLastChangeBy(ACCOUNTID);
        deptBaseInformation.setLastChanged(LAST_CHANGED);
        deptBaseInformation.setSend(SEND);
        deptBaseInformation.setStatus(WorkflowStatus.Provided);
        deptBaseInformation.setStatusId(10);
        deptBaseInformation.setYear(2019);
        ProofUpdater updater = new ProofUpdater(sessionController, proofFacade, baseDataFacade);

        updater.updateProof(deptBaseInformation);
        // proofFacade.retrieveCurrent(deptBaseInformation.getIk())
    }

    private Optional<ProofRegulationBaseInformation> buildBaseInfo() {
        return Optional.of(new ProofRegulationBaseInformation());
    }
}
