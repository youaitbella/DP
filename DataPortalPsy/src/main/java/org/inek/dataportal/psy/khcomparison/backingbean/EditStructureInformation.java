/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import org.inek.dataportal.common.data.adm.ChangeLog;
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
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
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
    private AccessManager _accessManager;
    @Inject
    private LogFacade _logFacade;

    private StructureBaseInformation _structureBaseInformation;
    private Boolean _readOnly;
    private List<ChangeLog> _changes = new ArrayList<>();

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null || "new".equals(id)) {
            _structureBaseInformation = createNewStructureBsseInformation();
            setReadOnly(false);
        } else {
            _structureBaseInformation = _aebFacade.findStructureBaseInformation(Integer.parseInt(id));
            setReadOnly();
        }
    }

    public Set<Integer> getAllowedIks() {
        Set<Integer> allowedIks = _accessManager.ObtainIksForCreation(Feature.HC_HOSPITAL);
        return allowedIks
                .stream()
                .filter(ik -> !_aebFacade.structureBaseInformaionAvailable(ik))
                .collect(Collectors.toSet());
    }

    public Boolean isReadOnly() {
        return _readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this._readOnly = readOnly;
    }

    public StructureBaseInformation getStructureBaseInformation() {
        return _structureBaseInformation;
    }

    public void setStructureBaseInformation(StructureBaseInformation structureBaseInformation) {
        this._structureBaseInformation = structureBaseInformation;
    }

    public void setReadOnly() {
        if (_structureBaseInformation != null) {
            setReadOnly(_accessManager.isReadOnly(Feature.HC_HOSPITAL,
                    WorkflowStatus.New,
                    _sessionController.getAccountId(),
                    _structureBaseInformation.getIk()));
        } else {
            setReadOnly(false);
        }
    }

    private StructureBaseInformation createNewStructureBsseInformation() {
        StructureBaseInformation info = new StructureBaseInformation();
        //------------------------------------------------------------ Befüllung der Structure Categories
        info.setCreatedBy(_sessionController.getAccountId());
        return info;
    }

    public void save() {
//        if (_structureBaseInformation.getIk() > 0 && (_structureBaseInformation.getBedCount() != 0
//                || _structureBaseInformation.getTherapyPartCount() != 0)) {
//            _structureBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
//            _structureBaseInformation.setLastChanged(new Date());
//            try {
//                _structureBaseInformation = _aebFacade.save(_structureBaseInformation);
//                saveChangeLogs(_changes);
//                _dialogController.showSaveDialog();
//            } catch (Exception ex) {
//                _dialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
//            }
//        } else {
//            _dialogController.showInfoDialog("Daten nicht vollständig", "Bitte wählen Sie eine IK "
//                    + "und geben Sie eine Anzahl Planbetten bzw. teilstationärer Therapieplätze an.");
//        }
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
                _structureBaseInformation.getId(),
                _structureBaseInformation.getClass().getSimpleName(),
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

    public Boolean canSave() {
        return !_accessManager.isReadOnly(Feature.HC_HOSPITAL,
                WorkflowStatus.New,
                _sessionController.getAccountId(),
                _structureBaseInformation.getIk());
    }
}
