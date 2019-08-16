package org.inek.dataportal.psy.nub.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.psy.nub.entities.PsyNubProposal;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;
import org.junit.jupiter.api.Test;

class NewPsyNubProposalHelperTest {

    @Test
    void createNewPsyNubProposalAllDateValuesTest() {
        Account acc = new Account();
        acc.setId(10);
        PsyNubProposal newPsyNubProposal = NewPsyNubProposalHelper.createNewPsyNubProposal(acc);

        for (PsyNubDateFields value : PsyNubDateFields.values()) {
            Assertions.assertThat(newPsyNubProposal.getDateValue(value).getField()).as(value.name()).isEqualTo(value);
        }
    }

    @Test
    void createNewPsyNubProposalAllNumberValuesTest() {
        Account acc = new Account();
        acc.setId(10);
        PsyNubProposal newPsyNubProposal = NewPsyNubProposalHelper.createNewPsyNubProposal(acc);

        for (PsyNubNumberFields value : PsyNubNumberFields.values()) {
            Assertions.assertThat(newPsyNubProposal.getNumberValue(value).getField()).as(value.name()).isEqualTo(value);
        }
    }

    @Test
    void createNewPsyNubProposalAllMoneyValuesTest() {
        Account acc = new Account();
        acc.setId(10);
        PsyNubProposal newPsyNubProposal = NewPsyNubProposalHelper.createNewPsyNubProposal(acc);

        for (PsyNubMoneyFields value : PsyNubMoneyFields.values()) {
            Assertions.assertThat(newPsyNubProposal.getMoneyValue(value).getField()).as(value.name()).isEqualTo(value);
        }
    }

    @Test
    void fillAccountToPsyNubTest() {
        Account acc = new Account();
        acc.setFirstName("Max");
        acc.setLastName("Mustermann");
        acc.setPhone("+ 49 (0) 126587");
        acc.setGender(1);
        acc.setTitle("Dr.");
        acc.setRoleId(3);
        acc.setStreet("Musterstraße 1");
        acc.setPostalCode("01358");
        acc.setCustomerFax("+46 (5) 989312");
        acc.setEmail("Muster@Max.Test");
        acc.setTown("Siegburg");

        PsyNubProposal proposal = new PsyNubProposal();

        NewPsyNubProposalHelper.fillAccountToPsyNub(proposal, acc);

        Assertions.assertThat(proposal.getFirstName()).as("Firstname").isEqualTo(acc.getFirstName());
        Assertions.assertThat(proposal.getLastName()).as("Lastname").isEqualTo(acc.getLastName());
        Assertions.assertThat(proposal.getPhone()).as("Phone").isEqualTo(acc.getPhone());
        Assertions.assertThat(proposal.getGender()).as("Gender").isEqualTo(acc.getGender());
        Assertions.assertThat(proposal.getTitle()).as("Title").isEqualTo(acc.getTitle());
        Assertions.assertThat(proposal.getRoleId()).as("RoleId").isEqualTo(acc.getRoleId());
        Assertions.assertThat(proposal.getStreet()).as("Street").isEqualTo(acc.getStreet());
        Assertions.assertThat(proposal.getPostalCode()).as("Zip").isEqualTo(acc.getPostalCode());
        Assertions.assertThat(proposal.getFax()).as("Fax").isEqualTo(acc.getCustomerFax());
        Assertions.assertThat(proposal.getEmail()).as("Email").isEqualTo(acc.getEmail());
        Assertions.assertThat(proposal.getTown()).as("Town").isEqualTo(acc.getTown());
    }
}