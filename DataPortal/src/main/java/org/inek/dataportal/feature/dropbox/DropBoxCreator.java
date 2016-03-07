/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.dropbox;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.SessionTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.dropbox.DropBox;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class DropBoxCreator  {

    @Inject private org.inek.dataportal.facades.DropBoxFacade _dropBoxFacade;
    @Inject private SessionController _sessionController;
    @Inject private SessionTools _sessionTools;
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
        List<SelectItem> ikItems = new ArrayList<>();
        Integer ik = _sessionController.getAccount().getIK();
        if (ik != null) {
            ikItems.add(new SelectItem(ik, ik + " " + _sessionController.getAccount().getCompany()));
        }
        for (AccountAdditionalIK addIk : _sessionController.getAccount().getAdditionalIKs()) {
            ikItems.add(new SelectItem(addIk.getIK(), addIk.getIK() + " " + addIk.getName()));
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

    public String getIkRequired(){
        return _sessionTools.getDropBoxType(_dropboxTypeId).isNeedsIK() ? "true" : "false";
    }
    
    private int createDropBox() {
        DropBox dropBox = new DropBox();
        dropBox.setAccountId(_sessionController.getAccountId());
        dropBox.setDirectory("");
        dropBox.setDescription(_description);
        dropBox.setDropboxType(_sessionTools.getDropBoxType(_dropboxTypeId));
        dropBox.setIK(_ik);
        dropBox = _dropBoxFacade.createDropBox(dropBox);
        return dropBox.getDropBoxId();
    }
}
