package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.psy.nub.entities.*;

public class PsyNubRequestHistoryMergeHelper {

    public static void copyHistoryToRequest(PsyNubRequest request, PsyNubRequestHistory requestHistory) {
        setGeneralData(request);
        copyGeneralData(request, requestHistory);
        copyRequestData(request, requestHistory);
        copyNumberValues(request, requestHistory);
        copyDateValues(request, requestHistory);
        copyMoneyValues(request, requestHistory);
        copyDocuments(request, requestHistory);
    }

    private static void setGeneralData(PsyNubRequest request) {
        request.setStatus(WorkflowStatus.Taken);
    }

    private static void copyGeneralData(PsyNubRequest request, PsyNubRequestHistory requestHistory) {
        request.setLastChangedByAccountId(requestHistory.getLastChangedByAccountId());
        request.setHelperId(requestHistory.getHelperId());
        request.setIk(requestHistory.getIk());
        request.setGender(requestHistory.getGender());
        request.setRoleId(requestHistory.getRoleId());
        request.setLastModifiedAt(requestHistory.getLastModifiedAt());
        request.setDisplayName(requestHistory.getDisplayName());
        request.setName(requestHistory.getName());
        request.setAltName(requestHistory.getAltName());
        request.setIkName(requestHistory.getIkName());
        request.setFirstName(requestHistory.getFirstName());
        request.setLastName(requestHistory.getLastName());
        request.setDivision(requestHistory.getDivision());
        request.setStreet(requestHistory.getStreet());
        request.setPostalCode(requestHistory.getPostalCode());
        request.setTown(requestHistory.getTown());
        request.setPhone(requestHistory.getPhone());
        request.setFax(requestHistory.getFax());
        request.setEmail(requestHistory.getEmail());
    }

    private static void copyRequestData(PsyNubRequest request, PsyNubRequestHistory requestHistory) {
        request.getProposalData().setProxyIks(requestHistory.getRequestData().getProxyIks());
        request.getProposalData().setFormFillHelper(requestHistory.getRequestData().getFormFillHelper());
        request.getProposalData().setUserComment(requestHistory.getRequestData().getUserComment());
        request.getProposalData().setDescription(requestHistory.getRequestData().getDescription());
        request.getProposalData().setProcs(requestHistory.getRequestData().getProcs());
        request.getProposalData().setHasNoProcs(requestHistory.getRequestData().getHasNoProcs());
        request.getProposalData().setProcsComment(requestHistory.getRequestData().getProcsComment());
        request.getProposalData().setIndication(requestHistory.getRequestData().getIndication());
        request.getProposalData().setReplacement(requestHistory.getRequestData().getReplacement());
        request.getProposalData().setWhatsNew(requestHistory.getRequestData().getWhatsNew());
        request.getProposalData().setLos(requestHistory.getRequestData().getLos());
        request.getProposalData().setPepps(requestHistory.getRequestData().getPepps());
        request.getProposalData().setWhyNotRepresented(requestHistory.getRequestData().getWhyNotRepresented());
        request.getProposalData().setFormerRequest(requestHistory.getRequestData().getFormerRequest());
        request.getProposalData().setFormerExternalId(requestHistory.getRequestData().getFormerExternalId());
    }

    private static void copyDocuments(PsyNubRequest request, PsyNubRequestHistory requestHistory) {
        request.clearDocuments();
        for (PsyNubRequestHistoryDocument requestDocument : requestHistory.getRequestDocuments()) {
            PsyNubRequestDocument doc = new PsyNubRequestDocument();
            doc.setContent(requestDocument.getContent());
            doc.setName(requestDocument.getName());
            request.addDocument(doc);
        }
    }

    private static void copyMoneyValues(PsyNubRequest request, PsyNubRequestHistory requestHistory) {
        for (PsyNubRequestHistoryMoneyValue requestMoneyValue : requestHistory.getRequestMoneyValues()) {
            request.getMoneyValue(requestMoneyValue.getField()).setMoney(requestMoneyValue.getMoney());
            request.getMoneyValue(requestMoneyValue.getField()).setComment(requestMoneyValue.getComment());
        }
    }

    private static void copyDateValues(PsyNubRequest request, PsyNubRequestHistory requestHistory) {
        for (PsyNubRequestHistoryDateValue requestDateValue : requestHistory.getRequestDateValues()) {
            request.getDateValue(requestDateValue.getField()).setDate(requestDateValue.getDate());
            request.getDateValue(requestDateValue.getField()).setComment(requestDateValue.getComment());
        }
    }

    private static void copyNumberValues(PsyNubRequest request, PsyNubRequestHistory requestHistory) {
        for (PsyNubRequestHistoryNumberValue requestNumberValue : requestHistory.getRequestNumberValues()) {
            request.getNumberValue(requestNumberValue.getField()).setNumber(requestNumberValue.getNumber());
            request.getNumberValue(requestNumberValue.getField()).setComment(requestNumberValue.getComment());
        }
    }
}
