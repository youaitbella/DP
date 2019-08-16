package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.psy.nub.entities.*;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import java.util.Date;

public class NewPsyNubProposalHelper {

    public static PsyNubProposal createNewPsyNubProposal(Account account) {
        PsyNubProposal proposal = new PsyNubProposal();
        fillGeneralInformation(proposal, account);
        fillAccountToPsyNub(proposal, account);
        ensureNubProposalData(proposal);
        ensureNubProposalLists(proposal);
        return proposal;
    }

    private static void fillGeneralInformation(PsyNubProposal proposal, Account account) {
        proposal.setCreatedAt(new Date());
        proposal.setCreatedByAccountId(account.getId());
        proposal.setStatus(WorkflowStatus.New);
        proposal.setTargetYear(Utils.getTargetYear(Feature.NUB_PSY));
    }

    private static void ensureNubProposalLists(PsyNubProposal proposal) {
        ensureNubDateValuesList(proposal);
        ensureNubNumberValuesList(proposal);
        ensureNubMoneyValuesList(proposal);
    }

    private static void ensureNubMoneyValuesList(PsyNubProposal proposal) {
        for (PsyNubMoneyFields value : PsyNubMoneyFields.values()) {
            PsyNubProposalMoneyValue moneyValue = new PsyNubProposalMoneyValue();
            moneyValue.setField(value);
            proposal.addNewPsyNubProposalMoneyValue(moneyValue);
        }
    }

    private static void ensureNubNumberValuesList(PsyNubProposal proposal) {
        for (PsyNubNumberFields value : PsyNubNumberFields.values()) {
            PsyNubProposalNumberValue numberValue = new PsyNubProposalNumberValue();
            numberValue.setField(value);
            proposal.addNewPsyNubProposalNumberValue(numberValue);
        }
    }

    private static void ensureNubDateValuesList(PsyNubProposal proposal) {
        for (PsyNubDateFields value : PsyNubDateFields.values()) {
            PsyNubProposalDateValue dateValue = new PsyNubProposalDateValue();
            dateValue.setField(value);
            proposal.addNewPsyNubProposalDateValue(dateValue);
        }
    }

    private static void ensureNubProposalData(PsyNubProposal proposal) {
        PsyNubProposalData data = new PsyNubProposalData();
        proposal.setProposalData(data);
    }

    public static void fillAccountToPsyNub(PsyNubProposal proposal, Account account) {
        proposal.setFirstName(account.getFirstName());
        proposal.setLastName(account.getLastName());
        proposal.setPhone(account.getPhone());
        proposal.setGender(account.getGender());
        proposal.setTitle(account.getTitle());
        proposal.setRoleId(account.getRoleId());
        proposal.setStreet(account.getStreet());
        proposal.setPostalCode(account.getPostalCode());
        proposal.setFax(account.getCustomerFax());
        proposal.setEmail(account.getEmail());
        proposal.setTown(account.getTown());
    }
}
