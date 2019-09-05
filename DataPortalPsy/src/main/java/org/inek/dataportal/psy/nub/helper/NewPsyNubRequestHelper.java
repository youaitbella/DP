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

public class NewPsyNubRequestHelper {

    public static PsyNubRequest createNewPsyNubRequest(Account account) {
        PsyNubRequest proposal = new PsyNubRequest();
        proposal.setStatus(WorkflowStatus.New);
        fillGeneralInformation(proposal, account);
        fillAccountToPsyNub(proposal, account);
        ensureNubRequestData(proposal);
        ensureNubRequestLists(proposal);
        return proposal;
    }

    public static PsyNubRequest createNewPsyNubRequestFromPsyNubRequest(PsyNubRequest request, Account account) {
        PsyNubRequest newProposal = createNewPsyNubRequest(account);
        copy(newProposal, request);
        return newProposal;
    }

    public static void copy(PsyNubRequest targetRequest, PsyNubRequest sourceRequest) {
        targetRequest.setDisplayName(sourceRequest.getDisplayName());
        targetRequest.setName(sourceRequest.getName());
        targetRequest.setAltName(sourceRequest.getAltName());

        targetRequest.getProposalData().setDescription(sourceRequest.getProposalData().getDescription());
        targetRequest.getProposalData().setHasNoProcs(sourceRequest.getProposalData().getHasNoProcs());
        targetRequest.getProposalData().setProcs(sourceRequest.getProposalData().getProcs());

        targetRequest.getProposalData().setProcsComment(sourceRequest.getProposalData().getProcsComment());

        targetRequest.getProposalData().setIndication(sourceRequest.getProposalData().getIndication());
        targetRequest.getProposalData().setReplacement(sourceRequest.getProposalData().getReplacement());

        targetRequest.getProposalData().setWhatsNew(sourceRequest.getProposalData().getWhatsNew());
        targetRequest.getProposalData().setLos(sourceRequest.getProposalData().getLos());

        targetRequest.getProposalData().setPepps(sourceRequest.getProposalData().getPepps());
        targetRequest.getProposalData().setWhyNotRepresented(sourceRequest.getProposalData().getWhyNotRepresented());

        targetRequest.getDateValue(PsyNubDateFields.IN_GERMANY).setDate(
                sourceRequest.getDateValue(PsyNubDateFields.IN_GERMANY).getDate());
        targetRequest.getDateValue(PsyNubDateFields.IN_GERMANY).setComment(
                sourceRequest.getDateValue(PsyNubDateFields.IN_GERMANY).getComment());

        targetRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setDate(
                sourceRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getDate());
        targetRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setComment(
                sourceRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getComment());

        targetRequest.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).setDate(
                sourceRequest.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).getDate());
        targetRequest.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).setComment(
                sourceRequest.getDateValue(PsyNubDateFields.INTRODUCED_HOSPITAL).getComment());

        targetRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setNumber(
                sourceRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getNumber());
        targetRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setComment(
                sourceRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getComment());

        if (sourceRequest.getTargetYear() == targetRequest.getTargetYear() - 1) {
            // copy Only from Prev year
            targetRequest.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).setNumber(
                    sourceRequest.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).getNumber());
            targetRequest.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).setComment(
                    sourceRequest.getNumberValue(PsyNubNumberFields.PATIENTS_TARGETYEAR).getComment());

            targetRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).setNumber(
                    sourceRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).getNumber());
            targetRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).setComment(
                    sourceRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_TARGETYEAR).getComment());

            targetRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).setNumber(
                    sourceRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).getNumber());
            targetRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).setComment(
                    sourceRequest.getNumberValue(PsyNubNumberFields.PATIENTS_PRE_PRE_TARGETYEAR).getComment());
        }

        if (targetRequest.getCreatedByAccountId() != sourceRequest.getCreatedByAccountId()) {
            targetRequest.setHelperId(sourceRequest.getCreatedByAccountId());
        }
    }

    private static void fillGeneralInformation(PsyNubRequest request, Account account) {
        request.setCreatedAt(new Date());
        request.setCreatedByAccountId(account.getId());
        request.setLastModifiedAt(new Date());
        request.setLastChangedByAccountId(account.getId());
        request.setStatus(WorkflowStatus.New);
        request.setTargetYear(Utils.getTargetYear(Feature.NUB_PSY));
    }

    private static void ensureNubRequestLists(PsyNubRequest request) {
        ensureNubDateValuesList(request);
        ensureNubNumberValuesList(request);
        ensureNubMoneyValuesList(request);
    }

    private static void ensureNubMoneyValuesList(PsyNubRequest request) {
        for (PsyNubMoneyFields value : PsyNubMoneyFields.values()) {
            PsyNubRequestMoneyValue moneyValue = new PsyNubRequestMoneyValue();
            moneyValue.setField(value);
            request.addNewPsyNubProposalMoneyValue(moneyValue);
        }
    }

    private static void ensureNubNumberValuesList(PsyNubRequest request) {
        for (PsyNubNumberFields value : PsyNubNumberFields.values()) {
            PsyNubRequestNumberValue numberValue = new PsyNubRequestNumberValue();
            numberValue.setField(value);
            request.addNewPsyNubProposalNumberValue(numberValue);
        }
    }

    private static void ensureNubDateValuesList(PsyNubRequest request) {
        for (PsyNubDateFields value : PsyNubDateFields.values()) {
            PsyNubRequestDateValue dateValue = new PsyNubRequestDateValue();
            dateValue.setField(value);
            request.addNewPsyNubProposalDateValue(dateValue);
        }
    }

    private static void ensureNubRequestData(PsyNubRequest request) {
        PsyNubRequestData data = new PsyNubRequestData();
        request.setRequestData(data);
    }

    public static void fillAccountToPsyNub(PsyNubRequest request, Account account) {
        request.setFirstName(account.getFirstName());
        request.setLastName(account.getLastName());
        request.setPhone(account.getPhone());
        request.setGender(account.getGender());
        request.setTitle(account.getTitle());
        request.setRoleId(account.getRoleId());
        request.setStreet(account.getStreet());
        request.setPostalCode(account.getPostalCode());
        request.setFax(account.getCustomerFax());
        request.setEmail(account.getEmail());
        request.setTown(account.getTown());
    }
}
