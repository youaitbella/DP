package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.ProofWardInfo;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.Period;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Dependent
public class ProofUpdater {
    private ProofFacade proofFacade;

    private BaseDataFacade baseDataFacade;

    public ProofUpdater() {
    }

    @Inject
    public ProofUpdater(ProofFacade proofFacade, BaseDataFacade baseDataFacade) {
        this.proofFacade = proofFacade;
        this.baseDataFacade = baseDataFacade;
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

        for (int month = quarter * 3 - 2; month <= quarter * 3; month++) {
            Period period = DateUtils.firstAndLastDayOfMonth(year, month);
            List<ProofWardInfo> proofWardInfos = ProofAggregator.aggregateDeptWards(deptBaseInfo.obtainCurrentWards(), period.from(), period.to());
            updateProof(proofBaseInfo, proofWardInfos);
        }
        if (originalProofBaseInfo.equals(proofBaseInfo)) {
            return;
        }
        originalProofBaseInfo.setStatus(WorkflowStatus.Retired);
        proofFacade.save(originalProofBaseInfo);
        proofFacade.save(proofBaseInfo);
    }

    private void updateProof(ProofRegulationBaseInformation proofBaseInfo, List<ProofWardInfo> proofWardInfos) {
        List<Proof> deletionList = new ArrayList<>();
        Set<ProofWardInfo> usedProofWardInfos = new HashSet<>();
        for (Proof proof : proofBaseInfo.getProofs()) {
            Optional<ProofWardInfo> proofWardInfo = proofWardInfos.stream()
                    .filter(pw -> pw.getLocationP21().equals(proof.getProofWard().getLocationP21()))
                    .filter(pw -> pw.getLocationNumber() == proof.getProofWard().getLocationNumber())
                    .filter(pw -> pw.getWardName().equals(proof.getProofWard().getName()))
                    .filter(pw -> pw.getFrom().equals(proof.getValidFrom()))
                    .filter(pw -> pw.getTo().equals(proof.getValidTo()))
                    .findAny();
            if (!proofWardInfo.isPresent()) {
                deletionList.add(proof);
                continue;
            }
            updateProof(proof, proofWardInfo.get());
            usedProofWardInfos.add(proofWardInfo.get());

        }
        proofBaseInfo.removeProofs(deletionList);
        proofWardInfos.removeAll(usedProofWardInfos);

        for (ProofWardInfo proofWardInfo : proofWardInfos) {
            int month = DateUtils.month(proofWardInfo.getFrom());
            for (Shift shift : Shift.values()) {
                proofBaseInfo.addProof(
                        ProofHelper.fillProof(new Proof(proofBaseInfo), proofWardInfo, month, shift, proofFacade, baseDataFacade));
            }
        }
    }

    private void updateProof(Proof proof, ProofWardInfo proofWardInfo) {
        proof.setDeptNumbers(proofWardInfo.depts());
        proof.setDeptNames(proofWardInfo.deptNames());
        proof.setSensitiveDomains(proofWardInfo.sensitiveDomains());
        int year = proof.getBaseInformation().getYear();
        SensitiveDomain sensitiveDomain = baseDataFacade.determineSignificantDomain(year, proofWardInfo.sensitiveDomainSet());
        proof.setSignificantSensitiveDomain(sensitiveDomain);
    }

}
