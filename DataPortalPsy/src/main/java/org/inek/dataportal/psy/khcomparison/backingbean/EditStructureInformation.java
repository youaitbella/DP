/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import org.inek.dataportal.common.data.adm.ChangeLog;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;
import org.inek.dataportal.common.data.adm.facade.LogFacade;
import org.inek.dataportal.common.enums.WorkflowStatus;

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
    private LogFacade _logFacade;

    private StructureInformation _structureInformation;
    private Boolean _readOnly;
    private Date _newValidFromDate = new Date();
    private List<ChangeLog> _changes = new ArrayList<>();
    private List<StructureInformation> _allStructureInformations = new ArrayList<>();
    private int _editableId;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null || "new".equals(id)) {
            _structureInformation = createNewStructureInformation();
            setReadOnly(false);
        } else {
            _structureInformation = _aebFacade.findStructureInformation(Integer.parseInt(id));
            _editableId = _structureInformation.getId();
            setAllStructureInformations(_aebFacade.getAllStructureInformationByIk(_structureInformation.getIk()));
            setReadOnly();
        }
    }

    public Set<Integer> getAllowedIks() {
        Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.HC_HOSPITAL);
        return allowedIks
                .stream()
                .filter(ik -> !_aebFacade.structureInformaionAvailable(ik))
                .collect(Collectors.toSet());
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

    public void setReadOnly() {
        if (_structureInformation != null) {
            setReadOnly(_accessManager.isReadOnly(Feature.HC_HOSPITAL,
                    WorkflowStatus.New,
                    _sessionController.getAccountId(),
                    _structureInformation.getIk()));
        } else {
            setReadOnly(false);
        }
    }

    private StructureInformation createNewStructureInformation() {
        StructureInformation info = new StructureInformation();
        info.setCreatedFrom(_sessionController.getAccountId());
        return info;
    }

    public void save() {
        if (_structureInformation.getIk() > 0 && (_structureInformation.getBedCount() != 0 || _structureInformation.getTherapyPartCount() != 0)) {
            _structureInformation.setLastChangeFrom(_sessionController.getAccountId());
            _structureInformation.setLastChanged(new Date());
            try {
                _structureInformation = _aebFacade.save(_structureInformation);
                saveChangeLogs(_changes);
                _dialogController.showSaveDialog();
            } catch (Exception ex) {
                _dialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
            }
        } else {
            _dialogController.showInfoDialog("Daten nicht vollständig", "Bitte wählen Sie eine IK und geben Sie eine Anzahl Planbetten bzw. teilstationärer Therapieplätze an.");
        }
    }

    public void saveNewValidFrom() {
        StructureInformation newInformation = copyStructureInformation(_structureInformation);
        newInformation.setValidFrom(getNewValidFromDate());

        newInformation.setLastChangeFrom(_sessionController.getAccountId());
        newInformation.setLastChanged(new Date());

        try {
            _structureInformation = _aebFacade.save(newInformation);
            _changes.clear();
            _editableId = _structureInformation.getId();
            setAllStructureInformations(_aebFacade.getAllStructureInformationByIk(_structureInformation.getIk()));
            _dialogController.showSaveDialog();
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
        }
    }

    public StructureInformation copyStructureInformation(StructureInformation info) {
        StructureInformation newInfo = new StructureInformation();
        newInfo.setAccommodationText(info.getAccommodationText());
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
        Optional<ChangeLog> oldChange = _changes.stream()
                .filter(c -> c.getField().equals(field))
                .findFirst();
        if (oldChange.isPresent()) {
            _changes.remove(oldChange.get());
        }
        ChangeLog log = new ChangeLog(_sessionController.getAccountId(),
                Feature.HC_HOSPITAL,
                "StructureInformation",
                _structureInformation.getId(),
                _structureInformation.getClass().getSimpleName(),
                field,
                oldValue,
                newValue);
        _changes.add(log);
    }

    private void saveChangeLogs(List<ChangeLog> changes) {
        _logFacade.saveChangeLogs(changes);
        changes.clear();
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

    public Boolean canSave() {
        return !_accessManager.isReadOnly(Feature.HC_HOSPITAL,
                WorkflowStatus.New,
                _sessionController.getAccountId(),
                _structureInformation.getIk());
    }
}
