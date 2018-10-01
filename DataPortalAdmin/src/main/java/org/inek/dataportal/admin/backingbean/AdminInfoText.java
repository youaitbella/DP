/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.backingbean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.infotext.entity.InfoText;
import org.inek.dataportal.common.data.infotext.facade.InfoTextFacade;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author lanzrama
 */
@Named
@FeatureScoped
public class AdminInfoText implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private SessionController _sessionController;
    @Inject
    private InfoTextFacade _infoTextFacade;
    @Inject
    private DialogController _dialogController;

    //<editor-fold defaultstate="collapsed" desc="List of Info Texts">
    private List<InfoText> _listOfInfoTexts;

    public void setListOfInfoTexts(List<InfoText> listOfInfoTexts) {
        _listOfInfoTexts = listOfInfoTexts;
    }

    public List<InfoText> getListOfInfoTexts() {
        return _listOfInfoTexts;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="New Key Name">
    private String _newKey;

    public String getNewKey() {
        return _newKey;
    }

    public void setNewKey(String newKey) {
        this._newKey = newKey;
    }
    //</editor-fold>

    @PostConstruct
    private void init() {
        _listOfInfoTexts = _infoTextFacade.getAllInfoTexts("DE");
        _newKey = "";
    }

    public void addInfoText() {
        InfoText newText = new InfoText(_newKey);
        _listOfInfoTexts.add(newText);
        _newKey = "";
    }

    public void checkInput(FacesContext context, UIComponent component, Object value) {

        String input = "" + value;
        for (InfoText infoText : _listOfInfoTexts) {

            if (infoText.getKey().equals(input)) {
                String msg = "Der Schlüssel existiert bereits.";
                throw new ValidatorException(new FacesMessage(msg));
            }

        }

    }

    public void save() {

        for (InfoText infoText : _listOfInfoTexts) {
            if (infoText.getModified()) {
                _infoTextFacade.save(infoText);
            }
        }
        _dialogController.showInfoDialog("Die Daten wurden gespeichert", "Infotexte gespeichert");

    }

    public void error() {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!", "Der Schlüssel existiert bereits."));
    }

}
