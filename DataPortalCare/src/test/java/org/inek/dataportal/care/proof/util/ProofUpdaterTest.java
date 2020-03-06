package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
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

    @Test
    void updateProof() {
        ProofFacade proofFacade = mock(ProofFacade.class);
        when(proofFacade.retrieveCurrent(IK)).thenReturn(buildBaseInfo());

        List<Dept> depts = new ArrayList<>();

        DeptBaseInformation deptBaseInformation = new DeptBaseInformation();

        Dept dept = new Dept(deptBaseInformation);
        dept.setDeptArea(1);
        dept.setDeptName("Unfallchirugie");
        dept.setDeptNumber("1600");
        dept.setDeptWards();
        dept.setId(1);
        dept.setLocation("Station 10");
        dept.setSensitiveArea();

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