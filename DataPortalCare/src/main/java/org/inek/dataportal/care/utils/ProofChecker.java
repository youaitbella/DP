package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofExceptionFact;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;

public class ProofChecker {
    public static String proofIsReadyForSave(ProofRegulationBaseInformation baseInfo, int exceptionsFactsCount) {
        for (Proof proof : baseInfo.getProofs()) {
            if (proof.getExceptionFact().size() > exceptionsFactsCount) {
                return "Für eine oder mehrere Stationen sind zuviele Ausnahmetatbestände angegeben";
            }
            for (ProofExceptionFact fact : proof.getExceptionFact()) {
                if (proof.getExceptionFact().stream().filter(c -> c.getExceptionFactId() == fact.getExceptionFactId()).count() > 1) {
                    return "Für eine oder mehrere Stationen sind doppelte Ausnahmetatbestände angegeben";
                }
            }
        }
        return "";
    }
}
