/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.Function;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChanges;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesWards;
import org.inek.dataportal.care.entities.StructuralChanges.WardsToChange;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.StructuralChangesType;
import org.inek.dataportal.care.facades.DeptFacade;
import org.inek.dataportal.care.facades.StructuralChangesFacade;
import org.inek.dataportal.care.utils.AggregatedWardsHelper;
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
import org.inek.dataportal.common.utils.DateUtils;
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
    private transient VzUtils _vzUtils;

    private List<DeptWard> _wards;
    private DeptBaseInformation _deptBaseInformation;
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
        return _wards.stream().sorted(Comparator.comparing(this::determineSortKey)).collect(Collectors.toList());
    }

    private String determineSortKey(DeptWard ward) {
        String key = ward.getLocationCodeP21()
                + "|" + ward.getLocationText()
                + "|" + ward.getWardName().toLowerCase().replace(" ", "")
                + "|" + DateUtils.toAnsi(ward.getValidFrom());
        return key;
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

    public void askForCorrection() {
        Conversation conversation = new Conversation();
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
                obtainWards();
            }
        } else {
            try {
                int id = Integer.parseInt(idParam);
                _structuralChangesBaseInformation = _structuralChangesFacade.findBaseInformationsById(id);

                if (isAccessAllowed(_structuralChangesBaseInformation)) {
                    obtainWards();
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

    private void obtainWards() {
        _deptBaseInformation = _deptFacade.findDeptBaseInformationByIk(_structuralChangesBaseInformation.getIk());
        int version = _structuralChangesBaseInformation.getBasedOnVersionId();
        if (version <= 0) {
            version = _deptBaseInformation.getCurrentVersionId();
            _structuralChangesBaseInformation.setBasedOnVersionId(version);
        }
        _wards = _deptBaseInformation.obtainWardsByVersion(version);
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

        if (AggregatedWardsHelper.aggregatedWards(calculateNewWards()).stream().anyMatch(w -> w.getDifferentBedCount())) {
            DialogController.showInfoDialog("Senden nicht möglich",
                    "Bitte achten Sie darauf, keine widersprüchlichen Bettenzahlen anzugeben " +
                            "(in der zusammengefassten Stationsübersicht als \"unterschiedlich\" gekennzeichnet).");
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
        obtainWards();
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

        return SensitiveArea.fromId(sensitiveAreaId).isFabRequired();
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
        List<DeptWard> wards = calculateNewWards();
        for (DeptWard ward : wards) {
            _deptBaseInformation.getDepts().stream()
                    .filter(d -> d.getId() == ward.getDept().getId())
                    .findAny().ifPresent(d -> {
                // for testing purpos: create new one
                DeptWard newWard = d.addNewDeptWard(ward.getMapVersion(), ward.getValidFrom(), ward.getValidTo());
                newWard.setWardName(ward.getWardName());
                newWard.setLocationCodeP21(ward.getLocationCodeP21());
                newWard.setLocationCodeVz(ward.getLocationCodeVz());
                newWard.setLocationText(ward.getLocationText());
                newWard.setBedCount(ward.getBedCount());
                newWard.setBaseDeptWardId(ward.getBaseDeptWardId());
                //    d.addDeptWard(ward);
            });
        }
        _deptBaseInformation.setCurrentVersion(wards.get(0).getMapVersion());
        _deptFacade.save(_deptBaseInformation);
        _structuralChangesBaseInformation.setStatus(WorkflowStatus.Taken);
        _structuralChangesFacade.save(_structuralChangesBaseInformation);
    }

    public List<DeptWard> calculateNewWards() {
        int ik = _structuralChangesBaseInformation.getIk();

        List<DeptWard> wards = obtainAndPrepareWards(_deptBaseInformation);
        List<StructuralChanges> structuralChanges = _structuralChangesBaseInformation.getStructuralChanges();
        if (structuralChanges.size() == 0) {
            return wards;
        }

        processChanges(wards, structuralChanges, ik);
        processDeletions(wards, structuralChanges);
        processTemporaryDeletions(wards, structuralChanges);
        processAdditions(wards, structuralChanges, ik);
        return wards;
    }

    private List<DeptWard> obtainAndPrepareWards(DeptBaseInformation deptBaseInformation) {
        MapVersion version = new MapVersion(_sessionController.getAccountId());
        return _wards.stream()
                .map(w -> {
                    DeptWard n = new DeptWard(w);
                    n.setMapVersion(version);
                    n.setIsInitial(false);
                    n.setBaseDeptWardId(w.getId());
                    return n;
                })
                .collect(Collectors.toList());
    }

    void processChanges(List<DeptWard> wards, List<StructuralChanges> structuralChanges, int ik) {
        structuralChanges
                .stream().filter(sc -> sc.getStructuralChangesType() == StructuralChangesType.CHANGE)
                .map(sc -> sc.getWardsToChange())
                .filter(w -> w.getValidFrom() != null)
                .sorted(Comparator.comparing(WardsToChange::getValidFrom))
                .forEachOrdered(changeWard -> {
                    List<DeptWard> deptWards = wards.stream()
                            .filter(w -> w.getBaseDeptWardId() == changeWard.getDeptWard().getId())
                            .sorted(Comparator.comparing(DeptWard::getValidFrom))
                            .collect(Collectors.toList());
                    assert deptWards.size() > 0;
                    DeptWard firstDeptWard = deptWards.get(0);
                    if (firstDeptWard.getValidFrom().compareTo(changeWard.getValidFrom()) >= 0) {
                        // before the first existing
                        DeptWard newWard = new DeptWard(firstDeptWard);

                        Date validTo = DateUtils.addDays(firstDeptWard.getValidFrom(), -1);
                        newWard.setValidTo(validTo);
                        newWard.setBaseDeptWardId(firstDeptWard.getBaseDeptWardId());
                        wards.add(newWard);
                        return;
                    }
                    DeptWard lastDeptWard = deptWards.get(deptWards.size() - 1);
                    if (lastDeptWard.getValidTo().compareTo(changeWard.getValidFrom()) <= 0) {
                        // after the last existing
                        DeptWard newWard = new DeptWard(lastDeptWard);
                        newWard.setValidFrom(changeWard.getValidFrom());
                        newWard.setValidTo(DateUtils.getMaxDate());
                        newWard.setBaseDeptWardId(lastDeptWard.getBaseDeptWardId());
                        wards.add(newWard);
                        return;
                    }
                    DeptWard deptWard = deptWards.stream()
                            .filter(w -> w.getValidFrom().compareTo(changeWard.getValidFrom()) <= 0
                                    && w.getValidTo().compareTo(changeWard.getValidFrom()) >= 0)
                            .findAny().get();
                    DeptWard newWard = new DeptWard(deptWard);
                    newWard.setValidFrom(changeWard.getValidFrom());
                    newWard.setValidTo(deptWard.getValidTo());
                    newWard.setBaseDeptWardId(lastDeptWard.getBaseDeptWardId());
                    copyValues(changeWard, newWard, ik);
                    wards.add(newWard);
                    deptWard.setValidTo(DateUtils.addDays(changeWard.getValidFrom(), -1));
                    if (deptWard.getValidFrom().compareTo(deptWard.getValidTo()) > 0) {
                        // invalid
                        wards.remove(deptWard);
                    }
                });
    }

    void processDeletions(List<DeptWard> wards, List<StructuralChanges> structuralChanges) {
        structuralChanges
                .stream().filter(sc -> sc.getStructuralChangesType() == StructuralChangesType.CLOSE)
                .map(sc -> sc.getWardsToChange())
                .filter(w -> w.getValidFrom() != null)
                .sorted(Comparator.comparing(WardsToChange::getValidFrom))
                .forEachOrdered(changeWard -> {
                    List<DeptWard> deptWards = wards.stream()
                            .filter(w -> w.getBaseDeptWardId() == changeWard.getDeptWard().getId())
                            .sorted(Comparator.comparing(DeptWard::getValidFrom))
                            .collect(Collectors.toList());
                    for (DeptWard deptWard : deptWards) {
                        if (deptWard.getValidFrom().compareTo(changeWard.getValidFrom()) >= 0) {
                            wards.remove(deptWard);
                            continue;
                        }
                        if (deptWard.getValidFrom().compareTo(changeWard.getValidFrom()) <= 0
                                && deptWard.getValidTo().compareTo(changeWard.getValidFrom()) >= 0) {
                            deptWard.setValidTo(DateUtils.addDays(changeWard.getValidFrom(), -1));
                        }
                    }
                });
    }

    void processTemporaryDeletions(List<DeptWard> wards, List<StructuralChanges> structuralChanges) {
        structuralChanges
                .stream().filter(sc -> sc.getStructuralChangesType() == StructuralChangesType.CLOSE_TEMP)
                .map(sc -> sc.getWardsToChange())
                .filter(w -> w.getValidFrom() != null && w.getValidTo() != null)
                .sorted(Comparator.comparing(WardsToChange::getValidFrom))
                .forEachOrdered(changeWard -> {
                    List<DeptWard> deptWards = wards.stream()
                            .filter(w -> w.getBaseDeptWardId() == changeWard.getDeptWard().getId())
                            .sorted(Comparator.comparing(DeptWard::getValidFrom))
                            .collect(Collectors.toList());
                    for (DeptWard deptWard : deptWards) {
                        if (deptWard.getValidFrom().compareTo(changeWard.getValidFrom()) >= 0
                                && deptWard.getValidTo().compareTo(changeWard.getValidTo()) <= 0) {
                            wards.remove(deptWard);
                            continue;
                        }
                        boolean adjusted = false;
                        if (deptWard.getValidFrom().compareTo(changeWard.getValidFrom()) < 0
                                && deptWard.getValidTo().compareTo(changeWard.getValidFrom()) >= 0) {
                            DeptWard wardBefore = new DeptWard(deptWard);
                            wardBefore.setValidTo(DateUtils.addDays(changeWard.getValidFrom(), -1));
                            wardBefore.setBaseDeptWardId(deptWard.getBaseDeptWardId());
                            wards.add(wardBefore);
                            adjusted = true;
                        }
                        if (deptWard.getValidFrom().compareTo(changeWard.getValidTo()) <= 0
                                && deptWard.getValidTo().compareTo(changeWard.getValidTo()) > 0) {
                            DeptWard wardAfter = new DeptWard(deptWard);
                            wardAfter.setValidFrom(DateUtils.addDays(changeWard.getValidTo(), 1));
                            wardAfter.setBaseDeptWardId(deptWard.getBaseDeptWardId());
                            wards.add(wardAfter);
                            adjusted = true;
                        }
                        if (adjusted) {
                            wards.remove(deptWard);
                        }
                    }
                });
    }

    void processAdditions(List<DeptWard> wards, List<StructuralChanges> structuralChanges, int ik) {
        //wards.get(0).getDept().getBaseInformation().getDepts().stream().filter(d -> d.getS)

        structuralChanges
                .stream().filter(sc -> sc.getStructuralChangesType() == StructuralChangesType.NEW)
                .map(sc -> sc.getWardsToChange())
                .filter(w -> w.getValidFrom() != null)
                .sorted(Comparator.comparing(WardsToChange::getValidFrom))
                .forEachOrdered(changeWard -> {
                    DeptWard deptWard = new DeptWard(wards.get(0).getMapVersion());
                    deptWard.setValidFrom(changeWard.getValidFrom());
                    deptWard.setValidTo(DateUtils.getMaxDate());
                    copyValues(changeWard, deptWard, ik);
                    deptWard.setDept(findDept(changeWard, wards.get(0)));
                    wards.add(deptWard);
                    // todo changeWard.getSensitiveAreaId()
                });
    }

    /**
     * Search Dept by best guess
     *
     * @param changeWard
     * @param deptWard
     * @return
     */
    private Dept findDept(WardsToChange changeWard, DeptWard deptWard) {
        if (deptWard.getDept() == null) {
            // during test
            return new Dept();
        }
        String sensitiveArea = SensitiveArea.fromId(changeWard.getSensitiveAreaId()).getName();

        List<Dept> allDepts = deptWard.getDept().getBaseInformation().getDepts();
        List<Dept> deptsWithArea = allDepts.stream().filter(d -> d.getSensitiveArea().equals(sensitiveArea)).collect(Collectors.toList());
        if (deptsWithArea.size() == 0) {
            return allDepts.get(0);
        }
        return deptsWithArea.stream().filter(d -> d.getDeptNumber().equals("" + changeWard.getDeptId())).findAny().orElse(deptsWithArea.get(0));
    }

    private void copyValues(WardsToChange changeWard, DeptWard newWard, int ik) {
        newWard.setDeptName(changeWard.getDeptName());
        newWard.setFab(changeWard.getFab());
        newWard.setWardName(changeWard.getWardName());
        newWard.setLocationCodeP21(changeWard.getLocationP21());
        newWard.setLocationText(changeWard.getLocationVz());
        newWard.setBedCount(changeWard.getBeds());
    }

    public List<AggregatedWards> getAggregatedWards() {
        return AggregatedWardsHelper.aggregatedWards(calculateNewWards());
    }

}
