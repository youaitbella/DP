package org.inek.dataportal.psy.nub.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;
import org.junit.jupiter.api.Test;

import java.util.List;

class PsyNubRequestMergeHelperTest {

    @Test
    void compareProposalsWithNoCollisionsTest() {
        PsyNubRequest baseLine = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest ownRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest partnerRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());

        partnerRequest.setName("Testname");
        partnerRequest.setAltName("Testname 2");
        partnerRequest.setLastName("Nachname");

        PsyNubRequestMergeHelper helper = new PsyNubRequestMergeHelper(baseLine, ownRequest, partnerRequest);
        List<String> collisions = helper.compareProposals();

        Assertions.assertThat(collisions).hasSize(3);
        Assertions.assertThat(collisions).contains("NUB-Name").contains("Alternativer Name").contains("Nachname");
        Assertions.assertThat(ownRequest.getName()).isEqualTo("Testname");
        Assertions.assertThat(ownRequest.getAltName()).isEqualTo("Testname 2");
        Assertions.assertThat(ownRequest.getLastName()).isEqualTo("Nachname");
    }

    @Test
    void compareProposalsWithCollisionsTest() {
        PsyNubRequest baseLine = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest ownRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest partnerRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());

        ownRequest.setName("Das ist aber mein Name");
        ownRequest.setLastName("Mein Nachname");
        partnerRequest.setName("Testname");
        partnerRequest.setAltName("Testname 2");
        partnerRequest.setLastName("Nachname");

        PsyNubRequestMergeHelper helper = new PsyNubRequestMergeHelper(baseLine, ownRequest, partnerRequest);
        List<String> collisions = helper.compareProposals();

        Assertions.assertThat(collisions).hasSize(3);
        Assertions.assertThat(collisions).contains("### NUB-Name ###").contains("Alternativer Name").contains("### Nachname ###");
        Assertions.assertThat(ownRequest.getName()).isEqualTo("Testname");
        Assertions.assertThat(ownRequest.getAltName()).isEqualTo("Testname 2");
        Assertions.assertThat(ownRequest.getLastName()).isEqualTo("Nachname");
    }

    @Test
    void compareProposalsWithNoPartnerChangesTest() {
        PsyNubRequest baseLine = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest ownRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest partnerRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());

        ownRequest.setName("Testname");
        ownRequest.setAltName("Testname 2");
        ownRequest.setLastName("Nachname");

        PsyNubRequestMergeHelper helper = new PsyNubRequestMergeHelper(baseLine, ownRequest, partnerRequest);
        List<String> collisions = helper.compareProposals();

        Assertions.assertThat(collisions).hasSize(0);
    }

    @Test
    void compareProposalsDataWithNoCollisionsTest() {
        PsyNubRequest baseLine = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest ownRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest partnerRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());

        partnerRequest.getProposalData().setProxyIks("222222222");
        partnerRequest.getProposalData().setIndication("Meine Indikation");
        partnerRequest.getProposalData().setReplacement("Wird durch nichts ersetzt");

        PsyNubRequestMergeHelper helper = new PsyNubRequestMergeHelper(baseLine, ownRequest, partnerRequest);
        List<String> collisions = helper.compareProposals();

        Assertions.assertThat(collisions).hasSize(3);
        Assertions.assertThat(collisions).contains("Stellvertreter IK's").contains("Indikation").contains("Ablösung oder Ergänzung");
        Assertions.assertThat(ownRequest.getProposalData().getProxyIks()).isEqualTo("222222222");
        Assertions.assertThat(ownRequest.getProposalData().getIndication()).isEqualTo("Meine Indikation");
        Assertions.assertThat(ownRequest.getProposalData().getReplacement()).isEqualTo("Wird durch nichts ersetzt");
    }

    @Test
    void compareProposalsNumberValuesWithNoCollisionsTest() {
        PsyNubRequest baseLine = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest ownRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest partnerRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());

        partnerRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setNumber(12);
        partnerRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setComment("Das ist mein Kommentar");
        partnerRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).setNumber(356);

        PsyNubRequestMergeHelper helper = new PsyNubRequestMergeHelper(baseLine, ownRequest, partnerRequest);
        List<String> collisions = helper.compareProposals();

        Assertions.assertThat(collisions).hasSize(2);
        Assertions.assertThat(collisions).contains(PsyNubNumberFields.USED_HOSPITALS.getDescription())
                .contains(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR.getDescription());
        Assertions.assertThat(ownRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getNumber()).isEqualTo(12);
        Assertions.assertThat(ownRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getComment()).isEqualTo("Das ist mein Kommentar");
        Assertions.assertThat(ownRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).getNumber()).isEqualTo(356);
    }

    @Test
    void compareProposalsDateValuesWithNoCollisionsTest() {
        PsyNubRequest baseLine = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest ownRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest partnerRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());

        partnerRequest.getDateValue(PsyNubDateFields.IN_GERMANY).setDate("01/05");
        partnerRequest.getDateValue(PsyNubDateFields.IN_GERMANY).setComment("Das ist mein Kommentar");
        partnerRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setDate("02/05");

        PsyNubRequestMergeHelper helper = new PsyNubRequestMergeHelper(baseLine, ownRequest, partnerRequest);
        List<String> collisions = helper.compareProposals();

        Assertions.assertThat(collisions).hasSize(2);
        Assertions.assertThat(collisions).contains(PsyNubDateFields.IN_GERMANY.getDescription())
                .contains(PsyNubDateFields.IN_GERMANY.getDescription());
        Assertions.assertThat(ownRequest.getDateValue(PsyNubDateFields.IN_GERMANY).getDate()).isEqualTo("01/05");
        Assertions.assertThat(ownRequest.getDateValue(PsyNubDateFields.IN_GERMANY).getComment()).isEqualTo("Das ist mein Kommentar");
        Assertions.assertThat(ownRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getDate()).isEqualTo("02/05");
    }

    @Test
    void compareProposalsMoneyValuesWithNoCollisionsTest() {
        PsyNubRequest baseLine = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest ownRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());
        PsyNubRequest partnerRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(new Account());

        partnerRequest.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_MATERIAL).setMoney(1.356);
        partnerRequest.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_MATERIAL).setComment("Das ist mein Kommentar");
        partnerRequest.getMoneyValue(PsyNubMoneyFields.LESS_COSTS_MATERIAL).setMoney(5);

        PsyNubRequestMergeHelper helper = new PsyNubRequestMergeHelper(baseLine, ownRequest, partnerRequest);
        List<String> collisions = helper.compareProposals();

        Assertions.assertThat(collisions).hasSize(2);
        Assertions.assertThat(collisions).contains(PsyNubMoneyFields.ADD_COSTS_MATERIAL.getDescription())
                .contains(PsyNubMoneyFields.LESS_COSTS_MATERIAL.getDescription());
        Assertions.assertThat(ownRequest.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_MATERIAL).getMoney()).isEqualTo(1.356);
        Assertions.assertThat(ownRequest.getMoneyValue(PsyNubMoneyFields.ADD_COSTS_MATERIAL).getComment()).isEqualTo("Das ist mein Kommentar");
        Assertions.assertThat(ownRequest.getMoneyValue(PsyNubMoneyFields.LESS_COSTS_MATERIAL).getMoney()).isEqualTo(5);
    }
}