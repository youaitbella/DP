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
public class editAEB {

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
            createNewAebBaseInformation();
        } else if ("new".equals(id)) {
            createNewAebBaseInformation();
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
        return false;
    }

    private void createNewAebBaseInformation() {
        _aebBaseInformation = new AEBBaseInformation();
        _aebBaseInformation.setCreatedFrom(_sessionController.getAccountId());

        _aebBaseInformation.setStructureInformation(new AEBStructureInformation());
        _aebBaseInformation.getStructureInformation().setBaseInformation(_aebBaseInformation);

    }

    public void save() {
        removeEmptyEntries();
        _aebBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
        _aebBaseInformation.setLastChanged(new Date());
        //
        _aebBaseInformation.getStructureInformation().setAccommodationId(1);
        _aebBaseInformation.getStructureInformation().setAmbulantPerformanceId(1);
        //
        _aebBaseInformation = _aebFacade.save(_aebBaseInformation);
        _dialogController.showInfoDialog("Speichern Erfolgreich", "Daten gespeichert");
    }

    public void send() {
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

    public void handleFileUpload(FileUploadEvent event) {
        _dialogController.showInfoDialog(event.getFile().getFileName(), "Datei erfolgreich hochgeladen");
    }

    public int getCaseCountSum() {
        int sum = 0;
        for (AEBPageE1_1 page : _aebBaseInformation.getAebPageE1_1()) {
            sum += page.getCaseCount();
        }
        return sum;
    }

    public double getSumValuationRadioSum() {
        double sum = 0;
        for (AEBPageE1_1 page : _aebBaseInformation.getAebPageE1_1()) {
            sum += page.getSumValuationRadio();
        }
        return sum;
    }

    public double getCalculationDaysSum() {
        double sum = 0;
        for (AEBPageE1_1 page : _aebBaseInformation.getAebPageE1_1()) {
            sum += page.getCalculationDays();
        }
        return sum;
    }

    public double calcSumValuationRadioSumE1_2() {
        double sum = 0;
        for (AEBPageE1_2 page : _aebBaseInformation.getAebPageE1_2()) {
            sum += page.getSumValuationRadio();
        }
        return sum;
    }

    public double calcSumValuationRadioSumE2() {
        double sum = 0;
        for (AEBPageE2 page : _aebBaseInformation.getAebPageE2()) {
            sum += page.getSumValuationRadio();
        }
        return sum;
    }

    public Set<Integer> getValidIks() {
        return _sessionController.getAccount().getFullIkSet();
    }

    private void removeEmptyEntries() {
        _aebBaseInformation.getAebPageE1_1().removeIf(c -> c.getPepp().length() == 0);
        _aebBaseInformation.getAebPageE1_2().removeIf(c -> c.getEt().length() == 0);
        _aebBaseInformation.getAebPageE2().removeIf(c -> c.getZe().length() == 0);
    }

    public void peppChanged(AEBPageE1_1 page) {
        if (page.getPepp().length() == 5) {
            page.setValuationRadioDay(_aebListItemFacade.getValuationRadioDays(page.getPepp(),
                    page.getCompensationClass(), _aebBaseInformation.getYear()));
        } else {
            page.setValuationRadioDay(0.0);
        }
        page.calculateSum();
    }

    public void zeChanged(AEBPageE2 page) {
        if (page.getZe().length() == 9) {
            page.setValuationRadioDay(0.123);
            page.calculateSum();
        } else {
            page.setValuationRadioDay(0.0);
        }
    }

    public void etChanged(AEBPageE1_2 page) {
        if (page.getEt().length() == 5) {
            page.setValuationRadioDay(0.123);
            page.calculateSum();
        } else {
            page.setValuationRadioDay(0.0);
        }
    }
}
