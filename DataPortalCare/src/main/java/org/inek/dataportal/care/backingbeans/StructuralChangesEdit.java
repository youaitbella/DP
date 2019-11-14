/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
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
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.List;
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

    @PostConstruct
    private void init() {
        String idParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if ("new".equals(idParam)) {
            _structuralChangesBaseInformation = createNewStructuralChangesBaseInformation();
            loadValidIks();

            if (_iks.size() == 1) {
                _structuralChangesBaseInformation.setIk(_iks.stream().findFirst().get());
                _wards = _structuralChangesFacade.findWardsByIkAndDate(_structuralChangesBaseInformation.getIk(), new Date());
            }
        } else {
            try {
                int id = Integer.parseInt(idParam);
                _structuralChangesBaseInformation = _structuralChangesFacade.findBaseInformationsById(id);

                if (isAccessAllowed(_structuralChangesBaseInformation.getIk())) {
                    _wards = _structuralChangesFacade.findWardsByIkAndDate(_structuralChangesBaseInformation.getIk(), new Date());
                } else {
                    LOGGER.log(Level.INFO, "No access for IK: " + _structuralChangesBaseInformation.getIk());
                    Utils.navigate(Pages.NotAllowed.RedirectURL());
                    return;
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "error init StructuralChangesEdit: " + ex + " --> " + ex.getMessage());
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
        }
    }

    private void loadValidIks() {
        Set<Integer> tmpAllowedIks = _sessionController.getAccount().getAccessRights().stream()
                .filter(c -> c.canRead() && c.getFeature() == Feature.CARE)
                .map(c -> c.getIk())
                .collect(Collectors.toSet());

        for (Integer ik : tmpAllowedIks) {
            Optional<StructuralChangesBaseInformation> openBaseInformationsByIk = _structuralChangesFacade.findOpenBaseInformationsByIk(ik);
            if (openBaseInformationsByIk.isPresent()) {
                continue;
            }

            List<StructuralChangesBaseInformation> sendBaseInformationsByIk = _structuralChangesFacade.findSendBaseInformationsByIk(ik);

            List<DeptBaseInformation> allByStatusAndIk = _deptFacade.getAllByStatusAndIk(WorkflowStatus.Provided, ik);
            if (allByStatusAndIk.size() == 1 && sendBaseInformationsByIk.size() == 0) {
                _iks.add(ik);
            }
        }
    }

    public boolean isChangeIkAllowed() {
        return true;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean structuralChangesIsReadOnly(StructuralChanges change) {
        return change.getStatus().getId() >= WorkflowStatus.Provided.getId();
    }

    private StructuralChangesBaseInformation createNewStructuralChangesBaseInformation() {
        StructuralChangesBaseInformation baseInfo = new StructuralChangesBaseInformation();
        baseInfo.setRequestedAccountId(_sessionController.getAccountId());
        baseInfo.setRequestedAt(new Date());
        return baseInfo;
    }

    public List<SelectItem> getCloseReasons() {
        return _structuralChangesFacade.findDeleteReasons();
    }

    public List<SelectItem> getSensitiveAreas() {
        List<SelectItem> items = new ArrayList<>();
        for (SensitiveArea value : SensitiveArea.values()) {
            items.add(new SelectItem(value.getId(), value.getName()));
        }
        return items;
    }

    private boolean isAccessAllowed(int ik) {
        return _accessManager.userHasWriteAccess(Feature.CARE, ik);
    }

    public void newChangeWard(DeptWard ward) {
        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.CHANGE);
        change.setWardsToChange(createNewWardsToChange(ward));
        _structuralChangesBaseInformation.addStructuralChanges(change);
    }

    public void closeWard(DeptWard ward) {
        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.CLOSE);
        change.setWardsToChange(createNewWardsToChange(ward));
        _structuralChangesBaseInformation.addStructuralChanges(change);
    }

    public void createNewWard() {
        StructuralChanges change = createNewChanges();
        change.setStructuralChangesType(StructuralChangesType.NEW);
        change.setWardsToChange(new WardsToChange());
        _structuralChangesBaseInformation.addStructuralChanges(change);
    }

    public void createNewWardFromSelectedWards() {
        if (_selectedWards.size() == 0) {
            DialogController.showInfoDialog("Keine Station ausgewählt", "Bitte wählen Sie mindesten eine Station aus");
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

    private WardsToChange createNewWardsToChange(DeptWard ward) {
        WardsToChange wardsToChange = new WardsToChange(ward);
        wardsToChange.setDeptWard(ward);
        return wardsToChange;
    }

    private StructuralChanges createNewChanges() {
        StructuralChanges change = new StructuralChanges();
        change.setStatus(WorkflowStatus.New);
        return change;
    }

    public void changesBaseInformation(StructuralChanges change) {
        _structuralChangesBaseInformation.removeStructuralChanges(change);
    }

    public void save() {
        _structuralChangesFacade.save(_structuralChangesBaseInformation);
        DialogController.showSaveDialog();
    }

    public void send() {
        for (StructuralChanges changes : _structuralChangesBaseInformation.getStructuralChanges()) {
            changes.setStatus(WorkflowStatus.Provided);
        }

        _structuralChangesFacade.save(_structuralChangesBaseInformation);
        DialogController.showSendDialog();
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
        int locationCode = (Integer) value;

        if (locationCode == 0) {
            return;
        }

        if (!CareValueChecker.isFormalValidVzNumber(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültiger Standort für dieses IK"));
        }
    }

    public void navigateToSummary() {
        Utils.navigate(Pages.CareStructuralChangesSummary.RedirectURL());
    }

    public void ikChanged() {
        _wards = _structuralChangesFacade.findWardsByIkAndDate(_structuralChangesBaseInformation.getIk(), new Date());
    }
}
