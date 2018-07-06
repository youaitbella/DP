/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.backingbean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
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

    @Inject
    private InfoTextFacade _infoTextFacade;

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

    //<editor-fold defaultstate="collapsed" desc="Drop Down Text">
    @PostConstruct
    private void initData() {
        _listOfInfoTexts = _infoTextFacade.getAllInfoTexts("DE");
    }

    public void addInfoText() {
        InfoText newText = new InfoText(_newKey);
        _listOfInfoTexts.add(newText);
        _newKey = "";
    }

    public void save() {

        for (InfoText infoText : _listOfInfoTexts) {
            if (infoText.getModified()) {
                _infoTextFacade.save(infoText);
            }
        }

    }

}
