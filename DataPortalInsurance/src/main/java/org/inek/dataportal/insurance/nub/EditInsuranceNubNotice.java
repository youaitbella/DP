package org.inek.dataportal.insurance.nub;

import org.inek.dataportal.insurance.nub.entities.InsuranceNubNoticeItem;
import org.inek.dataportal.insurance.nub.entities.InsuranceNubMethodInfo;
import org.inek.dataportal.insurance.nub.entities.InsuranceNubNotice;
import org.inek.dataportal.insurance.nub.entities.DosageForm;
import org.inek.dataportal.insurance.nub.entities.Unit;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ProcedureFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.common.RemunerationType;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.insurance.facade.InsuranceFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@FeatureScoped
public class EditInsuranceNubNotice extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditInsuranceNubNotice");
    @Inject
    private InsuranceFacade _insuranceFacade;
    @Inject
    private ProcedureFacade _procedureFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private CustomerFacade _customerFacade;
    @Inject
    private AccessManager _accessManager;
    private Set<Integer> _iks = new HashSet<>();
    private InsuranceNubNotice _notice;
    private List<InsuranceNubMethodInfo> _nubInfos = null;
    private Part _file;
    private String _importMessage = "";
    @Inject
    private Instance<NoticeItemImporter> _importProvider;
    private boolean _showJournal = false;

    // <editor-fold defaultstate="collapsed" desc="override AbstractEditController">
    @Override
    protected void addTopics() {
    }

    @PostConstruct
    private void init() {
        String id = "" + FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if ("new".equals(id)) {
            _notice = newNotice();
        } else {
            _notice = loadNubNotice(id);
            if (_notice.getId() <= 0) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
            }
        }
    }

    private InsuranceNubNotice loadNubNotice(String id) {
        try {
            int idInt = Integer.parseInt(id);
            InsuranceNubNotice notice = _insuranceFacade.findFreshNubNotice(idInt);
            if (notice != null) {
                return notice;
            }
        } catch (NumberFormatException ex) {
            // ignore
        }
        return new InsuranceNubNotice();
    }

    private InsuranceNubNotice newNotice() {
        Account account = _sessionController.getAccount();
        InsuranceNubNotice notice = new InsuranceNubNotice();
        notice.setAccountId(account.getId());
        notice.setWorkflowStatusId(0);
        return notice;
    }

    public void ikChanged() {
        _nubInfos = null;
        _notice.setInsuranceName(_customerFacade.getCustomerByIK(_notice.getInsuranceIk()).getName());
    }

    public Set<Integer> getValidIks() {
        if (_iks.isEmpty()) {
            _iks = _accessManager.ObtainIksForCreation(Feature.NUB_NOTICE)
                    .stream()
                    .filter(i -> i >= 100000000 && i < 199999999)  // only insurance ik
                    .collect(Collectors.toSet());
        }
        if (_iks.size() == 1) {
            _notice.setInsuranceIk(_iks.iterator().next());
        }
        return _iks;
    }

    public InsuranceNubNotice getNotice() {
        return _notice;
    }

    public List<SelectItem> getInekMethods() {
        if (_nubInfos == null) {
            getNubMethodInfos();
        }
        return _nubInfos
                .stream()
                .sorted((n, m) -> n.getMethodName().compareTo(m.getMethodName()))
                .map(i -> new SelectItem(i.getRequestId(), i.getMethodName() + " [N" + i.getRequestId() + "]", i.
                        getRequestName()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getInekMethodsBySeq() {
        if (_nubInfos == null) {
            getNubMethodInfos();
        }
        return _nubInfos
                .stream()
                .sorted((n, m) -> Integer.compare(n.getSequence(), m.getSequence()))
                .map(i -> new SelectItem(i.getRequestId(),
                        i.getSequence() + " - " + i.getMethodName() + " [N" + i.getRequestId() + "]", i.getRequestName()))
                .collect(Collectors.toList());
    }

    public List<InsuranceNubMethodInfo> getNubMethodInfos() {
        if (_notice.getHospitalIk() < 0 && _notice.getYear() < 0) {
            return Collections.emptyList();
        }
        if (_nubInfos == null) {
            _nubInfos = _insuranceFacade.getNubMethodInfos(_notice.getHospitalIk(), _notice.getYear());
        }
        return _nubInfos;
    }

    public List<DosageForm> getDosageForms() {
        return _insuranceFacade.getDosageForms();
    }

    public List<Unit> getUnits() {
        return _insuranceFacade.getUnits();
    }

    public boolean getProvideEnabled() {
        return _notice.getInsuranceIk() > 0 && _notice.getHospitalIk() > 0;
    }

    public int getMaxYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public boolean getReadOnly() {
        if (_notice.getWorkflowStatusId() >= WorkflowStatus.Provided.getId()) {
            return true;
        }
        return false;
    }

    public void validateProcedures(FacesContext context, UIComponent component, Object value) {
        int targetYear = _notice.getYear();
        String invalidCodes = _procedureFacade.checkProcedures(value.toString(), targetYear, targetYear, "\\s|;|,|\\+");
        if (invalidCodes.length() > 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, invalidCodes, invalidCodes);
            throw new ValidatorException(msg);
        }
    }

    public void validateRemuneration(FacesContext context, UIComponent component, Object value) {
        if ("".equals(value)) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Kein Entgeltschlüssel angegeben",
//                    "Kein Entgeltschlüssel angegeben");
//            throw new ValidatorException(msg);
            return;
        }
        if (value.toString().length() != 8) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Entgeltschlüssel müssen 8 Zeichen lang sein.",
                    "Entgeltschlüssel müssen 8 Zeichen lang sein.");
            throw new ValidatorException(msg);
        }
        Optional<RemunerationType> remunTypeOpt = _insuranceFacade.getRemunerationType(value.toString());
        if (!remunTypeOpt.isPresent()) {
            FacesContext.getCurrentInstance().addMessage(component.getClientId(),
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Entgeltschlüssel ist dem InEK unbekannt. Ggf. ist er fehlerhaft.",
                            "Entgeltschlüssel ist dem InEK unbekannt. Ggf. ist er fehlerhaft."
                    )
            );
        }
    }

    public void validatePrice(FacesContext context, UIComponent component, Object value) {
        double tmp = 0.0;
        try {
            tmp = Double.parseDouble(value + "");
        } catch (NumberFormatException ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte einen gültigen Geldbetrag eingeben.",
                    "Bitte einen gültigen Geldbetrag eingeben.");
            throw new ValidatorException(msg);
        }
        if (tmp == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Betrag darf nicht 0 sein.",
                    "Der Betrag darf nicht 0 sein.");
            throw new ValidatorException(msg);
        } else if (tmp < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Betrag darf nicht kleiner als 0 sein.",
                    "Der Betrag darf nicht kleiner als 0 sein.");
            throw new ValidatorException(msg);
        }
    }

    public void validateQuantity(FacesContext context, UIComponent component, Object value) {
        int x;
        try {
            x = Integer.parseInt(value + "");
        } catch (NumberFormatException ex) {
            x = -1;
        }
        if (x <= 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Mindestwert beträgt 1", "Der Mindestwert beträgt 1");
            throw new ValidatorException(msg);
        }
    }

    public void validateAmount(FacesContext context, UIComponent component, Object value) {
        double x;
        try {
            x = Double.parseDouble(value + "");
        } catch (NumberFormatException ex) {
            x = -1;
        }
        if (x < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Mindestwert beträgt 0", "Der Mindestwert beträgt 0");
            throw new ValidatorException(msg);
        }
    }

    public void addItem() {
        InsuranceNubNoticeItem item = new InsuranceNubNoticeItem();
        _notice.getItems().add(item);
    }

    public void deleteItem(InsuranceNubNoticeItem item) {
        _notice.getItems().remove(item);
    }

    public String save() {
        try {
            _notice = _insuranceFacade.saveNubNotice(_notice);
            _sessionController.alertClient(Utils.getMessage("msgSaveAndMentionSend"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during save NubNotice: {0}", e.getMessage());
            _sessionController.alertClient(Utils.getMessage("msgSaveError"));
        }
        return "";
    }

    /**
     * provide the message to InEK
     *
     * @return
     */
    public String provide() {
        String validatorMessage = "";
        if (_notice.getItems().isEmpty()) {
            validatorMessage = "Es gibt keine Meldungen, die ans InEK gesendet werden könnten.";
        }
        int lines = 0;
        for (InsuranceNubNoticeItem item : _notice.getItems()) {
            lines++;
            if ("N0".equals(item.getExternalId())) {
                validatorMessage += "Zeile " + lines + ": Name ist ein Pflichtfeld.\n";
            }
            if (item.getQuantity() <= 0) {
                validatorMessage += "Zeile " + lines + ": Anzahl ist ein Pflichtfeld.\n";
            }
            if (item.getPrice() == null || item.getPrice().floatValue() <= 0) {
                validatorMessage += "Zeile " + lines + ": Entgelthöhe ist ein Pflichtfeld.\n";
            }
            if (item.getRemunerationTypeCharId().length() != 0 && item.getRemunerationTypeCharId().length() != 8) {
                validatorMessage += "Zeile " + lines + ": Entgeltschlüssel muss 8 Zeichen lang sein.\n";
            }
        }
        if (!"".equals(validatorMessage)) {
            _sessionController.alertClient(validatorMessage);
            return "";
        }
        _notice.setStatus(WorkflowStatus.Provided);
        try {
            _notice = _insuranceFacade.saveNubNotice(_notice);
            _sessionController.alertClient("NUB-Meldung wurde erfolgreich eingereicht.");
            return Pages.InsuranceNubNoticeSummary.RedirectURL();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during save NubNotice: {0}", e.getMessage());
            _sessionController.alertClient(Utils.getMessage("msgSaveError"));
            return "";
        }
    }

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public String getImportMessage() {
        return _importMessage;
    }

    public void uploadNotices() {
        try {
            if (_file != null) {
                //Scanner scanner = new Scanner(_file.getInputStream(), "UTF-8");
                // We assume most of the documents coded with the Windows character set
                // Thus, we read with the system default
                // in case of an UTF-8 file, all German Umlauts will be corrupted.
                // We simply replace them.
                // Drawbacks: this only converts the German Umlauts, no other chars.
                // By intention it fails for other charcters
                // Alternative: implement a library which guesses th correct character set and read properly
                // Since we support German only, we started using the simple approach
                Scanner scanner = new Scanner(_file.getInputStream());
                if (!scanner.hasNextLine()) {
                    return;
                }
                NoticeItemImporter itemImporter = _importProvider.get();
                itemImporter.setNotice(_notice);
                while (scanner.hasNextLine()) {
                    String line = Utils.convertFromUtf8(scanner.nextLine());
                    if (!line.contains(";Form;Menge;Einheit;Anzahl;Preis;Entgelt")) {
                        itemImporter.tryImportLine(line);
                    }
                }
                _importMessage = itemImporter.getMessage();
                _sessionController.alertClient(_importMessage);
                _showJournal = false;
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

    public String toggleJournal() {
        _showJournal = !_showJournal;
        return "";
    }

    public boolean isShowJournal() {
        return _showJournal;
    }

    public void setShowJournal(boolean showJournal) {
        this._showJournal = showJournal;
    }
}
