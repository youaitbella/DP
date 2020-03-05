package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.ProofWardInfo;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.Period;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.List;

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

    private void updateProof(ProofRegulationBaseInformation proofBaseInfo, DeptBaseInformation deptBaseInfo) {
        ProofRegulationBaseInformation originalProofBaseInfo = new ProofRegulationBaseInformation(proofBaseInfo);
        int year = proofBaseInfo.getYear();
        int quarter = proofBaseInfo.getQuarter();
        boolean changed = false;

        for (int month = quarter * 3 - 2; month <= quarter * 3; month++) {
            Period period = DateUtils.firstAndLastDayOfMonth(year, month);
            List<ProofWardInfo> proofWardInfos = ProofAggregator.aggregateDeptWards(deptBaseInfo.obtainCurrentWards(), period.from(), period.to());
            changed |= updateProof(proofBaseInfo, proofWardInfos);
        }
        if (!changed) {
            return;
        }
        originalProofBaseInfo.setStatus(WorkflowStatus.Retired);
        proofFacade.save(originalProofBaseInfo);
        proofFacade.save(proofBaseInfo);
    }

    private boolean updateProof(ProofRegulationBaseInformation proofBaseInfo, List<ProofWardInfo> proofWardInfos) {
        for (ProofWardInfo proofWardInfo : proofWardInfos) {
            for (Shift shift : Shift.values()) {
                // todo
            }
        }
        return false; // todo
    }

}
