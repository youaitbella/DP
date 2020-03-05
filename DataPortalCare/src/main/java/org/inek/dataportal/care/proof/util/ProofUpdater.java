package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

@Dependent
public class ProofUpdater {
    private ProofFacade proofFacade;

    public ProofUpdater() {
    }

    @Inject
    public ProofUpdater(ProofFacade proofFacade) {
        this.proofFacade = proofFacade;
    }

    public void updateProof(DeptBaseInformation deptBaseInformation) {
        try {
            ProofRegulationBaseInformation proofBaseInfo = proofFacade.retrieveCurrent(deptBaseInformation.getIk());
            updateProof(proofBaseInfo, deptBaseInformation);
        } catch (EntityNotFoundException e) {
            return;
        }

    }

    private void updateProof(ProofRegulationBaseInformation proofBaseInfo, DeptBaseInformation deptBaseInformation) {
    }

}
