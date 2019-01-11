package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofExceptionFact;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.junit.jupiter.api.Test;

class ProofCheckerTest {

    @Test
    void proofIsReadyForSaveTest() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();

        Proof proof1 = new Proof();
        Proof proof2 = new Proof();

        ProofExceptionFact fact1 = new ProofExceptionFact();
        fact1.setExceptionFactId(1);

        ProofExceptionFact fact2 = new ProofExceptionFact();
        fact2.setExceptionFactId(1);

        ProofExceptionFact fact3 = new ProofExceptionFact();
        fact3.setExceptionFactId(2);

        proof1.addExceptionFact(fact1);
        proof1.addExceptionFact(fact3);

        baseInfo.addProof(proof1);
        baseInfo.addProof(proof2);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2)).isEqualTo("");

        proof1.addExceptionFact(fact2);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2)).isEqualTo("Für eine oder mehrere Stationen sind zuviele Ausnahmetatbestände angegeben");

        proof1.removeExceptionFact(fact3);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2)).isEqualTo("Für eine oder mehrere Stationen sind doppelte Ausnahmetatbestände angegeben");
    }
}