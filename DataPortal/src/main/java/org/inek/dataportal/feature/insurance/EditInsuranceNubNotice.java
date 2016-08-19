/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.insurance;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.print.attribute.standard.Severity;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.entities.insurance.DosageForm;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.entities.insurance.InsuranceNubNoticeItem;
import org.inek.dataportal.entities.insurance.Unit;
import org.inek.dataportal.entities.insurance.NubMethodInfo;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
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
@FeatureScoped(name = "Insurance")
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

    private InsuranceNubNotice _notice;

    public InsuranceNubNotice getNotice() {
        return _notice;
    }

    public List<SelectItem> getInekMethods() {
        return getNubMethodInfos()
                .stream()
                .sorted((n, m) -> n.getMethodName().compareTo(m.getMethodName()))
                .map(i -> new SelectItem(i.getRequestId(), i.getMethodName() + " [" + i.getRequestId() + "]", i.getRequestName()))
                .collect(Collectors.toList());
    }

    public List<NubMethodInfo> getNubMethodInfos() {
        return _insuranceFacade.getNubMethodInfos(_notice.getHospitalIk(), _notice.getYear());
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
        return false;
    }

    public void validateProcedures(FacesContext context, UIComponent component, Object value){
        int targetYear = _notice.getYear();
        String invalidCodes = _procedureFacade.checkProcedures(value.toString(), targetYear, targetYear, "\\s|,|\\+");
        if (invalidCodes.length() > 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, invalidCodes, invalidCodes);
            throw new ValidatorException(msg);
        }
    }
    
    public void validateRemuneration(FacesContext context, UIComponent component, Object value){
        if("".equals(value.toString())) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kein Entgeltschlüssel angegeben", "Kein Entgeltschlüssel angegeben");
            throw new ValidatorException(msg);
        }
        if(value.toString().length() != 8) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Entgeltschlüssel müssen 8 Zeichen lang sein.", "Entgeltschlüssel müssen 8 Zeichen lang sein.");
            throw new ValidatorException(msg);
        }
        Optional<RemunerationType> remunTypeOpt = _insuranceFacade.getRemunerationType(value.toString());
        if (!remunTypeOpt.isPresent()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ggf. ungültiger Entgeltschlüssel.", "Ggf. ungültiger Entgeltschlüssel.");
            throw new ValidatorException(msg);
        }
    }
    
    public void addItem() {
        InsuranceNubNoticeItem item = new InsuranceNubNoticeItem();
//        if (_notice.getId() > 0) {
//            item.setInsuranceNubNoticeId(_notice.getId());
//        }
        _notice.getItems().add(item);
    }

    public void deleteItem(InsuranceNubNoticeItem item) {
        _notice.getItems().remove(item);
    }

    public String save() {
        _insuranceFacade.saveNubNotice(_notice);
        _sessionController.alertClient(Utils.getMessage("msgSave"));
        return "";
    }

    /**
     * provide the message to InEK
     *
     * @return
     */
    public String provide() {
        return "";
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
