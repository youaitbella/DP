/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import org.inek.dataportal.common.data.KhComparison.entities.*;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.data.KhComparison.checker.AebChecker;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;
import org.inek.dataportal.common.data.KhComparison.importer.AebImporter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class Edit {

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

    private AEBBaseInformation _aebBaseInformation;
    private List<Integer> _validDatayears = new ArrayList<>();
    private Boolean _readOnly;
    private String _errorMessage = "";

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            _aebBaseInformation = createNewAebBaseInformation();
            _aebBaseInformation.setCreatedFrom(_sessionController.getAccountId());
        } else if ("new".equals(id)) {
            _aebBaseInformation = createNewAebBaseInformation();
            _aebBaseInformation.setCreatedFrom(_sessionController.getAccountId());
        } else {
            _aebBaseInformation = _aebFacade.findAEBBaseInformation(Integer.parseInt(id));
        }
        ikChanged();
        setReadOnly();
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this._errorMessage = errorMessage;
    }

    public Boolean isReadOnly() {
        return _readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this._readOnly = readOnly;
    }

    public List<Integer> getValidDatayears() {
        return _validDatayears;
    }

    public void setValidDatayears(List<Integer> validDatayears) {
        this._validDatayears = validDatayears;
    }

    public AEBBaseInformation getAebBaseInformation() {
        return _aebBaseInformation;
    }

    public void setAebBaseInformation(AEBBaseInformation aebBaseInformation) {
        this._aebBaseInformation = aebBaseInformation;
    }

    public List<SelectItem> getStructureCategories() {
        return _aebListItemFacade.getStructureCategorie();
    }

    public void setReadOnly() {
        if (_aebBaseInformation != null) {
            setReadOnly(!_accessManager.isReadAllowed(Feature.AEB,
                    _sessionController.getAccount(), _aebBaseInformation.getIk()));
        } else if (_aebBaseInformation.getIk() == 0) {
            setReadOnly(false);
        } else {
            setReadOnly(true);
        }
    }

    private AEBBaseInformation createNewAebBaseInformation() {
        AEBBaseInformation info = new AEBBaseInformation();
        info.setTyp(0);
        for (OccupationalCategory cat : _aebFacade.getOccupationalCategories()) {
            PersonalAgreed agreed = new PersonalAgreed();
            agreed.setOccupationalCategory(cat);
            info.addPersonalAgreed(agreed);
        }

        info.setAebPageB1(new AEBPageB1());
        info.getAebPageB1().setBaseInformation(info);
        return info;
    }

    public void save() {
        if (baseInfoisComplete(_aebBaseInformation)) {
            removeEmptyEntries(_aebBaseInformation);
            _aebBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
            _aebBaseInformation.setLastChanged(new Date());
            try {
                _aebBaseInformation = _aebFacade.save(_aebBaseInformation);
                _dialogController.showSaveDialog();
            } catch (Exception ex) {
                _dialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
            }
        } else {
            _dialogController.showWarningDialog("Fehler beim Speichern", "Bitte geben Sie eine gültige IK und Datenjahr an");
        }
    }

    public void send() {
        _aebBaseInformation.setStatus(WorkflowStatus.Provided);
        _aebBaseInformation.setSend(new Date());
        save();
    }

    public void addNewPageE1_1() {
        _aebBaseInformation.addAebPageE1_1();
    }

    public void removePageE1_1(AEBPageE1_1 page) {
        _aebBaseInformation.removeAebPageE1_1(page);
    }

    public void addNewPageE1_2() {
        _aebBaseInformation.addAebPageE1_2();
    }

    public void removePageE1_2(AEBPageE1_2 page) {
        _aebBaseInformation.removeAebPageE1_2(page);
    }

    public void addNewPageE2() {
        _aebBaseInformation.addAebPageE2();
    }

    public void removePageE2(AEBPageE2 page) {
        _aebBaseInformation.removeAebPageE2(page);
    }

    public void addNewPageE3_1() {
        _aebBaseInformation.addAebPageE3_1();
    }

    public void removePageE3_1(AEBPageE3_1 page) {
        _aebBaseInformation.removeAebPageE3_1(page);
    }

    public void addNewPageE3_2() {
        _aebBaseInformation.addAebPageE3_2();
    }

    public void removePageE3_2(AEBPageE3_2 page) {
        _aebBaseInformation.removeAebPageE3_2(page);
    }

    public void addNewPageE3_3() {
        _aebBaseInformation.addAebPageE3_3();
    }

    public void removePageE3_3(AEBPageE3_3 page) {
        _aebBaseInformation.removeAebPageE3_3(page);
    }

    public void removeRegionStructurParticularities(RegionStructurParticularities region) {
        _aebBaseInformation.removeRegionStructurParticularities(region);
    }

    public void addNewRegionStructurParticularities() {
        _aebBaseInformation.addNewRegionStructurParticularities();
    }

    public void handleFileUpload(FileUploadEvent event) {
        AebImporter importer = new AebImporter();
        AebChecker checker = new AebChecker(_aebListItemFacade);
        try {
            if (importer.startImport(_aebBaseInformation, event.getFile().getInputstream())) {
                checker.checkAeb(_aebBaseInformation);
                setErrorMessage(checker.getMessage());
                setErrorMessage(getErrorMessage() + "\n \n --> " + importer.getCounter() + " Zeilen eingelesen");
                _dialogController.showInfoDialog("Upload abgeschlossen", "Ihre Daten wurden erfolgreich hochgeladen");
            }
        } catch (Exception ex) {
            _dialogController.showWarningDialog("Upload fehlgeschlagen", "Fehler beim Upload. Bitte versuchen Sie es erneut");
        }
    }

    private void removeEmptyEntries(AEBBaseInformation baseInformation) {
        baseInformation.getAebPageE1_1().removeIf(c -> c.getPepp().length() == 0);
        baseInformation.getAebPageE1_2().removeIf(c -> c.getEt().length() == 0);
        baseInformation.getAebPageE2().removeIf(c -> c.getZe().length() == 0);
        baseInformation.getAebPageE3_1().removeIf(c -> c.getRenumeration().length() == 0);
        baseInformation.getAebPageE3_2().removeIf(c -> c.getZe().length() == 0);
        baseInformation.getAebPageE3_3().removeIf(c -> c.getRenumeration().length() == 0);
    }

    public void peppChanged(AEBPageE1_1 page) {
        if (page.getPepp().length() == 5) {
            page.setValuationRadioDay(_aebListItemFacade.getValuationRadioDaysByPepp(page.getPepp(),
                    page.getCompensationClass(), _aebBaseInformation.getYear()));
        } else {
            page.setValuationRadioDay(0.0);
        }
    }

    public void zeChanged(AEBPageE2 page) {
        if (page.getZe().length() == 7) {
            page.setValuationRadioDay(_aebListItemFacade.getValuationRadioDaysByZe(page.getZe(),
                    _aebBaseInformation.getYear()));
        } else {
            page.setValuationRadioDay(0.0);
        }
    }

    public void etChanged(AEBPageE1_2 page) {
        if (page.getEt().length() == 7) {
            page.setValuationRadioDay(_aebListItemFacade.getValuationRadioDaysByEt(page.getEt(),
                    _aebBaseInformation.getYear()));
        } else {
            page.setValuationRadioDay(0.0);
        }
    }

    public void handleDocumentUpload(FileUploadEvent file) {
        PsyDocument doc = new PsyDocument();
        doc.setName(file.getFile().getFileName());
        doc.setContent(file.getFile().getContents());
        _aebBaseInformation.addPsyDocument(doc);
    }

    public void removeDocument(PsyDocument doc) {
        _aebBaseInformation.removePsyDocument(doc);
    }

    public StreamedContent downloadDocument(PsyDocument doc) {
        ByteArrayInputStream stream = new ByteArrayInputStream(doc.getContent());
        return new DefaultStreamedContent(stream, "applikation/" + doc.getContentTyp(), doc.getName());
    }

    public void change() {
        _aebBaseInformation.setStatus(WorkflowStatus.CorrectionRequested);
        _aebBaseInformation = _aebFacade.save(_aebBaseInformation);
    }

    public void ikChanged() {
        List<Integer> usedYears = _aebFacade.getUsedDataYears(_aebBaseInformation.getIk(), 0);
        setValidDatayears(getValideDatayears(getAllowedDataYears(), usedYears));
    }

    public List<Integer> getValideDatayears(List<Integer> allowedYears, List<Integer> usedYears) {
        allowedYears.removeAll(usedYears);
//        if (_aebBaseInformation.getStatus().getId() > WorkflowStatus.New.getId()) {
//            allowedYears.add(_aebBaseInformation.getYear());
//        }
        return allowedYears;
    }

    public List<Integer> getAllowedDataYears() {
        List<Integer> years = new ArrayList<>();
        years.add(2018);
        return years;
    }

    public List<Integer> getAllowedIks() {
        List<Integer> iks = new ArrayList<>();
        for (Integer ik : _aebFacade.getAllowedIks(_sessionController.getAccountId(),
                Utils.getTargetYear(Feature.AEB), 0)) {
            if (_accessManager.isCreateAllowed(Feature.AEB, _sessionController.getAccount(), ik)) {
                iks.add(ik);
            }
        }
        if (_aebBaseInformation.getIk() != 0) {
            iks.add(_aebBaseInformation.getIk());
        }
        return iks;
    }

    private boolean baseInfoisComplete(AEBBaseInformation info) {
        if (info.getIk() != 0 && info.getYear() != 0) {
            return true;
        }
        return false;
    }
}
