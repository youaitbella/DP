/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.KhComparison.entities.StructureBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.adm.ChangeLog;
import org.inek.dataportal.common.data.adm.facade.LogFacade;
import org.inek.dataportal.common.enums.StructureInformationCategorie;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.TransferFileCreator;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.khcomparison.helper.StructureInformationChecker;
import org.inek.dataportal.psy.khcomparison.helper.StructureinformationHelper;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.inek.dataportal.common.mail.Mailer;

/**
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


    private static final Logger LOGGER = Logger.getLogger("StructureInformationEdit");

    private StructureBaseInformation _structureBaseInformation;
    private Boolean _readOnly;
    private List<ChangeLog> _changes = new ArrayList<>();

    private Date _filterValidFrom = new Date();
    private Date _filterValidTo = new Date();
    private Boolean _filterActive = false;
    private Boolean _ikIsModellIntention = false;

    public Boolean getFilterActive() {
        return _filterActive;
    }

    public void setFilterActive(Boolean filterActive) {
        this._filterActive = filterActive;
    }

    public Date getFilterValidFrom() {
        return _filterValidFrom;
    }

    public void setFilterValidFrom(Date filterValidFrom) {
        this._filterValidFrom = filterValidFrom;
    }

    public Date getFilterValidTo() {
        return _filterValidTo;
    }

    public void setFilterValidTo(Date filterValidTo) {
        this._filterValidTo = filterValidTo;
    }

    public Boolean getIkIsModellIntention() {
        return _ikIsModellIntention;
    }

    public void setIkIsModellIntention(Boolean ikIsModellIntention) {
        this._ikIsModellIntention = ikIsModellIntention;
    }

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null || "new".equals(id)) {
            _structureBaseInformation = createNewStructureBsseInformation();
            setReadOnly(false);
        } else {
            _structureBaseInformation = _aebFacade.findStructureBaseInformation(Integer.parseInt(id));
            setReadOnly();
            setIkIsModellIntention(_aebFacade.ikHasModelIntention(_structureBaseInformation.getIk()));
        }
    }

    public Set<Integer> getAllowedIks() {
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.HC_HOSPITAL);
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
        info.setCreatedBy(_sessionController.getAccountId());
        return info;
    }

    public void save() {
        if (_structureBaseInformation.getIk() == 0) {
            DialogController.showInfoDialog("Daten nicht vollständig", "Geben Sie eine IK an.");
            return;
        }

        ensureStructureInformations();

        String checkError = StructureInformationChecker.checkStructureBaseInformation(_structureBaseInformation);
        if (!"".equals(checkError)) {
            DialogController.showInfoDialog("Daten nicht vollständig", checkError);
            return;
        }

        try {
            String errors = StructureinformationHelper.checkForDuplicatedDates(_structureBaseInformation);
            if ("".equals(errors)) {
                _structureBaseInformation.setLastChangeBy(_sessionController.getAccountId());
                _structureBaseInformation.setLastChanged(new Date());
                _structureBaseInformation = _aebFacade.save(_structureBaseInformation);
                saveChangeLogs(_changes);
                setIkIsModellIntention(_aebFacade.ikHasModelIntention(_structureBaseInformation.getIk()));
                DialogController.showSaveDialog();


                try {
                    TransferFileCreator.createObjectTransferFile(_sessionController, _structureBaseInformation,
                            _structureBaseInformation.getIk(), "Structureinformation");
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error duringTransferFileCreation Structureinformation: ik: " + _structureBaseInformation.getIk());
                    _sessionController.getMailer().sendError("Error duringTransferFileCreation Structureinformation: ik: "
                            + _structureBaseInformation.getIk(), ex);
                }


            } else {
                DialogController.showInfoDialog("Doppelte Gültigkeiten",
                        "Sie haben Doppele Gültigkeiten in den folgenden Bereichen: " + errors
                                + " Bitte geben Sie für jeden Bereich ein Gültigkietsdatum nur einmal an");
            }

        } catch (Exception ex) {
            DialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
            _sessionController.getMailer().sendError(ex.getMessage(), ex);
        }
    }

    private void ensureStructureInformations() {
        for (StructureInformation info : getStructureInformationsByStructureCategorie(StructureInformationCategorie.BedCount.name())) {
            if (info.getContent() == null) {
                info.setContent("");
            }
        }

        for (StructureInformation info : getStructureInformationsByStructureCategorie(StructureInformationCategorie.TherapyPartCount.name())) {
            if (info.getContent() == null) {
                info.setContent("");
            }
        }
    }

    public void handleChange(ValueChangeEvent event) {
        createActionLog(event.getComponent().getId(),
                event.getOldValue().toString(),
                event.getNewValue().toString());
    }

    public void handleDateChange(ValueChangeEvent event) {
        String categorie = (String) event.getComponent().getAttributes().get("categorie");

        Date oldDate = (Date) event.getOldValue();
        Date newDate = (Date) event.getNewValue();

        if (oldDate == null || newDate == null) {
            return;
        }

        if (StructureinformationHelper.newDateChangeOrder(oldDate, newDate, getStructureInformationsByCategorieNoFilter(categorie))) {
            DialogController.showInfoDialog("Achtung", "Durch das neue Datum wird die Reihenfolge der Gültigkeiten verändert");
        }

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

    public Boolean canSave() {
        return !_accessManager.isReadOnly(Feature.HC_HOSPITAL,
                WorkflowStatus.New,
                _sessionController.getAccountId(),
                _structureBaseInformation.getIk());
    }

    public List<StructureInformation> getStructureInformationsByStructureCategorie(String catName) {
        if (_filterActive) {
            return StructureinformationHelper.getStructureInformationsByStructureCategorieFiltered(_structureBaseInformation, catName,
                    _filterValidFrom, _filterValidTo);
        } else {
            return getStructureInformationsByCategorieNoFilter(catName);
        }

    }

    private List<StructureInformation> getStructureInformationsByCategorieNoFilter(String catName) {
        return _structureBaseInformation.getStructureInformations().stream()
                .filter(c -> c.getStructureCategorie() == StructureInformationCategorie.valueOf(catName))
                .sorted(Comparator.comparing(StructureInformation::getValidFrom, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public void saveNewValidFrom(String catName) {
        StructureInformation newInfo = new StructureInformation();
        newInfo.setBaseInformation(_structureBaseInformation);
        newInfo.setStructureCategorie(StructureInformationCategorie.valueOf(catName));
        newInfo.setValidFrom(createCurrentDate());
        _structureBaseInformation.addStructureInformation(newInfo);
    }

    private Date createCurrentDate() {
        LocalDate date = LocalDate.now();

        return java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

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

    public void activateFilterStructureInformation() {
        _filterActive = true;
    }

    public void deactivateFilterStructureInformation() {
        _filterActive = false;
        _filterValidFrom = createCurrentDate();
        _filterValidTo = createCurrentDate();
    }

    public void deleteStructureinformation(StructureInformation info) {
        _structureBaseInformation.removeStructureInformation(info);
    }

    public Boolean structureInformationReadonly(StructureInformation info) {
        return StructureinformationHelper.structureInformationIsReadonly(info,
                getStructureInformationsByStructureCategorie(info.getStructureCategorie().name()), createCurrentDate()) && !isReadOnly();
    }

    public List<SelectItem> validModelIntentionItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(0, "Keins"));
        items.add(new SelectItem(1, "Teilweise"));
        items.add(new SelectItem(2, "Vollständig"));
        return items;
    }
}
