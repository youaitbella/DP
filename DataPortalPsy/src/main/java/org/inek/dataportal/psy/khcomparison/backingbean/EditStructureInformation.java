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
import java.util.HashSet;
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
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.adm.facade.LogFacade;
import org.inek.dataportal.common.enums.StructureInformationCategorie;
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
        info.addNewStructureInformation(StructureInformationCategorie.BedCount);
        info.addNewStructureInformation(StructureInformationCategorie.TherapyPartCount);
        for (StructureInformation sInfo : info.getStructureInformations()) {
            sInfo.setValidFrom(new Date());
            sInfo.setContent("0");
        }
        info.setCreatedBy(_sessionController.getAccountId());
        return info;
    }

    public void save() {
        if (_structureBaseInformation.getIk() == 0) {
            DialogController.showInfoDialog("Daten nicht vollständig", "Geben Sie eine IK an.");
            return;
        }

        if (!hasTherapyOrBeds(_structureBaseInformation)) {
            DialogController.showInfoDialog("Daten nicht vollständig", "Geben Sie eine Anzahl Planbetten bzw. teilstationärer Therapieplätze an.");
            return;
        }
        try {
            String errors = checkForDuplicatedDates(_structureBaseInformation);
            if ("".equals(errors)) {
                _structureBaseInformation.setLastChangeBy(_sessionController.getAccountId());
                _structureBaseInformation.setLastChanged(new Date());
                _structureBaseInformation = _aebFacade.save(_structureBaseInformation);
                saveChangeLogs(_changes);
                DialogController.showSaveDialog();
            } else {
                DialogController.showInfoDialog("Doppelte Gültigkeiten",
                        "Sie haben Doppele Gültigkeiten in den folgenden Bereichen: " + errors
                        + " Bitte geben Sie für jeden Bereich ein gültigkietsdatum nur einmal an");
            }

        } catch (Exception ex) {
            DialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
        }
    }

    private Boolean hasTherapyOrBeds(StructureBaseInformation baseInfo) {
        Boolean hasAny = false;

        for (StructureInformation info : baseInfo.getStructureInformations().stream()
                .filter(c -> c.getStructureCategorie() == StructureInformationCategorie.BedCount
                || c.getStructureCategorie() == StructureInformationCategorie.TherapyPartCount)
                .collect(Collectors.toList())) {
            if (!info.getContent().isEmpty()) {
                try {
                    int count = Integer.parseInt(info.getContent());
                    hasAny = count > 0;
                } catch (Exception ex) {

                }
            }
        }

        return hasAny;

    }

    private String checkForDuplicatedDates(StructureBaseInformation info) {
        Set<String> categories = new HashSet<>();
        String errors = "";

        for (StructureInformation sInfo : info.getStructureInformations()) {
            if (info.getStructureInformations().stream().filter(c -> c.getStructureCategorie() == sInfo.getStructureCategorie()
                    && c.getValidFrom().equals(sInfo.getValidFrom())).count() > 1) {
                categories.add(errors);
            }
        }

        for (String category : categories) {
            errors += category + ", ";
        }

        return errors;
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

    public List<StructureInformationCategorie> getValidSingleStructureCategoriesAreas() {
        List<StructureInformationCategorie> validInfos = new ArrayList<>();

        for (StructureInformationCategorie infoCat : StructureInformationCategorie.values()) {
            if (!validInfos.stream().anyMatch(c -> c.getArea().equals(infoCat.getArea()))
                    && infoCat.getCountElements() == 1) {
                validInfos.add(infoCat);
            }
        }

        return validInfos;
    }

    public List<StructureInformation> getStructureInformationsByStructureCategorie(String catName) {
        return _structureBaseInformation.getStructureInformations().stream()
                .filter(c -> c.getStructureCategorie() == StructureInformationCategorie.valueOf(catName))
                .collect(Collectors.toList());
    }

    public void saveNewValidFrom(String catName) {
        StructureInformation newInfo = new StructureInformation();
        newInfo.setBaseInformation(_structureBaseInformation);
        newInfo.setStructureCategorie(StructureInformationCategorie.valueOf(catName));
        newInfo.setValidFrom(new Date());
        _structureBaseInformation.addStructureInformation(newInfo);
        //_structureBaseInformation.addStructureInformation(copyStructureInformationForNewVality(info));
    }

//    public StructureInformation copyStructureInformationForNewVality(StructureInformation info) {
//        StructureInformation newInfo = new StructureInformation();
//        newInfo.setBaseInformation(info.getBaseInformation());
//        newInfo.setContent(info.getContent());
//        newInfo.setStructureCategorie(info.getStructureCategorie());
//        newInfo.setValidFrom(new Date());
//        return newInfo;
//    }
    public Boolean collapsCategorie(String catName) {
        return getStructureInformationsByStructureCategorie(catName).isEmpty();
    }

    public StructureInformationCategorie getStructureCategorie(String catName) {
        return StructureInformationCategorie.valueOf(catName);
    }

    public Boolean showNewValityDate(StructureInformation info) {
        List<StructureInformation> infos = _structureBaseInformation.getStructureInformations().stream()
                .filter(c -> c.getStructureCategorie() == info.getStructureCategorie())
                .collect(Collectors.toList());

        return showNewValityDate(info, infos);
    }

    public Boolean showNewValityDate(StructureInformation info, List<StructureInformation> infos) {
        if (infos.size() == 1) {
            return true;
        }

        if (infos.stream().anyMatch(c -> c.getValidFrom().after(info.getValidFrom()))) {
            return false;
        }

        return true;
    }
}
