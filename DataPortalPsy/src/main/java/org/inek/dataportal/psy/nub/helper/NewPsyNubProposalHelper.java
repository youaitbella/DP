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

    public static PsyNubProposal createNewPsyNubProposalFromPsyNubProposal(PsyNubProposal proposal, Account account) {
        PsyNubProposal newProposal = createNewPsyNubProposal(account);
        copy(newProposal, proposal);
        return newProposal;
    }

    public static void copy(PsyNubProposal targetProposal, PsyNubProposal sourceProposal) {
        targetProposal.setDisplayName(sourceProposal.getDisplayName());
        targetProposal.setName(sourceProposal.getName());
        targetProposal.setAltName(sourceProposal.getAltName());

        targetProposal.getProposalData().setDescription(sourceProposal.getProposalData().getDescription());
        targetProposal.getProposalData().setHasNoProcs(sourceProposal.getProposalData().getHasNoProcs());
        targetProposal.getProposalData().setProcs(sourceProposal.getProposalData().getProcs());

        targetProposal.getProposalData().setProcsComment(sourceProposal.getProposalData().getProcsComment());

        targetProposal.getProposalData().setIndication(sourceProposal.getProposalData().getIndication());
        targetProposal.getProposalData().setReplacement(sourceProposal.getProposalData().getReplacement());

        targetProposal.getProposalData().setWhatsNew(sourceProposal.getProposalData().getWhatsNew());
        targetProposal.getProposalData().setLos(sourceProposal.getProposalData().getLos());

        targetProposal.getProposalData().setPepps(sourceProposal.getProposalData().getPepps());
        targetProposal.getProposalData().setWhyNotRepresented(sourceProposal.getProposalData().getWhyNotRepresented());

        targetProposal.getDateValue(PsyNubDateFields.IN_GERMANY).setDate(
                sourceProposal.getDateValue(PsyNubDateFields.IN_GERMANY).getDate());
        targetProposal.getDateValue(PsyNubDateFields.IN_GERMANY).setComment(
                sourceProposal.getDateValue(PsyNubDateFields.IN_GERMANY).getComment());

        targetProposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setDate(
                sourceProposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getDate());
        targetProposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setComment(
                sourceProposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getComment());

        targetProposal.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).setDate(
                sourceProposal.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).getDate());
        targetProposal.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).setComment(
                sourceProposal.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).getComment());

        targetProposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setNumber(
                sourceProposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getNumber());
        targetProposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setComment(
                sourceProposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getComment());

        if (sourceProposal.getTargetYear() == targetProposal.getTargetYear() - 1) {
            // copy Only from Prev year
            targetProposal.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).setNumber(
                    sourceProposal.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).getNumber());
            targetProposal.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).setComment(
                    sourceProposal.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).getComment());

            targetProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).setNumber(
                    sourceProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).getNumber());
            targetProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).setComment(
                    sourceProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).getComment());

            targetProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).setNumber(
                    sourceProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).getNumber());
            targetProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).setComment(
                    sourceProposal.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).getComment());
        }

        if (targetProposal.getCreatedByAccountId() != sourceProposal.getCreatedByAccountId()) {
            targetProposal.setHelperId(sourceProposal.getCreatedByAccountId());
        }
    }

    private static void fillGeneralInformation(PsyNubProposal proposal, Account account) {
        proposal.setCreatedAt(new Date());
        proposal.setCreatedByAccountId(account.getId());
        proposal.setLastModifiedAt(new Date());
        proposal.setLastChangedByAccountId(account.getId());
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
