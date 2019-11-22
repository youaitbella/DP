/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.Function;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChanges;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesWards;
import org.inek.dataportal.care.entities.StructuralChanges.WardsToChange;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.StructuralChangesType;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.facades.StructuralChangesFacade;
import org.inek.dataportal.care.utils.CareValueChecker;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.common.Conversation;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.TransferFileType;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.TransferFileCreator;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.utils.VzUtils;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class StructuralChangesEdit implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(StructuralChangesEdit.class.toString());

    @Inject
    private SessionController _sessionController;
    @Inject
    private StructuralChangesFacade _structuralChangesFacade;
    @Inject
    private DeptFacade _deptFacade;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private VzUtils _vzUtils;

    private List<DeptWard> _wards;

    private List<DeptWard> _selectedWards = new ArrayList<>();

    private StructuralChangesBaseInformation _structuralChangesBaseInformation;

    private Set<Integer> _iks = new HashSet<>();

    public Set<Integer> getIks() {
        return _iks;
    }

    public void setIks(Set<Integer> iks) {
        this._iks = iks;
    }

    public StructuralChangesBaseInformation getStructuralChangesBaseInformation() {
        return _structuralChangesBaseInformation;
    }

    public void setStructuralChangesBaseInformation(StructuralChangesBaseInformation structuralChangesBaseInformation) {
        this._structuralChangesBaseInformation = structuralChangesBaseInformation;
    }

    public List<StructuralChanges> getChangesBaseInformationsByType(StructuralChangesType structuralChangesType) {
        return _structuralChangesBaseInformation.getStructuralChanges().stream()
                .filter(c -> c.getStructuralChangesType().equals(structuralChangesType))
                .collect(Collectors.toList());
    }

    public List<DeptWard> getWards() {
        return _wards;
    }

    public void setWards(List<DeptWard> wards) {
        this._wards = wards;
    }

    public List<DeptWard> getSelectedWards() {
        return _selectedWards;
    }

    public void setSelectedWards(List<DeptWard> selectedWards) {
        this._selectedWards = selectedWards;
    }

    private String _correction;

    public String getCorrection() {
        return _correction;
    }

    public void setCorrection(String correction) {
        this._correction = correction;
    }

    public void askForCorrection(){
        Conversation conversation= new Conversation();
        conversation.setAccountId(_sessionController.getAccountId());
        conversation.setDataId(_structuralChangesBaseInformation.getId());
        conversation.setFunction(Function.STRUCTURAL_CHANGES);
        conversation.setInek(isInekUser());
        conversation.setMessage(_correction);
    }

    @PostConstruct
    private void init() {
        String idParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if ("new".equals(idParam)) {
            _structuralChangesBaseInformation = createNewStructuralChangesBaseInformation();
            loadValidIks();

            if (_iks.size() == 1) {
                _structuralChangesBaseInformation.setIk(_iks.stream().findFirst().get());
                _wards = _structuralChangesFacade.findWardsByIk(_structuralChangesBaseInformation.getIk());
            }
        } else {
            try {
                int id = Integer.parseInt(idParam);
                _structuralChangesBaseInformation = _structuralChangesFacade.findBaseInformationsById(id);

                if (isAccessAllowed(_structuralChangesBaseInformation)) {
                    _wards = _structuralChangesFacade.findWardsByIk(_structuralChangesBaseInformation.getIk());
                } else {
                    LOGGER.log(Level.INFO, "No access for IK: " + _structuralChangesBaseInformation.getIk());
                    Utils.navigate(Pages.NotAllowed.RedirectURL());
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "error init StructuralChangesEdit: " + ex + " --> " + ex.getMessage());
                Utils.navigate(Pages.NotAllowed.RedirectURL());
            }
        }
    }

    private boolean isAccessAllowed(StructuralChangesBaseInformation info) {
        return _accessManager.isAccessAllowed(Feature.CARE, info.getStatus(),
                Integer.MIN_VALUE, info.getIk());
    }


    private void loadValidIks() {
        Set<Integer> tmpAllowedIks = _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .map(c -> c.getIk())
                .collect(Collectors.toSet());

        for (Integer ik : tmpAllowedIks) {
            if (_structuralChangesFacade.findOpenOrSendBaseInformationsByIk(ik).isPresent()) {
                continue;
            }

            List<DeptBaseInformation> allByStatusAndIk = _deptFacade.getAllByStatusAndIk(WorkflowStatus.Provided, ik);
            if (allByStatusAndIk.stream().anyMatch(i -> i.getYear() >= 2018)) {
                _iks.add(ik);
            }
        }
    }

    public boolean isChangeIkAllowed() {
        return true;
    }

    public boolean isReadOnly() {
        return _accessManager.
                isReadOnly(Feature.CARE,
                        _structuralChangesBaseInformation.getStatus(),
                        _structuralChangesBaseInformation.getRequestedAccountId(),
                        _structuralChangesBaseInformation.getIk());
    }

    public boolean structuralChangesIsReadOnly(StructuralChanges change) {
        //return change.getStatus().getId() >= WorkflowStatus.Provided.getId(); todo? readOnly or single Element protected
        return isReadOnly();
    }

    private StructuralChangesBaseInformation createNewStructuralChangesBaseInformation() {
        StructuralChangesBaseInformation baseInfo = new StructuralChangesBaseInformation();
        baseInfo.setRequestedAccountId(_sessionController.getAccountId());
        baseInfo.setRequestedAt(new Date());
        baseInfo.setStatus(WorkflowStatus.New);
        return baseInfo;
    }

    public List<SelectItem> getChangeReasons() {
        return _structuralChangesFacade.findChangeReasons();
    }

    public List<SelectItem> getCloseReasons() {
        return _structuralChangesFacade.findCloseReasons();
    }

    public List<SelectItem> getCloseTmpReasons() {
        return _structuralChangesFacade.findTmpCloseReasons();
    }

    public List<SelectItem> getSensitiveAreas() {
        List<SelectItem> items = new ArrayList<>();
        for (SensitiveArea value : SensitiveArea.values()) {
            if (value.getId() < 0) {
                continue;
            }
            items.add(new SelectItem(value.getId(), value.getName()));
        }
        return items;
    }


    public void newChangeWard(DeptWard ward) {
        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.CHANGE);
        change.setWardsToChange(createNewWardsToChange(ward));
        change.getWardsToChange().setValidFrom(null);
        _structuralChangesBaseInformation.addStructuralChanges(change);
        scrollToId("change:tableChangeWard");
    }

    public void closeWardTemp(DeptWard ward) {
        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.CLOSE_TEMP);
        change.setWardsToChange(createNewWardsToChangeForTempClose(ward));
        _structuralChangesBaseInformation.addStructuralChanges(change);
        scrollToId("change:tableCloseTempWard");
    }

    public void closeWard(DeptWard ward) {
        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.CLOSE);
        change.setWardsToChange(createNewWardsToChange(ward));
        change.getWardsToChange().setValidFrom(null);
        _structuralChangesBaseInformation.addStructuralChanges(change);
        scrollToId("change:tableCloseWard");
    }

    public void createNewWard() {
        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.NEW);
        change.setWardsToChange(new WardsToChange());
        _structuralChangesBaseInformation.addStructuralChanges(change);
        scrollToId("change:tableNewWard");
    }

    private void scrollToId(String id) {
        PrimeFaces.current().scrollTo(id);
    }

    public void createNewWardFromSelectedWards() {
        if (_selectedWards.size() == 0) {
            DialogController.showInfoDialog("Keine Station ausgewählt", "Bitte wählen Sie mindestens eine Station aus");
            return;
        }

        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.COMBINE_WITH_NEW);
        change.setWardsToChange(new WardsToChange());
        createNewStructuralChangesWards(change);
        _structuralChangesBaseInformation.addStructuralChanges(change);

        _selectedWards.clear();
    }

    private void createNewStructuralChangesWards(StructuralChanges change) {
        for (DeptWard selectedWard : _selectedWards) {
            StructuralChangesWards structuralChangesWards = new StructuralChangesWards();
            structuralChangesWards.setDeptWard(selectedWard);
            change.addStructuralChangesWards(structuralChangesWards);
        }
    }

    private WardsToChange createNewWardsToChangeForTempClose(DeptWard ward) {
        WardsToChange wardsToChange = new WardsToChange(ward);
        wardsToChange.setDeptWard(ward);
        wardsToChange.setValidFrom(null);
        wardsToChange.setValidTo(null);
        return wardsToChange;
    }

    private WardsToChange createNewWardsToChange(DeptWard ward) {
        WardsToChange wardsToChange = new WardsToChange(ward);
        wardsToChange.setDeptWard(ward);
        return wardsToChange;
    }

    private StructuralChanges createNewChanges() {
        return new StructuralChanges();
    }

    public void changesBaseInformation(StructuralChanges change) {
        _structuralChangesBaseInformation.removeStructuralChanges(change);
    }

    public void save() {
        if (baseInformationHasErrors(_structuralChangesBaseInformation)) {
            return;
        }

        _structuralChangesFacade.save(_structuralChangesBaseInformation);
        TransferFileCreator.createObjectTransferFile(_sessionController, _structuralChangesBaseInformation,
                _structuralChangesBaseInformation.getIk(), TransferFileType.CareChanges);
        DialogController.showSaveDialog();
    }

    public void send() {
        if (baseInformationHasErrors(_structuralChangesBaseInformation)) {
            return;
        }
        if (_structuralChangesBaseInformation.getStructuralChanges().size() == 0) {
            DialogController.showInfoDialog("Senden nicht möglich", "Bitte geben Sie mindestens eine Strukturelle Veränderung an.");
            return;
        }

        _structuralChangesBaseInformation.setStatus(WorkflowStatus.Provided);

        for (StructuralChanges changes : _structuralChangesBaseInformation.getStructuralChanges()) {
            changes.setStatus(WorkflowStatus.Provided);
        }

        _structuralChangesFacade.save(_structuralChangesBaseInformation);
        TransferFileCreator.createObjectTransferFile(_sessionController, _structuralChangesBaseInformation,
                _structuralChangesBaseInformation.getIk(), TransferFileType.CareChanges);
        sendMail("StructuralChangesSendConfirm");
        Utils.navigate(Pages.CareStructuralChangesSummary.RedirectURL());
        DialogController.showSendDialog();
    }

    public boolean baseInformationHasErrors(StructuralChangesBaseInformation baseInfo) {
        List<StructuralChanges> structuralChanges = getChangesBaseInformationsByType(StructuralChangesType.CLOSE_TEMP);
        for (StructuralChanges structuralChange : structuralChanges) {
            if (structuralChange.getWardsToChange().getValidFrom().after(structuralChange.getWardsToChange().getValidTo())) {
                DialogController.showInfoDialog("Temporäre Abmeldungen unplausibel",
                        "Bei den temporären Abmeldungen gibt es unplausible Gültigkeitszeiträume. Bitte korrigieren Sie Ihre Eingabe");
                return true;
            }
        }
        return false;
    }

    public void isFabValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        if ("".equals(value.toString().trim())) {
            return;
        }

        if (!CareValueChecker.isValidFabNumber(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültige FAB"));
        }
        if (!_deptFacade.isValidFab(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültige FAB"));
        }
    }

    public void isCommentValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        if (value.toString().length() <= 20) {
            throw new ValidatorException(new FacesMessage("Kommentar zu kurz (Mindestens 20 Zeichen)"));
        }
    }

    public void isP21LocationCodeValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int locationCode = (Integer) value;

        //TODO: P21 Standort überprüfen
        /*
        if (_allowedP21LocationCodes.isEmpty() && locationCode == 0) {
            return;
        }

        if (!_allowedP21LocationCodes.contains(locationCode)) {
            throw new ValidatorException(new FacesMessage("Ungültiger Standort nach § 21 KHEntgG für diese IK"));
        }
        */
    }

    public void isVZLocationCodeValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int locationCode = CareValueChecker.extractFormalValidVzNumber("" + value);
        if (locationCode == 0) {
            return;
        }
        if (!_vzUtils.locationCodeIsValidForIk(_structuralChangesBaseInformation.getIk(), locationCode)) {
            throw new ValidatorException(new FacesMessage(
                    "In Ihrer Eingabe wurde eine Standortnummer erkannt. Sie ist jedoch für dieses IK ungültig."));
        }
    }

    public void navigateToSummary() {
        Utils.navigate(Pages.CareStructuralChangesSummary.RedirectURL());
    }

    public void ikChanged() {
        _wards = _structuralChangesFacade.findWardsByIk(_structuralChangesBaseInformation.getIk());
    }

    public boolean changeAllowed() {
        return _structuralChangesBaseInformation.getStatus().equals(WorkflowStatus.Provided)
                && _accessManager.userHasWriteAccess(Feature.CARE, _structuralChangesBaseInformation.getIk());
    }

    public void change() {
        _structuralChangesBaseInformation.setStatus(WorkflowStatus.New);
        for (StructuralChanges changes : _structuralChangesBaseInformation.getStructuralChanges()) {
            changes.setStatus(WorkflowStatus.New);
        }
    }

    public boolean fabRequired(WardsToChange wardToChange) {
        Integer sensitiveAreaId = wardToChange.getSensitiveAreaId();

        if (sensitiveAreaId == null) {
            return true;
        }

        return SensitiveArea.getById(sensitiveAreaId).isFabRequired();
    }

    private void sendMail(String mailTemplateName) {
        String salutation = _sessionController.getMailer().getFormalSalutation(_sessionController.getAccount());

        MailTemplate template = _sessionController.getMailer().getMailTemplate(mailTemplateName);
        MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", Integer.toString(_structuralChangesBaseInformation.getIk()));

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation}", salutation);

        if (!_sessionController.getMailer().sendMailTemplate(template, _sessionController.getAccount().getEmail())) {
            LOGGER.log(Level.SEVERE, "Fehler beim Emailversand an " + _structuralChangesBaseInformation.getIk() + "(Care Proof)");
            _sessionController.getMailer().sendException(Level.SEVERE,
                    "Fehler beim Emailversand an " + _structuralChangesBaseInformation.getIk() + "(Struktuelle Veränderung)", new Exception());
        }
    }

    public Boolean isInekUser() {
        return _sessionController.isInekUser(Feature.CARE);
    }

    public void acceptChanges() {
        DeptBaseInformation deptBaseInformation = _deptFacade.findDeptBaseInformationByIk(_structuralChangesBaseInformation.getIk());

        List<WardsToChange> wardsToChange = _structuralChangesBaseInformation.getStructuralChanges().stream().map(sc -> sc.getWardsToChange()).collect(Collectors.toList());
    }
}
