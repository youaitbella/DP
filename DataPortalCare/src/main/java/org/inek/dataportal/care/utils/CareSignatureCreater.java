package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.common.utils.Crypt;

import java.util.List;

public class CareSignatureCreater {

    public static String createPvSignature(ProofRegulationBaseInformation baseInformation) {
        String data = "^"
                + baseInformation.getIk() + "^"
                + baseInformation.getYear() + "^"
                + baseInformation.getQuarter() + "^"
                + getProofData(baseInformation.getProofs());
        String hash64 = Crypt.getHash64("SHA-1", data);
        return "PV" + hash64;
    }

    private static String getProofData(List<Proof> proofs) {
        String data = "";
        data = proofs.stream()
                .map((proof) -> ""
                        + proof.getSignificantSensitiveDomain().getId() + "^"
                        + proof.getDeptNumbers() + "^"
                        + proof.getDeptNames() + "^"
                        + proof.getProofWard().getName() + "^"
                        + proof.getProofWard().getLocationP21() + "^"
                        + proof.getMonth().getId() + "^"
                        + proof.getShift().getId() + "^"
                        + proof.getCountShift() + "^"
                        + proof.getNurse() + "^"
                        + proof.getHelpNurse() + "^"
                        + proof.getPatientOccupancy() + "^"
                        + proof.getCountShiftNotRespected() + "^")
                .reduce(data, String::concat);
        return data;
    }
}
