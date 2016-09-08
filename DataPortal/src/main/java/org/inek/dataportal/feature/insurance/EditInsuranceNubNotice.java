/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.insurance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.entities.insurance.DosageForm;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.entities.insurance.InsuranceNubNoticeItem;
import org.inek.dataportal.entities.insurance.Unit;
import org.inek.dataportal.entities.insurance.NubMethodInfo;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.InsuranceFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditInsuranceNubNotice extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditInsuranceNubNotice");

    // <editor-fold defaultstate="collapsed" desc="override AbstractEditController">    
    @Override
    protected void addTopics() {
        addTopic(NubRequestTabs.tabMessageAdress.name(), Pages.InsuranceNubNoticeEditAddress.URL());
        addTopic(NubRequestTabs.tabMessageList.name(), Pages.InsuranceNubNoticeEditList.URL());
    }

    enum NubRequestTabs {
        tabMessageAdress,
        tabMessageList;
    }
    // </editor-fold>

    @Inject private InsuranceFacade _insuranceFacade;
    @Inject private ProcedureFacade _procedureFacade;
    @Inject private SessionController _sessionController;

    @PostConstruct
    private void init() {
        Object noticeId = Utils.getFlash().get("noticeId");
        int id = noticeId == null ? 0 : (int) noticeId;
        _notice = findFresh(id);
    }

    private InsuranceNubNotice findFresh(int id) {
        InsuranceNubNotice notice = _insuranceFacade.findFreshNubNotice(id);
        if (notice == null) {
            Account account = _sessionController.getAccount();
            notice = new InsuranceNubNotice();
            notice.setInsuranceName(account.getCompany());
            notice.setInsuranceIk(account.getIK());
            notice.setAccountId(account.getId());
            notice.setWorkflowStatusId(0);
        }
        return notice;
    }

    public void ikChanged() {
        _nubInfos = null;
    }

    private InsuranceNubNotice _notice;

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
                .map(i -> new SelectItem(i.getRequestId(), i.getMethodName() + " [N" + i.getRequestId() + "]", i.getRequestName()))
                .collect(Collectors.toList());
    }

    private List<NubMethodInfo> _nubInfos = null;

    public List<NubMethodInfo> getNubMethodInfos() {
        if (_notice.getHospitalIk() < 0 && _notice.getYear() < 0) {
            return Collections.EMPTY_LIST;
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
        return true;
    }

    public boolean getReadOnly() {
        if (_notice.getWorkflowStatusId() >= WorkflowStatus.Provided.getValue()) {
            return true;
        }
        return false;
    }

    public void validateProcedures(FacesContext context, UIComponent component, Object value) {
        int targetYear = _notice.getYear();
        String invalidCodes = _procedureFacade.checkProcedures(value.toString(), targetYear, targetYear, "\\s|,|\\+");
        if (invalidCodes.length() > 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, invalidCodes, invalidCodes);
            throw new ValidatorException(msg);
        }
    }

    public void validateRemuneration(FacesContext context, UIComponent component, Object value) {
        String labelRemunId = "msgRemun";
        HtmlMessage remunLabel = (HtmlMessage) Utils.findComponent(labelRemunId);
        if (value.equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Kein Entgeltschlüssel angegeben",
                    "Kein Entgeltschlüssel angegeben");
            throw new ValidatorException(msg);
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
            tmp = Double.parseDouble(value+"");
        } catch(Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte einen gültigen Geldbetrag eingeben.", "Bitte einen gültigen Geldbetrag eingeben.");
            throw new ValidatorException(msg);
        }
        if(tmp == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Betrag darf nicht 0 sein.", "Der Betrag darf nicht 0 sein.");
            throw new ValidatorException(msg);
        } else if(tmp < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Betrag darf nicht kleiner als 0 sein.", "Der Betrag darf nicht kleiner als 0 sein.");
            throw new ValidatorException(msg);
        }
    }
    
    public void validateQuantity(FacesContext context, UIComponent component, Object value) {
        int x = Integer.parseInt(value+"");
        if(x <= 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Mindestwert beträgt 1", "Der Mindestwert beträgt 1");
            throw new ValidatorException(msg);
        }
    }
    
    public void validateAmount(FacesContext context, UIComponent component, Object value) {
        int x = Integer.parseInt(value+"");
        if(x < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Mindestwert beträgt 1", "Der Mindestwert beträgt 1");
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
//        try {
            _notice = _insuranceFacade.saveNubNotice(_notice);
            _sessionController.alertClient(Utils.getMessage("msgSave"));
//        } catch (EJBException e) {
//            _sessionController.alertClient(Utils.getMessage("msgSaveError"));
//        }
        return "";
    }

    /**
     * provide the message to InEK
     *
     * @return
     */
    public String provide() {
        String validatorMessage = "";
        int lines = 1;
        if (_notice.getItems().isEmpty()) {
            validatorMessage = "Es gibt keine Meldungen, die ans InEK gesendet werden könnten.";
        }
        for (InsuranceNubNoticeItem item : _notice.getItems()) {
            if (item.getExternalId() == null || item.getExternalId().equals("N0")) {
                validatorMessage += "Zeile " + lines + ": Name ist ein Pflichtfeld.\n";
            }
            if (item.getAmount() == null || item.getAmount().intValue() <= 0) {
                validatorMessage += "Zeile " + lines + ": Anzahl ist ein Pflichtfeld.\n";
            }
            if (item.getPrice() == null || item.getPrice().intValue() <= 0) {
                validatorMessage += "Zeile " + lines + ": Preis ist ein Pflichtfeld.\n";
            }
            if (item.getRemunerationTypeCharId().length() != 8) {
                validatorMessage += "Zeile " + lines + ": Entgeltschlüssel muss 8 Zeichen lang sein.\n";
            }
            lines++;
        }
        if (!validatorMessage.equals("")) {
            _sessionController.alertClient(validatorMessage);
            return "";
        }
        _notice.setWorkflowStatus(WorkflowStatus.Provided);
        _insuranceFacade.merge(_notice);
        _insuranceFacade.clearCache();
        _sessionController.alertClient("Krankenkassenmeldung wurde erfolgreich eingereicht.");
        return Pages.InsuranceSummary.RedirectURL();
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    @Inject Instance<NoticeItemImporter> _importProvider;

    public void uploadNotices() {
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(), "UTF-8");
                if (!scanner.hasNextLine()) {
                    return;
                }
                NoticeItemImporter itemImporter = _importProvider.get();
                itemImporter.setNotice(_notice);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    itemImporter.tryImportLine(line);
                }
                _sessionController.alertClient(itemImporter.getMessage());
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

}
