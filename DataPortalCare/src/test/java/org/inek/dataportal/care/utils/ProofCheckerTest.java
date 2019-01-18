package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofExceptionFact;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
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

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2)).isEmpty();

        proof1.addExceptionFact(fact2);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2))
                .hasSize(1)
                .containsOnly("Für eine oder mehrere Stationen sind zuviele Ausnahmetatbestände angegeben");

        proof1.removeExceptionFact(fact3);

        Assertions.assertThat(ProofChecker.proofIsReadyForSave(baseInfo, 2))
                .hasSize(1)
                .containsOnly("Für eine oder mehrere Stationen sind doppelte Ausnahmetatbestände angegeben");
    }

    @Test
    void proofIsReadyForSendTest() {
        ProofRegulationBaseInformation baseInfo = new ProofRegulationBaseInformation();

        Proof proof1 = new Proof();

        ProofRegulationStation station = new ProofRegulationStation();
        station.setStationName("Station H1");

        proof1.setProofRegulationStation(station);
        proof1.setShift(Shift.DAY);
        proof1.setMonth(Months.JANUARY);

        Proof proof2 = new Proof();

        ProofRegulationStation station2 = new ProofRegulationStation();
        station2.setStationName("Station H2");

        proof2.setProofRegulationStation(station2);
        proof2.setShift(Shift.NIGHT);
        proof2.setMonth(Months.JANUARY);

        baseInfo.addProof(proof1);
        baseInfo.addProof(proof2);

        Assertions.assertThat(ProofChecker.proofIsReadyForSend(baseInfo, 0)).isEmpty();

        proof1.setNurse(0);
        proof1.setPatientOccupancy(1);

        Assertions.assertThat(ProofChecker.proofIsReadyForSend(baseInfo, 0))
                .hasSize(1)
                .containsOnly("Station: Station H1 Monat: Januar Schicht: Tag: Pflegekräfte = 0 aber Anzahl Patienten > 0");

        proof1.setCountShiftNotRespected(5);

        Assertions.assertThat(ProofChecker.proofIsReadyForSend(baseInfo, 0))
                .hasSize(2)
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: Pflegekräfte = 0 aber Anzahl Patienten > 0")
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: Anzahl Schichten < Anzahl Schichten nicht eingehalten");

        proof2.setCountShift(0);
        proof2.setNurse(5);

        Assertions.assertThat(ProofChecker.proofIsReadyForSend(baseInfo, 0))
                .hasSize(3)
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: Pflegekräfte = 0 aber Anzahl Patienten > 0")
                .contains("Station: Station H1 Monat: Januar Schicht: Tag: Anzahl Schichten < Anzahl Schichten nicht eingehalten")
                .contains("Station: Station H2 Monat: Januar Schicht: Nacht: Anzahl Schichten = 0 aber Pflegekräfte > 0");
    }
}