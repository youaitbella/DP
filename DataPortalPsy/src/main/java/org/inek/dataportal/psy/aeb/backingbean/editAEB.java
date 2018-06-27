/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.aeb.backingbean;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.faceletvalidators.NameValidator;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.aeb.entity.*;
import org.inek.dataportal.psy.aeb.facade.AEBFacade;
import org.primefaces.event.CellEditEvent;

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
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem("-1", "Bitte Eintrag wählen");
        items[1] = new SelectItem("1", "Bonn");
        items[2] = new SelectItem("2", "Nicht Bonn");
        return items;
    }

    public SelectItem[] getAmbulantItems() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem("-1", "Bitte Eintrag wählen");
        items[1] = new SelectItem("1", "Bonn");
        items[2] = new SelectItem("2", "Nicht Bonn");
        return items;
    }

    public Boolean isReadOnly() {
        return false;
    }

    private void createNewAebBaseInformation() {
        _aebBaseInformation = new AEBBaseInformation();
        _aebBaseInformation.setCreatedFrom(_sessionController.getAccountId());
        _aebBaseInformation.setCreated(new Date());

        _aebBaseInformation.setStructureInformation(new AEBStructureInformation());
        _aebBaseInformation.getStructureInformation().setBaseInformation(_aebBaseInformation);

    }

    public void save() {
        _aebBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
        _aebBaseInformation.setLastChanged(new Date());

        //
        _aebBaseInformation.getStructureInformation().setAccommodationId(1);
        _aebBaseInformation.getStructureInformation().setAmbulantPerformanceId(1);
        //
        _aebBaseInformation = _aebFacade.save(_aebBaseInformation);
        _dialogController.showWarningDialog("Speichern Erfolgreich", "Daten gespeichert");
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

    public int getCaseCountSum() {
        int sum = 0;
        for (AEBPageE1_1 page : _aebBaseInformation.getAebPageE1_1()) {
            sum += page.getCaseCount();
        }
        return sum;
    }

    public int getSumValuationRadioSum() {
        int sum = 0;
        for (AEBPageE1_1 page : _aebBaseInformation.getAebPageE1_1()) {
            sum += page.getSumValuationRadio();
        }
        return sum;
    }

    public int getCalculationDaysSum() {
        int sum = 0;
        for (AEBPageE1_1 page : _aebBaseInformation.getAebPageE1_1()) {
            sum += page.getCalculationDays();
        }
        return sum;
    }

    public void isValidPepp(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (input.length() != 5) {
            String msg = Utils.getMessage("Ungültige Pepp");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }
}
