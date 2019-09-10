package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubFieldKey;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Optional;

public class PsyNubRequestTemplateHelper implements Serializable {

    public static String createTemplateContentFromPsyNubRequest(PsyNubRequest request, Account account) {
        StringBuilder content = new StringBuilder();

        appendLine(content, PsyNubFieldKey.Version, "" + request.getTargetYear());
        String helperId = encodeHelpId(account.getId());
        appendLine(content, PsyNubFieldKey.ID, helperId);
        appendLine(content, PsyNubFieldKey.System, "PEPP");
        String helperName = account.getTitle() + " " + account.getFirstName() + " " + account.getLastName();
        String helper = account.getCompany()
                + "\r\n" + helperName.trim()
                + "\r\n" + request.getProposalData().getFormFillHelper();
        appendLine(content, PsyNubFieldKey.Helper, helper);
        appendLine(content, PsyNubFieldKey.DisplayName, request.getDisplayName());
        appendLine(content, PsyNubFieldKey.Name, request.getName());
        appendLine(content, PsyNubFieldKey.AltName, request.getAltName());
        appendLine(content, PsyNubFieldKey.Description, request.getProposalData().getDescription());
        appendLine(content, PsyNubFieldKey.HasNoProcs, "" + request.getProposalData().getHasNoProcs());
        appendLine(content, PsyNubFieldKey.ProcCodes, request.getProposalData().getProcs());
        appendLine(content, PsyNubFieldKey.ProcComment, request.getProposalData().getProcsComment());
        appendLine(content, PsyNubFieldKey.Indication, request.getProposalData().getIndication());
        appendLine(content, PsyNubFieldKey.Replacement, request.getProposalData().getReplacement());
        appendLine(content, PsyNubFieldKey.WhatsNew, request.getProposalData().getWhatsNew());
        appendLine(content, PsyNubFieldKey.Los, request.getProposalData().getLos());
        appendLine(content, PsyNubFieldKey.InGermanySinceDate, request.getDateValue(PsyNubDateFields.IN_GERMANY).getDate());
        appendLine(content, PsyNubFieldKey.InGermanySinceComment, request.getDateValue(PsyNubDateFields.IN_GERMANY).getComment());
        appendLine(content, PsyNubFieldKey.MedApprovedDate, request.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getDate());
        appendLine(content, PsyNubFieldKey.MedApprovedComment, request.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).getComment());
        appendLine(content, PsyNubFieldKey.HospitalCountNumber,
                String.valueOf(request.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getNumber()));
        appendLine(content, PsyNubFieldKey.HospitalCountComment, request.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).getComment());
        appendLine(content, PsyNubFieldKey.PEPPs, request.getProposalData().getPepps());
        appendLine(content, PsyNubFieldKey.WhyNotRepresented, request.getProposalData().getWhyNotRepresented());
        appendLine(content, PsyNubFieldKey.CheckSum, Utils.getChecksum(content.toString() + "Length=" + content.toString().length()));
        return content.toString();
    }

    public static Optional<PsyNubRequest> createNewRequestFromTemplate(String template, Account account) {
        PsyNubRequest newPsyNubRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(account);
        if (!checksumIsValid(template)) {
            return Optional.empty();
        }
        String[] lines = template.split("[\\r\\n]+");
        for (String line : lines) {
            int pos = line.indexOf("=");
            String var = line.substring(0, pos);
            PsyNubFieldKey key;
            try {
                key = PsyNubFieldKey.valueOf(var);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Unbekanntes Feld gefunden: " + var);
            }
            String content = line.substring(pos + 1);
            distributeField(newPsyNubRequest, key, content);
        }
        return Optional.of(newPsyNubRequest);
    }

    @SuppressWarnings("checkstyle:JavaNCSS")
    private static void distributeField(PsyNubRequest newPsyNubRequest, PsyNubFieldKey key, String content) {
        switch (key) {
            case Version:
                // might check version here
                break;
            case System:
                if (!"PEPP".equals(content)) {
                    throw new IllegalArgumentException("Es können nur Vorlagen für NUB-PEPP eingelesen werden.");
                }
                break;
            case ID:
                newPsyNubRequest.setHelperId(decodeHelpId(content));
                break;
            case Helper:
                newPsyNubRequest.getProposalData().setFormFillHelper(restoreBreaks(content));
                break;
            case Name:
                newPsyNubRequest.setName(restoreBreaks(content));
                break;
            case DisplayName:
                newPsyNubRequest.setDisplayName(restoreBreaks(content));
                break;
            case AltName:
                newPsyNubRequest.setAltName(restoreBreaks(content));
                break;
            case Description:
                newPsyNubRequest.getProposalData().setDescription(restoreBreaks(content));
                break;
            case HasNoProcs:
                newPsyNubRequest.getProposalData().setHasNoProcs(content.toLowerCase().equals("true"));
                break;
            case ProcCodes:
                newPsyNubRequest.getProposalData().setProcs(restoreBreaks(content));
                break;
            case ProcComment:
                newPsyNubRequest.getProposalData().setProcsComment(restoreBreaks(content));
                break;
            case Indication:
                newPsyNubRequest.getProposalData().setIndication(restoreBreaks(content));
                break;
            case Replacement:
                newPsyNubRequest.getProposalData().setReplacement(restoreBreaks(content));
                break;
            case WhatsNew:
                newPsyNubRequest.getProposalData().setWhatsNew(restoreBreaks(content));
                break;
            case Los:
                newPsyNubRequest.getProposalData().setLos(restoreBreaks(content));
                break;
            case InGermanySinceDate:
                newPsyNubRequest.getDateValue(PsyNubDateFields.IN_GERMANY).setDate(restoreBreaks(content));
                break;
            case InGermanySinceComment:
                newPsyNubRequest.getDateValue(PsyNubDateFields.IN_GERMANY).setComment(restoreBreaks(content));
                break;
            case MedApprovedDate:
                newPsyNubRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setDate(restoreBreaks(content));
                break;
            case MedApprovedComment:
                newPsyNubRequest.getDateValue(PsyNubDateFields.MEDICAL_APPROVAL).setComment(restoreBreaks(content));
                break;
            case HospitalCountNumber:
                newPsyNubRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setNumber(Integer.parseInt(restoreBreaks(content)));
                break;
            case HospitalCountComment:
                newPsyNubRequest.getNumberValue(PsyNubNumberFields.USED_HOSPITALS).setComment(restoreBreaks(content));
                break;
            case PEPPs:
                newPsyNubRequest.getProposalData().setPepps(restoreBreaks(content));
                break;
            case WhyNotRepresented:
                newPsyNubRequest.getProposalData().setWhyNotRepresented(restoreBreaks(content));
                break;
            case CheckSum:
                break;
            default:
                throw new IllegalArgumentException("Unbekanntes Feld gefunden: + " + key.name());
        }
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

    public static String createFileName(PsyNubRequest psyNubRequest) {
        return psyNubRequest.getName().replace("\r\n", " ").replace("\r", " ").replace("\n", " ") + ".nub";
    }
}

