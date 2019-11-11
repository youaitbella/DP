/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.WardsToChange;
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
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    private int _ik;

    private List<DeptWard> _wards;

    private List<StructuralChangesBaseInformation> _changesBaseInformations = new ArrayList<>();

    public List<StructuralChangesBaseInformation> getChangesBaseInformationsByType(StructuralChangesType structuralChangesType) {
        return _changesBaseInformations.stream().filter(c -> c.getStructuralChangesType().equals(structuralChangesType)).collect(Collectors.toList());
    }

    public List<DeptWard> getWards() {
        return _wards;
    }

    public void setWards(List<DeptWard> wards) {
        this._wards = wards;
    }


    @PostConstruct
    private void init() {
        String ikParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("ik");
        try {
            int ik = Integer.parseInt(ikParam);
            if (isAccessAllowed(ik)) {
                _ik = ik;
                _wards = _structuralChangesFacade.findWardsByIkAndDate(ik, new Date());
            } else {
                LOGGER.log(Level.INFO, "No access for IK: " + ik);
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "error open Page: " + ex + " --> " + ex.getMessage());
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        }
    }

    private boolean isAccessAllowed(int ik) {
        return _accessManager.userHasWriteAccess(Feature.CARE, ik);
    }

    public void newChangeWard(DeptWard ward) {
        StructuralChangesBaseInformation info = createNewChangesBaseInformation();
        info.setStructuralChangesType(StructuralChangesType.CHANGE);
        info.setWardsToChange(createNewWardsToChange(ward));
        _changesBaseInformations.add(info);
    }

    public void closeWard(DeptWard ward) {
        StructuralChangesBaseInformation info = createNewChangesBaseInformation();
        info.setStructuralChangesType(StructuralChangesType.CLOSE);
        info.setWardsToChange(createNewWardsToChange(ward));
        _changesBaseInformations.add(info);
    }

    private WardsToChange createNewWardsToChange(DeptWard ward) {
        WardsToChange wardsToChange = new WardsToChange(ward);
        wardsToChange.setDeptWard(ward);
        return wardsToChange;
    }

    private StructuralChangesBaseInformation createNewChangesBaseInformation() {
        StructuralChangesBaseInformation info = new StructuralChangesBaseInformation();
        info.setIk(_ik);
        info.setRequestedAccountId(_sessionController.getAccountId());
        info.setStatus(WorkflowStatus.New);
        return info;
    }

    public void changesBaseInformation(StructuralChangesBaseInformation baseInfo) {
        _changesBaseInformations.remove(baseInfo);
    }

    public void send() {
        for (StructuralChangesBaseInformation baseInfo : _changesBaseInformations) {
            baseInfo.setRequestedAt(new Date());
            baseInfo.setStatus(WorkflowStatus.Provided);
            _structuralChangesFacade.save(baseInfo);
        }

        DialogController.showSaveDialog();
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
        if (!CareValueChecker.isFormalValidVzNumber(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültiger Standort für dieses IK"));
        }
    }
}
