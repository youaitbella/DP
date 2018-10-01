/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.base.feature.dropbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.base.feature.dropbox.entities.DropBox;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class DropBoxCreator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject private org.inek.dataportal.base.feature.dropbox.facade.DropBoxFacade _dropBoxFacade;
    @Inject private SessionController _sessionController;
    @Inject private ApplicationTools _appTools;
    @Inject private DropBoxTools _dropboxTools;
    @Inject private AccessManager _accessManager;

    private int _dropboxTypeId = 1;
    private int _ik;
    private String _description;

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public int getDropboxTypeId() {
        return _dropboxTypeId;
    }

    public void setDropboxTypeId(int dropboxTypeId) {
        _dropboxTypeId = dropboxTypeId;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    // </editor-fold>
    public List<SelectItem> getIkItems() {
        Set<Integer> _iks = _accessManager.ObtainIksForCreation(Feature.DROPBOX);
        List<SelectItem> ikItems = new ArrayList<>();
        for (int ik : _iks) {
            ikItems.add(new SelectItem(ik, ik + " " + _appTools.retrieveHospitalInfo(ik)));
        }
        if (ikItems.size() > 1) {
            ikItems.add(0, new SelectItem(null, Utils.getMessage("lblChooseEntry")));
        }
        return ikItems;
    }

    public String create() {
        Utils.getFlash().put("dbId", createDropBox());
        return Pages.DropBoxUpload.URL();
    }

    public String cancel() {
        return Pages.MainApp.URL();
    }

    public boolean getIkRequired() {
        return _dropboxTools.getDropBoxType(_dropboxTypeId).isNeedsIK();
    }

    private int createDropBox() {
        DropBox dropBox = new DropBox();
        dropBox.setAccountId(_sessionController.getAccountId());
        dropBox.setDirectory("");
        dropBox.setDescription(_description);
        dropBox.setDropboxType(_dropboxTools.getDropBoxType(_dropboxTypeId));
        dropBox.setIK(_ik);
        dropBox = _dropBoxFacade.createDropBox(dropBox);
        return dropBox.getDropBoxId();
    }
}
