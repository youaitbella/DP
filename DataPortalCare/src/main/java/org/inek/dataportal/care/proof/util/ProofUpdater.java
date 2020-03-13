package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.facades.BaseDataFacade;
import org.inek.dataportal.care.proof.ProofFacade;
import org.inek.dataportal.care.proof.ProofWardInfo;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.Period;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import static org.inek.dataportal.api.helper.PortalConstants.VAR_IK;

@Dependent
public class ProofUpdater implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("ProofUpdater");

    private ProofFacade proofFacade;
    private BaseDataFacade baseDataFacade;
    private SessionController sessionController;

    public ProofUpdater() {
    }

    @Inject
    public ProofUpdater(SessionController sessionController, ProofFacade proofFacade, BaseDataFacade baseDataFacade) {
        this.proofFacade = proofFacade;
        this.baseDataFacade = baseDataFacade;
        this.sessionController = sessionController;
    }

    public void updateProof(DeptBaseInformation deptBaseInformation) {
        proofFacade.retrieveCurrent(deptBaseInformation.getIk()).ifPresent(pbi -> updateProof(pbi, deptBaseInformation));
    }

    private void updateProof(ProofRegulationBaseInformation originalProofBaseInfo, DeptBaseInformation deptBaseInfo) {
        ProofRegulationBaseInformation proofBaseInfo = new ProofRegulationBaseInformation(originalProofBaseInfo);
        int year = proofBaseInfo.getYear();
        int quarter = proofBaseInfo.getQuarter();
        List<ProofWardInfo> proofWardInfos = new ArrayList<>();

        for (int month = quarter * 3 - 2; month <= quarter * 3; month++) {
            Period period = DateUtils.firstAndLastDayOfMonth(year, month);
            proofWardInfos.addAll(ProofAggregator.aggregateDeptWards(deptBaseInfo.obtainCurrentWards(), period.from(), period.to()));
        }
        updateProof(proofBaseInfo, proofWardInfos);
        if (originalProofBaseInfo.contentEquals(proofBaseInfo)) {
            return;
        }
        originalProofBaseInfo.setStatus(WorkflowStatus.Retired);
        if (proofBaseInfo.getStatus() == WorkflowStatus.Provided) {
            proofBaseInfo.setStatus(WorkflowStatus.CorrectionRequested);
            sendMail(proofBaseInfo);
        }
        proofFacade.save(originalProofBaseInfo);
        proofFacade.save(proofBaseInfo);
    }

    private void sendMail(ProofRegulationBaseInformation proofBaseInfo) {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put(VAR_IK, "" + proofBaseInfo.getIk());
        sessionController.getMailer().sendMailWithTemplate("CareProofReopenAfterStructuralChange",
                substitutions, sessionController.getAccount());
    }

    private void updateProof(ProofRegulationBaseInformation proofBaseInfo, List<ProofWardInfo> proofWardInfos) {
        ProofUpdateSets updateSets = collectLists(proofBaseInfo, proofWardInfos);
        proofBaseInfo.removeProofs(updateSets.getDeletionSet());
        updateProofs(updateSets.getUpdateSet(), updateSets.getUsedProofWardInfos());
        proofWardInfos.removeAll(updateSets.getUsedProofWardInfos());
        addNewProofs(proofBaseInfo, proofWardInfos);
    }

    private ProofUpdateSets collectLists(ProofRegulationBaseInformation proofBaseInfo, List<ProofWardInfo> proofWardInfos) {
        Set<Proof> deletionSet = new HashSet<>();
        Set<Proof> updateSet = new HashSet<>();
        Set<ProofWardInfo> usedProofWardInfos = new HashSet<>();

        for (Proof proof : proofBaseInfo.getProofs()) {
            Optional<ProofWardInfo> proofWardInfo = obtainProofWardInfo(proofWardInfos, proof);
            if (!proofWardInfo.isPresent()) {
                deletionSet.add(proof);
                continue;
            }
            if (different(proof, proofWardInfo.get())) {
                updateSet.add(proof);
            }
            usedProofWardInfos.add(proofWardInfo.get());
        }
        return new ProofUpdateSets(deletionSet, updateSet, usedProofWardInfos);
    }

    private boolean different(Proof proof, ProofWardInfo proofWardInfo) {
        int year = proof.getBaseInformation().getYear();
        SensitiveDomain sensitiveDomain = baseDataFacade.determineSignificantDomain(year, proofWardInfo.sensitiveDomainSet());

        boolean different = !proof.getDeptNames().equals(proofWardInfo.deptNames())
                || !proof.getDeptNumbers().equals(proofWardInfo.depts())
                || proof.getSignificantSensitiveDomain().getId() != sensitiveDomain.getId();
        return different;
    }

    private void updateProofs(Set<Proof> proofs, Set<ProofWardInfo> proofWardInfos) {
        for (Proof proof : proofs) {
            Optional<ProofWardInfo> proofWardInfo = obtainProofWardInfo(proofWardInfos, proof);
            updateProof(proof, proofWardInfo.get());
        }
    }

    private Optional<ProofWardInfo> obtainProofWardInfo(Collection<ProofWardInfo> proofWardInfos, Proof proof) {
        return proofWardInfos.stream()
                .filter(pw -> pw.getLocationP21().equals(proof.getProofWard().getLocationP21()))
                .filter(pw -> pw.getLocationNumber() == proof.getProofWard().getLocationNumber())
                .filter(pw -> pw.getWardName().equals(proof.getProofWard().getName()))
                .filter(pw -> pw.getFrom().equals(proof.getValidFrom()))
                .filter(pw -> pw.getTo().equals(proof.getValidTo()))
                .findAny();
    }

    private void addNewProofs(ProofRegulationBaseInformation proofBaseInfo, List<ProofWardInfo> proofWardInfos) {
        for (ProofWardInfo proofWardInfo : proofWardInfos) {
            int month = DateUtils.month(proofWardInfo.getFrom());
            for (Shift shift : Shift.reversedValues()) {
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
