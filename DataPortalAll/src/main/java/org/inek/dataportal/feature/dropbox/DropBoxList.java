/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.dropbox;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.entities.dropbox.DropBox;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.facades.DropBoxFacade;
import org.inek.dataportal.common.helper.Utils;

/**
 * Handler for list views and other non-editing views of DropBox
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class DropBoxList {

    @Inject private DropBoxFacade _dropBoxFacade;
    @Inject private SessionController _sessionController;

    private DropBoxController getDropBoxController() {
        return (DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX);
    }

    public List<DropBox> getDropBoxes() {
        return _dropBoxFacade.findAll(_sessionController.getAccountId(), false);
    }

    public List<DropBox> getSealedDropBoxes() {
        return _dropBoxFacade.findAll(_sessionController.getAccountId(), true);
    }

    public boolean containsFiles(DropBox dropBox) {
        return getDropBoxController().containsFiles(dropBox);
    }

    public String editDropBox(int dropBoxId) {
        Utils.getFlash().put("dbId", dropBoxId);
        return Pages.DropBoxUpload.URL();
    }

    public String validUntil(DropBox dropbox) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(dropbox.getValidUntil());
    }
    
}
