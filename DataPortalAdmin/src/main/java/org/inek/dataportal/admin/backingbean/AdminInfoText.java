/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;

import javax.inject.Inject;
import javax.inject.Named;

import org.inek.dataportal.common.controller.SessionController;

import org.inek.dataportal.common.data.infotext.entity.InfoText;
import org.inek.dataportal.common.data.infotext.facade.InfoTextFacade;

import org.inek.dataportal.common.scope.FeatureScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author lanzrama
 */
@Named
@FeatureScoped
public class AdminInfoText implements Serializable {

    @Inject
    private InfoTextFacade _infoTextFacade;
    @Inject
    private SessionController _sessionController;

    //<editor-fold defaultstate="collapsed" desc="List of Info Texts">
    private List<InfoText> _listOfInfoTexts;

    public void setListOfInfoTexts(List<InfoText> listOfInfoTexts) {

        _listOfInfoTexts = listOfInfoTexts;

    }

    public List<InfoText> getListOfInfoTexts() {

        return _listOfInfoTexts;

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Drop Down Text">
    private String _dropDownText;

    public String getDropDownText() {
        return _dropDownText;
    }

    public void setDropDownText(String dropDownText) {
        this._dropDownText = dropDownText;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Info Text Text">
    private String _infoTextText;

    public String getInfoTextText() {
        return _infoTextText;
    }

    public void setInfoTextText(String infoTextText) {
        this._infoTextText = infoTextText;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Current Info Text">
    private InfoText _currentInfoText;

    public InfoText getCurrentInfoText() {
        return _currentInfoText;
    }

    public void setCurrentInfoText(InfoText currentInfoText) {
        this._currentInfoText = currentInfoText;
    }

    //</editor-fold>
    @PostConstruct
    private void initData() {

        _dropDownText = "";
        _infoTextText = "";
        _listOfInfoTexts = _infoTextFacade.getAllInfoTexts("DE");

    }

    public void onSave() {

        setInfoTextText(_listOfInfoTexts.get(1).getModified() + "");

        for (InfoText infoText : _listOfInfoTexts) {

            if (infoText.getModified()) {

                _infoTextFacade.save(infoText);

            }

        }

    }

}
