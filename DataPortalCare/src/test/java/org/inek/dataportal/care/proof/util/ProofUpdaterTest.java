package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Test
    void updateProof() {
        ProofFacade proofFacade = mock(ProofFacade.class);
        when(proofFacade.retrieveCurrent(IK)).thenReturn(buildBaseInfo());

        List<Dept> depts = new ArrayList<>();
        List<DeptWard> deptWards = new ArrayList<>();

        DeptBaseInformation deptBaseInformation = new DeptBaseInformation();

        DeptWard deptWard1 = new DeptWard();
        DeptWard deptWard2 = new DeptWard();
        DeptWard deptWard3 = new DeptWard();
        Dept dept = new Dept(deptBaseInformation);

        deptWard1.setValidFrom(SEND);
        deptWard1.setValidTo(VALID_TO);
        deptWard1.setDept(dept);
        deptWard1.setFab("3600");
        deptWard1.setIsInitial(true);
        deptWard1.setLocationCodeVz(0);
        deptWard1.setLocationCodeP21(0);
        deptWard1.setWardName("Station 1");
        deptWard1.setDeptName("Unfallchirugie");
        deptWard1.setBedCount(26);
        deptWard1.setCreatedAt(CREATED_DATE);
        deptWard1.setLocation2017("01");

        deptWard2.setValidFrom(SEND);
        deptWard2.setValidTo(VALID_TO);
        deptWard2.setDept(dept);
        deptWard2.setFab("1700");
        deptWard2.setIsInitial(true);
        deptWard2.setLocationCodeVz(0);
        deptWard2.setLocationCodeP21(0);
        deptWard2.setWardName("Station 1");
        deptWard2.setDeptName("Neurochirurgie");
        deptWard2.setBedCount(23);
        deptWard2.setCreatedAt(CREATED_DATE);
        deptWard2.setLocation2017("01");

        deptWard3.setValidFrom(SEND);
        deptWard3.setValidTo(VALID_TO);
        deptWard3.setDept(dept);
        deptWard3.setFab("1700");
        deptWard3.setIsInitial(true);
        deptWard3.setLocationCodeVz(0);
        deptWard3.setLocationCodeP21(0);
        deptWard3.setWardName("Station 2");
        deptWard3.setDeptName("Unfallchirurgie");
        deptWard3.setBedCount(23);
        deptWard3.setCreatedAt(CREATED_DATE);
        deptWard3.setLocation2017("01");

        dept.setDeptArea(1);
        dept.setDeptName("Unfallchirugie");
        dept.setDeptNumber("1600");
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
        ProofUpdater updater = new ProofUpdater(proofFacade, baseDataFacade);

        updater.updateProof(deptBaseInformation);
        // proofFacade.retrieveCurrent(deptBaseInformation.getIk())
    }

    private ProofRegulationBaseInformation buildBaseInfo() {
        return new ProofRegulationBaseInformation();
    }
}
