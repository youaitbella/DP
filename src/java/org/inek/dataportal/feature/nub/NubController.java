/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.Calendar;
import java.util.logging.Level;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.NubProposal;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.NubFieldKey;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class NubController extends AbstractFeatureController {

    private static final long serialVersionUID = 1L;

    public NubController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblNUB"), Pages.NubSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartNub.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.NUB;
    }

    /**
     * creates a template file content (without checksum) from nubProposal
     *
     * @param nubProposal
     * @return
     */
    public String createTemplate(NubProposal nubProposal) {
        StringBuilder sb = new StringBuilder();
        appendLine(sb, NubFieldKey.Version, "" + nubProposal.getTargetYear());
        Account account = getSessionController().getAccount();
        String helperId = encodeHelpId(account.getAccountId());
        appendLine(sb, NubFieldKey.ID, helperId);
        String helper = account.getCompany() + "\r\n" + account.getFirstName() + " " + account.getLastName();
        appendLine(sb, NubFieldKey.Helper, helper);
        appendLine(sb, NubFieldKey.Name, nubProposal.getName());
        appendLine(sb, NubFieldKey.AltName, nubProposal.getAltName());
        appendLine(sb, NubFieldKey.Description, nubProposal.getDescription());
        appendLine(sb, NubFieldKey.Procedures, nubProposal.getProcedures());
        appendLine(sb, NubFieldKey.Indication, nubProposal.getIndication());
        appendLine(sb, NubFieldKey.Replacement, nubProposal.getReplacement());
        appendLine(sb, NubFieldKey.WhatsNew, nubProposal.getWhatsNew());
        appendLine(sb, NubFieldKey.Los, nubProposal.getLos());
        appendLine(sb, NubFieldKey.InGermanySince, nubProposal.getInGermanySince());
        appendLine(sb, NubFieldKey.MedApproved, nubProposal.getMedApproved());
        appendLine(sb, NubFieldKey.HospitalCount, "" + nubProposal.getHospitalCount());
        appendLine(sb, NubFieldKey.HigherCosts, nubProposal.getAddCosts());
        appendLine(sb, NubFieldKey.DRGs, nubProposal.getDrgs());
        appendLine(sb, NubFieldKey.WhyNotRepresented, nubProposal.getWhyNotRepresented());
        appendLine(sb, NubFieldKey.RequestedEarlierOther, "" + nubProposal.getRequestedEarlierOther());
        String content = sb.toString(); 
        appendLine(sb, NubFieldKey.CheckSum, Utils.getChecksum(content + "Length=" + content.length()));// add length as invisible "salt" before calculating checksum. 
        return sb.toString();
    }

    private void appendLine(StringBuilder sb, NubFieldKey key, String text) {
        sb.append(key.toString())
                .append("=")
                .append(text.replace("\r", "#{r}").replace("\n", "#{n}"))
                .append("\r\n");
    }

    private String encodeHelpId(int id) {
        int salt = 100 + (int) (Calendar.getInstance().getTimeInMillis() % 900);
        return "" + salt + (id * salt);
    }

    private int decodeHelpId(String id) {
        int salt = Integer.parseInt(id.substring(0, 3));
        long temp = Long.parseLong(id.substring(3));
        int helperId = (int) temp / salt;
        if (helperId * salt != temp) {
            // it seems the ID to be altered
            throw new IllegalStateException();
        }
        return helperId;
    }

    /**
     * creates a new NubProposal and appends information from template
     *
     * @param template
     * @return
     */
    public NubProposal createNubProposal(String template) {
        NubProposal proposal = createNubProposal();
        String[] lines = template.split("[\\r\\n]+");
        for (String line : lines) {
            int pos = line.indexOf("="); // do not use split, because content may contain an equal sign. Just search for the first one
            String var = line.substring(0, pos);
            NubFieldKey key = NubFieldKey.valueOf(var);
            String content = line.substring(pos + 1);
            switch (key) {
                case ID:
                    proposal.setHelperId(decodeHelpId(content));
                    break;
                case Helper:
                    proposal.setFormFillHelper(restoreBreaks(content));
                    break;
                case Name:
                    proposal.setName(restoreBreaks(content));
                    break;
                case AltName:
                    proposal.setAltName(restoreBreaks(content));
                    break;
                case Description:
                    proposal.setDescription(restoreBreaks(content));
                    break;
                case Procedures:
                    proposal.setProcedures(restoreBreaks(content));
                    break;
                case Indication:
                    proposal.setIndication(restoreBreaks(content));
                    break;
                case Replacement:
                    proposal.setReplacement(restoreBreaks(content));
                    break;
                case WhatsNew:
                    proposal.setWhatsNew(restoreBreaks(content));
                    break;
                case Los:
                    proposal.setLos(restoreBreaks(content));
                    break;
                case InGermanySince:
                    proposal.setInGermanySince(restoreBreaks(content));
                    break;
                case MedApproved:
                    proposal.setMedApproved(restoreBreaks(content));
                    break;
                case HospitalCount:
                    proposal.setHospitalCount(restoreBreaks(content));
                    break;
                case HigherCosts:
                    proposal.setAddCosts(restoreBreaks(content));
                    break;
                case DRGs:
                    proposal.setDrgs(restoreBreaks(content));
                    break;
                case WhyNotRepresented:
                    proposal.setWhyNotRepresented(restoreBreaks(content));
                    break;
                case RequestedEarlierOther:
                    proposal.setRequestedEarlierOther(Boolean.parseBoolean(content));
                    break;
            }
        }
        return proposal;
    }

    private String restoreBreaks(String text) {
        return text.replace("#{r}", "\r").replace("#{n}", "\n");
    }

    public NubProposal createNubProposalFromOldFormat(String template) {
        NubProposal proposal = createNubProposal();
        String[] lines = template.replace("\r", "").split("[\\n]"); // split at CRLF or LF only
        int i = 0;
        while (i < lines.length) {
            try {
                int pos = lines[i].indexOf("="); // do not use split, because content may contain an equal sign. Just search for the first one
                if (pos < 1) {
                    i++;
                    continue;
                }
                String var = lines[i].substring(0, pos);
                String content = lines[i].substring(pos + 1);
                int count;
                try {
                    count = Integer.parseInt(content);
                } catch (NumberFormatException ex) {
                    count = 0;
                }
                switch (var) {
                    case "memStammAnnahmeHilfe":
                        proposal.setFormFillHelper(readLines(lines, i, count));
                        break;
                    case "memoNub1Behandlungsmethode":
                        proposal.setName(readLines(lines, i, count));
                        break;
                    case "memoNub1AlternativeBezeichnung":
                        proposal.setAltName(readLines(lines, i, count));
                        break;
                    case "memoNub1Beschreibung":
                        proposal.setDescription(readLines(lines, i, count));
                        break;
                    case "memoNub1OPS":
                        proposal.setProcedures(readLines(lines, i, count));
                        break;
                    case "memoNub2Indikation":
                        proposal.setIndication(readLines(lines, i, count));
                        break;
                    case "memoNub2MethodeErsetzt":
                        proposal.setReplacement(readLines(lines, i, count));
                        break;
                    case "memoNub2WarumNeu":
                        proposal.setWhatsNew(readLines(lines, i, count));
                        break;
                    case "memoNub2AuswirkungAufVWD":
                        proposal.setLos(readLines(lines, i, count));
                        break;
                    case "memoNub3EinfuehrungInDeutschland":
                        proposal.setInGermanySince(readLines(lines, i, count));
                        break;
                    case "memoNub3ZulassungMedikament":
                        proposal.setMedApproved(readLines(lines, i, count));
                        break;
                    case "memoNub3EingesetztIn":
                        proposal.setHospitalCount(readLines(lines, i, count));
                        break;
                    case "memoNub5Mehrkosten":
                        proposal.setAddCosts(readLines(lines, i, count));
                        break;
                    case "memoNub5BetroffeneDRG":
                        proposal.setDrgs(readLines(lines, i, count));
                        break;
                    case "memoNub5WarumNichtSachgerecht":
                        proposal.setWhyNotRepresented(readLines(lines, i, count));
                        break;
                    case "cbNub5AnfrageVonAnderenGestellt":
                        proposal.setRequestedEarlierOther(content.equalsIgnoreCase("ja"));
                        count = 0;
                        break;
                    default:
                        count = 0;
                        break;
                }
                i += count + 1;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return proposal;
    }

    private String readLines(String[] lines, int row, int count) {
        String content = "";
        for (int i = 1; i <= count; i++) {
            if (row + i < lines.length) {
                content += (i > 1 ? "\r\n" : "") + lines[row + i];
            } else {
                _logger.log(Level.WARNING, "Corrupted template. Continued.");
            }
        }
        return content;
    }

    /**
     * creates a new NubProposal pre-populated with master data
     *
     * @return
     */
    public NubProposal createNubProposal() {
        Account account = getSessionController().getAccount();
        NubProposal proposal = new NubProposal();
        proposal.setAccountId(account.getAccountId());
        proposal.setIk(account.getIK());
        proposal.setIkName(account.getCompany());
        proposal.setGender(account.getGender());
        proposal.setTitle(account.getTitle());
        proposal.setFirstName(account.getFirstName());
        proposal.setLastName(account.getLastName());
        proposal.setRoleId(account.getRoleId());
        proposal.setStreet(account.getStreet());
        String postalCode=account.getPostalCode().replaceAll("[\\D]", "");
        if (postalCode.length() > 5){postalCode="";}
        proposal.setPostalCode(postalCode);
        proposal.setTown(account.getTown());
        String phone = account.getPhone();
        if (Utils.isNullOrEmpty(phone)) {
            phone = account.getCustomerPhone();
        }
        proposal.setPhone(phone);
        proposal.setFax(account.getCustomerFax());
        proposal.setEmail(account.getEmail());
        return proposal;
    }

}
