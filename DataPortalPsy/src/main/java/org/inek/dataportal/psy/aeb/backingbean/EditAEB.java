/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.aeb.backingbean;

import java.util.Date;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.aeb.entity.*;
import org.inek.dataportal.psy.aeb.facade.AEBFacade;
import org.inek.dataportal.psy.aeb.facade.AEBListItemFacade;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class EditAEB {

    @Inject
    private SessionController _sessionController;
    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private DialogController _dialogController;
    @Inject
    private AEBListItemFacade _aebListItemFacade;

    private AEBBaseInformation _aebBaseInformation;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            _aebBaseInformation = createNewAebBaseInformation();
        } else if ("new".equals(id)) {
            _aebBaseInformation = createNewAebBaseInformation();
            _aebBaseInformation.setCreatedFrom(_sessionController.getAccountId());
        } else {
            _aebBaseInformation = _aebFacade.findAEBBaseInformation(Integer.parseInt(id));
        }
    }

    public AEBBaseInformation getAebBaseInformation() {
        return _aebBaseInformation;
    }

    public void setAebBaseInformation(AEBBaseInformation aebBaseInformation) {
        this._aebBaseInformation = aebBaseInformation;
    }

    public SelectItem[] getAccommodationItems() {
        return _aebListItemFacade.getAccommodationItems();
    }

    public SelectItem[] getAmbulantItems() {
        return _aebListItemFacade.getAmbulantItems();
    }

    public Boolean isReadOnly() {
        if (_aebBaseInformation != null) {
            return _aebBaseInformation.getStatus() == WorkflowStatus.Provided;
        } else {
            return true;
        }
    }

    private AEBBaseInformation createNewAebBaseInformation() {
        AEBBaseInformation info = new AEBBaseInformation();
        info.setStructureInformation(new AEBStructureInformation());
        info.getStructureInformation().setBaseInformation(info);
        info.setAebPageB1(new AEBPageB1());
        info.getAebPageB1().setBaseInformation(info);
        return info;
    }

    public void save() {
        removeEmptyEntries(_aebBaseInformation);
        _aebBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
        _aebBaseInformation.setLastChanged(new Date());
        //
        _aebBaseInformation.getStructureInformation().setAccommodationId(1);
        _aebBaseInformation.getStructureInformation().setAmbulantPerformanceId(1);
        //
        try {
            _aebBaseInformation = _aebFacade.save(_aebBaseInformation);
            _dialogController.showSaveDialog();
        } catch (Exception ex) {
            _dialogController.showWarningDialog("Fehler beim Speichern", "Vorgang abgebrochen");
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

    public void handleFileUpload(FileUploadEvent event) {
        _dialogController.showInfoDialog(event.getFile().getFileName(), "Datei erfolgreich hochgeladen");
    }

    public Set<Integer> getValidIks() {
        return _sessionController.getAccount().getFullIkSet();
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
}
