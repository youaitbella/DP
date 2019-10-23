/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.StructuralChanges.StructuralChangesBaseInformation;
import org.inek.dataportal.care.entities.StructuralChanges.WardsToChange;
import org.inek.dataportal.care.enums.StructuralChangesType;
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
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lautenti
 */
@Named
@ViewScoped
public class StructuralChangesEdit implements Serializable {

    @Inject
    private SessionController _sessionController;
    @Inject
    private StructuralChangesFacade _structuralChangesFacade;
    @Inject
    private AccessManager _accessManager;

    private int _ik;

    private List<DeptStation> _wards;

    private List<StructuralChangesBaseInformation> _changesBaseInformations = new ArrayList<>();

    public List<StructuralChangesBaseInformation> getChangesBaseInformations() {
        return _changesBaseInformations;
    }

    public void setChangesBaseInformations(List<StructuralChangesBaseInformation> changesBaseInformations) {
        this._changesBaseInformations = changesBaseInformations;
    }

    public List<DeptStation> getWards() {
        return _wards;
    }

    public void setWards(List<DeptStation> wards) {
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
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
        } catch (Exception ey) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        }
    }

    private boolean isAccessAllowed(int ik) {
        return _accessManager.userHasWriteAccess(Feature.CARE, ik);
    }

    public void newChangeWard(DeptStation ward) {
        StructuralChangesBaseInformation info = createNewChangesBaseInformation();
        info.setStructuralChangesType(StructuralChangesType.CHANGE);
        info.setWardsToChange(createNewWardsToChange(ward));
        _changesBaseInformations.add(info);
    }

    public void deleteWard(DeptStation ward) {
        //TODO löschung der Stationen
    }

    private WardsToChange createNewWardsToChange(DeptStation ward) {
        WardsToChange wardsToChange = new WardsToChange(ward);
        wardsToChange.setMapVersion(ward.getMapVersion());
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
        if (!CareValueChecker.isValidFabNumber(value.toString())) {
            throw new ValidatorException(new FacesMessage("Ungültige FAB"));
        }
    }
}
