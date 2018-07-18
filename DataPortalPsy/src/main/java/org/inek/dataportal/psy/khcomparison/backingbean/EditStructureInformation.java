/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.khcomparison.entity.*;
import org.inek.dataportal.psy.khcomparison.facade.AEBFacade;
import org.inek.dataportal.psy.khcomparison.facade.AEBListItemFacade;
import org.inek.dataportal.psy.khcomparison.facade.ActionLogFacade;

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
    @Inject
    private ActionLogFacade _actionLogFacade;

    private StructureInformation _structureInformation;
    private Boolean _readOnly;
    private Date _newValidFromDate = new Date();
    private List<ActionLog> _actions = new ArrayList<>();
    private List<StructureInformation> _allStructureInformations = new ArrayList<>();
    private int _editableId;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null || "new".equals(id)) {
            _structureInformation = createNewStructureInformation();
        } else {
            _structureInformation = _aebFacade.findStructureInformation(Integer.parseInt(id));
            _editableId = _structureInformation.getId();
            setAllStructureInformations(_aebFacade.getAllStructureInformationByIk(_structureInformation.getIk()));
        }
        setReadOnly();
    }

    public List<StructureInformation> getAllStructureInformations() {
        return _allStructureInformations;
    }

    public void setAllStructureInformations(List<StructureInformation> allStructureInformations) {
        this._allStructureInformations = allStructureInformations;
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
            saveActionLogs(_actions);
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

        try {
            _structureInformation = _aebFacade.save(newInformation);
            _actions.clear();
            _editableId = _structureInformation.getId();
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

    public void handleChange(ValueChangeEvent event) {
        createActionLog(event.getComponent().getId(),
                event.getOldValue().toString(),
                event.getNewValue().toString());
    }

    private void createActionLog(String field, String oldValue, String newValue) {
        if (_actions.stream().anyMatch(c -> c.getField().equals(field))) {
            _actions.stream()
                    .filter(c -> c.getField().equals(field))
                    .findFirst()
                    .get()
                    .setNewValue(newValue);
        } else {
            ActionLog log = new ActionLog(_sessionController.getAccountId(),
                    Feature.AEB.name(),
                    "StructureInformation",
                    field,
                    oldValue,
                    newValue);
            _actions.add(log);
        }
    }

    private void saveActionLogs(List<ActionLog> actions) {
        _actionLogFacade.saveActionLogs(actions);
        actions.clear();
    }

    public void checkDate(FacesContext context, UIComponent component, Object value) {
        Date input = (Date) value;
        if (input.before(_structureInformation.getValidFrom()) || input.equals(_structureInformation.getValidFrom())) {
            String msg = "Die neue Gültigkeit muss nach der jetzigen sein";
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public String convertDate(Date date) {
        return (new SimpleDateFormat("dd.MM.yyyy")).format(date);
    }

    public void handleChangeStructureInformation(ValueChangeEvent event) {
        reloadStructureInformation(Integer.parseInt(event.getNewValue().toString()));
    }

    private void reloadStructureInformation(int id) {
        _structureInformation = _aebFacade.findStructureInformation(id);
        setReadOnly(_structureInformation.getId() != _editableId);
    }
}
