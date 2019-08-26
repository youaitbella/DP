package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.psy.nub.entities.PsyNubProposal;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubFieldKey;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Optional;

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

    @SuppressWarnings("checkstyle:JavaNCSS")
    public static Optional<PsyNubProposal> createNewProposalFromTemplate(String template, Account account) {
        PsyNubProposal newPsyNubProposal = NewPsyNubProposalHelper.createNewPsyNubProposal(account);
        if (checksumIsValid(template)) {
            String[] lines = template.split("[\\r\\n]+");
            for (String line : lines) {
                int pos = line.indexOf("=");
                String var = line.substring(0, pos);
                PsyNubFieldKey key = PsyNubFieldKey.valueOf(var);
                String content = line.substring(pos + 1);
                switch (key) {
                    case Version:
                        // might check version here
                        break;
                    case ID:
                        newPsyNubProposal.setHelperId(decodeHelpId(content));
                        break;
                    case Helper:
                        newPsyNubProposal.getProposalData().setFormFillHelper(restoreBreaks(content));
                        break;
                    case Name:
                        newPsyNubProposal.setName(restoreBreaks(content));
                        break;
                    case DisplayName:
                        newPsyNubProposal.setDisplayName(restoreBreaks(content));
                        break;
                    case AltName:
                        newPsyNubProposal.setAltName(restoreBreaks(content));
                        break;
                    case Description:
                        newPsyNubProposal.getProposalData().setDescription(restoreBreaks(content));
                        break;
                    case HasNoProcs:
                        newPsyNubProposal.getProposalData().setHasNoProcs(content.toLowerCase().equals("true"));
                        break;
                    case ProcCodes:
                        newPsyNubProposal.getProposalData().setProcs(restoreBreaks(content));
                        break;
                    case Procedures:
                        newPsyNubProposal.getProposalData().setProcs(restoreBreaks(content));
                        break;
                    case Indication:
                        newPsyNubProposal.getProposalData().setIndication(restoreBreaks(content));
                        break;
                    case Replacement:
                        newPsyNubProposal.getProposalData().setReplacement(restoreBreaks(content));
                        break;
                    case WhatsNew:
                        newPsyNubProposal.getProposalData().setWhatsNew(restoreBreaks(content));
                        break;
                    case Los:
                        newPsyNubProposal.getProposalData().setLos(restoreBreaks(content));
                        break;
                    case InGermanySinceDate:
                        newPsyNubProposal.getDateValue(PsyNubDateFields.IN_GERMANY).setDate(restoreBreaks(content));
                        break;
                    case InGermanySinceComment:
                        newPsyNubProposal.getDateValue(PsyNubDateFields.IN_GERMANY).setComment(restoreBreaks(content));
                        break;
                    case MedApprovedDate:
                        newPsyNubProposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setDate(restoreBreaks(content));
                        break;
                    case MedApprovedComment:
                        newPsyNubProposal.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setComment(restoreBreaks(content));
                        break;
                    case HospitalCountNumber:
                        newPsyNubProposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setNumber(Integer.parseInt(restoreBreaks(content)));
                        break;
                    case HospitalCountComment:
                        newPsyNubProposal.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setComment(restoreBreaks(content));
                        break;
                    case PEPPs:
                        newPsyNubProposal.getProposalData().setPepps(restoreBreaks(content));
                        break;
                    case WhyNotRepresented:
                        newPsyNubProposal.getProposalData().setWhyNotRepresented(restoreBreaks(content));
                        break;
                    case CheckSum:
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown Key [PSY-NUB]: + " + key);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid checksum [PSY-NUB]");
        }
        return Optional.of(newPsyNubProposal);
    }

    private static boolean checksumIsValid(String template) {
        String bom = "" + (char) 239 + (char) 187 + (char) 191;
        if (template.startsWith(bom)) {
            template = template.substring(3);
        }
        int pos = template.lastIndexOf(PsyNubFieldKey.CheckSum + "=");
        if (pos < 0) {
            return false;
        }
        String templateWithoutChecksum = template.substring(0, pos);
        String checksum = template.substring(pos + 9).replace("\r\n", "");
        if (checksum.equals(Utils.getChecksum(templateWithoutChecksum + "Length=" + templateWithoutChecksum.length()))) {
            return true;
        }
        return false;
    }

    private static String restoreBreaks(String text) {
        return text.replace("#{r}", "\r").replace("#{n}", "\n");
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

