/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.Calendar;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.entities.account.Account;
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

    public NubController(SessionController sessionController) {
        super(sessionController);
    }

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblNUB"), Pages.NubSummary.URL());
        if (getSessionController().isHospital()) {
            topics.addTopic(Utils.getMessage("lblNubMethodInfo"), Pages.NubMethodInfo.URL());
        }
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
     * creates a template file content (without checksum) from nubRequest
     *
     * @param nubRequest
     * @return
     */
    public String createTemplate(NubRequest nubRequest) {
        StringBuilder sb = new StringBuilder();
        appendLine(sb, NubFieldKey.Version, "" + nubRequest.getTargetYear());
        Account account = getSessionController().getAccount();
        String helperId = encodeHelpId(account.getId());
        appendLine(sb, NubFieldKey.ID, helperId);
        String helper = account.getCompany() + "\r\n" + account.getFirstName() + " " + account.getLastName();
        appendLine(sb, NubFieldKey.Helper, helper);
        appendLine(sb, NubFieldKey.DisplayName, nubRequest.getDisplayName());
        appendLine(sb, NubFieldKey.Name, nubRequest.getName());
        appendLine(sb, NubFieldKey.AltName, nubRequest.getAltName());
        appendLine(sb, NubFieldKey.Description, nubRequest.getDescription());
        appendLine(sb, NubFieldKey.HasNoProcs, "" + nubRequest.isHasNoProcs());
        appendLine(sb, NubFieldKey.ProcCodes, nubRequest.getProcs());
        appendLine(sb, NubFieldKey.Procedures, nubRequest.getProcedures());
        appendLine(sb, NubFieldKey.MedicalDevice, "" + nubRequest.getMedicalDevice());
        appendLine(sb, NubFieldKey.RiscClass, "" + nubRequest.getRiscClass());
        appendLine(sb, NubFieldKey.RiscClassComment, nubRequest.getRiscClassComment());
        appendLine(sb, NubFieldKey.TradeName, nubRequest.getTradeName());
        appendLine(sb, NubFieldKey.CeMark, nubRequest.getCeMark());
        appendLine(sb, NubFieldKey.Indication, nubRequest.getIndication());
        appendLine(sb, NubFieldKey.Replacement, nubRequest.getReplacement());
        appendLine(sb, NubFieldKey.WhatsNew, nubRequest.getWhatsNew());
        appendLine(sb, NubFieldKey.Los, nubRequest.getLos());
        appendLine(sb, NubFieldKey.InGermanySince, nubRequest.getInGermanySince());
        appendLine(sb, NubFieldKey.MedApproved, nubRequest.getMedApproved());
        appendLine(sb, NubFieldKey.HospitalCount, "" + nubRequest.getHospitalCount());
        appendLine(sb, NubFieldKey.HigherCosts, nubRequest.getAddCosts());
        appendLine(sb, NubFieldKey.DRGs, nubRequest.getDrgs());
        appendLine(sb, NubFieldKey.WhyNotRepresented, nubRequest.getWhyNotRepresented());
        appendLine(sb, NubFieldKey.RequestedEarlierOther, "" + nubRequest.getRequestedEarlierOther());
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
     * creates a new NubRequest and appends information from template
     *
     * @param template
     * @return
     */
    public NubRequest createNubRequest(String template) {
        NubRequest request = NubController.this.createNubRequest();
        String[] lines = template.split("[\\r\\n]+");
        for (String line : lines) {
            int pos = line.indexOf("="); // do not use split, because content may contain an equal sign. Just search for the first one
            String var = line.substring(0, pos);
            NubFieldKey key = NubFieldKey.valueOf(var);
            String content = line.substring(pos + 1);
            switch (key) {
                case ID:
                    request.setHelperId(decodeHelpId(content));
                    break;
                case Helper:
                    request.setFormFillHelper(restoreBreaks(content));
                    break;
                case Name:
                    request.setName(restoreBreaks(content));
                    break;
                case DisplayName:
                    request.setDisplayName(restoreBreaks(content));
                    break;
                case AltName:
                    request.setAltName(restoreBreaks(content));
                    break;
                case Description:
                    request.setDescription(restoreBreaks(content));
                    break;
                case HasNoProcs:
                    request.setHasNoProcs(content.toLowerCase().equals("true"));
                    break;
                case ProcCodes:
                    request.setProcs(restoreBreaks(content));
                    break;
                case Procedures:
                    request.setProcedures(restoreBreaks(content));
                    break;
                case MedicalDevice:
                    request.setMedicalDevice(getByteValue(content));
                    break;
                case RiscClass:
                    request.setRiscClass(getByteValue(content));
                    break;
                case RiscClassComment:
                    request.setRiscClassComment(restoreBreaks(content));
                    break;
                case TradeName:
                    request.setTradeName(restoreBreaks(content));
                    break;
                case CeMark:
                    request.setCeMark(restoreBreaks(content));
                    break;
                case Indication:
                    request.setIndication(restoreBreaks(content));
                    break;
                case Replacement:
                    request.setReplacement(restoreBreaks(content));
                    break;
                case WhatsNew:
                    request.setWhatsNew(restoreBreaks(content));
                    break;
                case Los:
                    request.setLos(restoreBreaks(content));
                    break;
                case InGermanySince:
                    request.setInGermanySince(restoreBreaks(content));
                    break;
                case MedApproved:
                    request.setMedApproved(restoreBreaks(content));
                    break;
                case HospitalCount:
                    request.setHospitalCount(restoreBreaks(content));
                    break;
                case HigherCosts:
                    request.setAddCosts(restoreBreaks(content));
                    break;
                case DRGs:
                    request.setDrgs(restoreBreaks(content));
                    break;
                case WhyNotRepresented:
                    request.setWhyNotRepresented(restoreBreaks(content));
                    break;
                case RequestedEarlierOther:
                    request.setRequestedEarlierOther(Boolean.parseBoolean(content));
                    break;
            }
        }
        if (request.getHelperId() == request.getAccountId()) {
            // no entry for own template
            request.setFormFillHelper("");
        }
        request.setCreatedBy(getSessionController().getAccountId());
        request.setLastChangedBy(getSessionController().getAccountId());
        return request;
    }

    private String restoreBreaks(String text) {
        return text.replace("#{r}", "\r").replace("#{n}", "\n");
    }

    /**
     * creates a new NubRequest pre-populated with master data
     *
     * @return
     */
    public NubRequest createNubRequest() {
        Account account = getSessionController().getAccount();
        NubRequest proposal = new NubRequest();
        proposal.setAccountId(account.getId());
        populateMasterData(proposal, account);
        return proposal;
    }

    public void populateMasterData(NubRequest proposal, Account account) {
        Integer ik = account.getIK();
        if (ik == null && !account.getAdditionalIKs().isEmpty()) {
            ik = account.getAdditionalIKs().get(0).getIK();
        }
        proposal.setIk(ik);
        proposal.setIkName(account.getCompany());
        proposal.setGender(account.getGender());
        proposal.setTitle(account.getTitle());
        proposal.setFirstName(account.getFirstName());
        proposal.setLastName(account.getLastName());
        proposal.setRoleId(account.getRoleId());
        proposal.setStreet(account.getStreet());
        String postalCode = account.getPostalCode().replaceAll("[\\D]", "");
        if (postalCode.length() > 5) {
            postalCode = "";
        }
        proposal.setPostalCode(postalCode);
        proposal.setTown(account.getTown());
        String phone = account.getPhone();
        if (Utils.isNullOrEmpty(phone)) {
            phone = account.getCustomerPhone();
        }
        proposal.setPhone(phone);
        proposal.setFax(account.getCustomerFax());
        proposal.setEmail(account.getEmail());
    }

    private byte getByteValue(String content) {
        try {
            return Byte.parseByte(content);
        } catch (Exception ex) {
            return 0;
        }
    }

}
