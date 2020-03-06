package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProofUpdaterTest {

    public static final int IK = 222222222;

    @Test
    void updateProof() {
        ProofFacade proofFacade = mock(ProofFacade.class);
        when(proofFacade.retrieveCurrent(IK)).thenReturn(buildBaseInfo());

        BaseDataFacade baseDataFacade = mock(BaseDataFacade.class);

        DeptBaseInformation deptBaseInformation = new DeptBaseInformation();
        deptBaseInformation.setIk(IK);
        ProofUpdater updater = new ProofUpdater(proofFacade, baseDataFacade);

        updater.updateProof(deptBaseInformation);
        // proofFacade.retrieveCurrent(deptBaseInformation.getIk())
    }

    private ProofRegulationBaseInformation buildBaseInfo() {
        return new ProofRegulationBaseInformation();
    }
}