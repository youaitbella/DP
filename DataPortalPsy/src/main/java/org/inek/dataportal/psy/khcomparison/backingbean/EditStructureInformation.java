/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.khcomparison.entity.*;
import org.inek.dataportal.psy.khcomparison.facade.AEBFacade;
import org.inek.dataportal.psy.khcomparison.facade.AEBListItemFacade;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class EditStructureInformation {

    @Inject
    private SessionController _sessionController;
    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private DialogController _dialogController;
    @Inject
    private AEBListItemFacade _aebListItemFacade;
    @Inject
    private AccessManager _accessManager;

    private StructureInformation _structureInformation;
    private Boolean _readOnly;
    private Date _newValidFromDate = new Date();

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null || "new".equals(id)) {
            _structureInformation = createNewStructureInformation();
        } else {
            _structureInformation = _aebFacade.findStructureInformation(Integer.parseInt(id));
        }
        setReadOnly();
    }

    public Date getNewValidFromDate() {
        return _newValidFromDate;
    }

    public void setNewValidFromDate(Date newDate) {
        this._newValidFromDate = newDate;
    }

    public Boolean isReadOnly() {
        return _readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this._readOnly = readOnly;
    }

    public StructureInformation getStructureInformation() {
        return _structureInformation;
    }

    public void setStructureInformation(StructureInformation structureInformation) {
        this._structureInformation = structureInformation;
    }

    public List<SelectItem> getAccommodationItems() {
        return _aebListItemFacade.getAccommodationItems();
    }

    public List<SelectItem> getAmbulantItems() {
        return _aebListItemFacade.getAmbulantItems();
    }

    public void setReadOnly() {
//        if (_structureInformation != null) {
//            setReadOnly(_accessManager.isReadOnly(Feature.AEB,
//                    _aebBaseInformation.getStatus(),
//                    _sessionController.getAccountId(),
//                    _aebBaseInformation.getIk()));
//        } else if (_aebBaseInformation.getIk() == 0) {
//            setReadOnly(false);
//        } else {
//            setReadOnly(true);
//        }
        setReadOnly(false);
    }

    private StructureInformation createNewStructureInformation() {
        StructureInformation info = new StructureInformation();
        info.setCreatedFrom(_sessionController.getAccountId());
        return info;
    }

    public void save() {
        _structureInformation.setLastChangeFrom(_sessionController.getAccountId());
        _structureInformation.setLastChanged(new Date());
        try {
            _structureInformation = _aebFacade.save(_structureInformation);
            _dialogController.showSaveDialog();
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
        }
    }

    public void saveNewValidFrom() {
        StructureInformation newInformation = copyStructureInformation(_structureInformation);
        newInformation.setValidFrom(getNewValidFromDate());

        newInformation.setLastChangeFrom(_sessionController.getAccountId());
        newInformation.setLastChanged(new Date());

        _structureInformation.setLastChangeFrom(_sessionController.getAccountId());
        _structureInformation.setLastChanged(new Date());
        try {
            _aebFacade.save(_structureInformation);
            _structureInformation = _aebFacade.save(newInformation);
            _dialogController.showSaveDialog();
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
        }
    }

    public StructureInformation copyStructureInformation(StructureInformation info) {
        StructureInformation newInfo = new StructureInformation();
        newInfo.setAccommodationId(info.getAccommodationId());
        newInfo.setAccommodationText(info.getAccommodationText());
        newInfo.setAmbulantPerformanceId(info.getAmbulantPerformanceId());
        newInfo.setAmbulantPerformanceMain(info.getAmbulantPerformanceMain());
        newInfo.setAmbulantStructure(info.getAmbulantStructure());
        newInfo.setBedCount(info.getBedCount());
        newInfo.setCareProvider(info.getCareProvider());
        newInfo.setCreatedFrom(info.getCreatedFrom());
        newInfo.setDismissManagement(info.getDismissManagement());
        newInfo.setIk(info.getIk());
        newInfo.setPerformanceAreas(info.getPerformanceAreas());
        newInfo.setRegionalCare(info.getRegionalCare());
        newInfo.setSPCenterText(info.getSPCenterText());
        newInfo.setSocialPsychiatryService(info.getSocialPsychiatryService());
        newInfo.setTherapyPartCount(info.getTherapyPartCount());
        return newInfo;
    }
}
