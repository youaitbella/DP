package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.psy.nub.entities.PsyNubProposal;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubFieldKey;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import java.io.Serializable;
import java.util.Calendar;

public class PsyNubProposalTemplateHelper implements Serializable {

    public static String createTemplateContentFromPsyNubProposal(PsyNubProposal proposal, Account account) {
        StringBuilder content = new StringBuilder();

        appendLine(content, PsyNubFieldKey.Version, "" + proposal.getTargetYear());
        String helperId = encodeHelpId(account.getId());
        appendLine(content, PsyNubFieldKey.ID, helperId);
        String helperName = account.getTitle() + " " + account.getFirstName() + " " + account.getLastName();
        String helper = account.getCompany()
                + "\r\n" + helperName.trim()
                + "\r\n" + proposal.getProposalData().getFormFillHelper();
        appendLine(content, PsyNubFieldKey.Helper, helper);
        appendLine(content, PsyNubFieldKey.DisplayName, proposal.getDisplayName());
        appendLine(content, PsyNubFieldKey.Name, proposal.getName());
        appendLine(content, PsyNubFieldKey.AltName, proposal.getAltName());
        appendLine(content, PsyNubFieldKey.Description, proposal.getProposalData().getDescription());
        appendLine(content, PsyNubFieldKey.HasNoProcs, "" + proposal.getProposalData().getHasNoProcs());
        appendLine(content, PsyNubFieldKey.ProcCodes, proposal.getProposalData().getProcs());
        appendLine(content, PsyNubFieldKey.Procedures, proposal.getProposalData().getProcs());
        appendLine(content, PsyNubFieldKey.Indication, proposal.getProposalData().getIndication());
        appendLine(content, PsyNubFieldKey.Replacement, proposal.getProposalData().getReplacement());
        appendLine(content, PsyNubFieldKey.WhatsNew, proposal.getProposalData().getWhatsNew());
        appendLine(content, PsyNubFieldKey.Los, proposal.getProposalData().getLos());
        appendLine(content, PsyNubFieldKey.InGermanySinceDate, proposal.getDateValue(PsyNubDateFields.IN_GERMANY).getDate());
        appendLine(content, PsyNubFieldKey.InGermanySinceComment, proposal.getDateValue(PsyNubDateFields.IN_GERMANY).getComment());
        appendLine(content, PsyNubFieldKey.MedApprovedDate, proposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getDate());
        appendLine(content, PsyNubFieldKey.MedApprovedComment, proposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getComment());
        appendLine(content, PsyNubFieldKey.HospitalCountNumber,
                String.valueOf(proposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getNumber()));
        appendLine(content, PsyNubFieldKey.HospitalCountComment, proposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getComment());
        appendLine(content, PsyNubFieldKey.PEPPs, proposal.getProposalData().getPepps());
        appendLine(content, PsyNubFieldKey.WhyNotRepresented, proposal.getProposalData().getWhyNotRepresented());
        appendLine(content, PsyNubFieldKey.CheckSum, Utils.getChecksum(content.toString() + "Length=" + content.toString().length()));
        return content.toString();
    }

    private static void appendLine(StringBuilder sb, PsyNubFieldKey key, String text) {
        sb.append(key.toString())
                .append("=")
                .append(text.replace("\r", "#{r}").replace("\n", "#{n}"))
                .append("\r\n");
    }

    private static String encodeHelpId(int id) {
        int salt = 100 + (int) (Calendar.getInstance().getTimeInMillis() % 900);
        return "" + salt + (id * salt);
    }

    private static int decodeHelpId(String id) {
        int salt = Integer.parseInt(id.substring(0, 3));
        long temp = Long.parseLong(id.substring(3));
        int helperId = (int) temp / salt;
        if (helperId * salt != temp) {
            // it seems the ID to be altered
            throw new IllegalStateException();
        }
        return helperId;
    }

    public static String createFileName(PsyNubProposal psyNubProposal) {
        return psyNubProposal.getName().replace("\r\n", " ").replace("\r", " ").replace("\n", " ") + ".nub";
    }
}
