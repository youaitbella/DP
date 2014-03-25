/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.dropbox;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.DropBox;
import org.inek.dataportal.entities.DropBoxItem;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.DropBoxFacade;
import org.inek.dataportal.helper.ProcessingException;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class EditDropBox implements Serializable {

    private static final Logger _logger = Logger.getLogger("EditDropBox");
    @Inject private SessionController _sessionController;
    @Inject private DropBoxFacade _dropBoxFacade;
    @Inject private Conversation _conversation;
    private DropBox _dropBox;

    public EditDropBox    (){
        //System.out.println("ctor EditDropBox");
    }

    @PostConstruct
    private void init() {
        _logger.log(Level.WARNING, "Init EditDropBox");
        _sessionController.beginConversation(_conversation);
        Object dbId = Utils.getFlash().get("dbId");
        _dropBox = loadDropBox(dbId);

    }

    @PreDestroy
    private void destroy(){
        //_logger.log(Level.WARNING, "Destroy EditDropBox");
    }
    
    private DropBox loadDropBox(Object dbId) {
        DropBoxController dropBoxController = (DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX);
        try {
            int id = Integer.parseInt("" + dbId);
            DropBox dropBox = _dropBoxFacade.find(id);
            if (_sessionController.isMyAccount(dropBox.getAccountId())) {
                dropBoxController.setCurrentDropBox(dropBox);
                return dropBox;
            }
            _logger.log(Level.WARNING, "No DropBox found for id {0}", dbId);
        } catch (NumberFormatException ex) {
            _logger.log(Level.WARNING, ex.getMessage());
        }
        dropBoxController.setCurrentDropBox(null);
        return null;
    }

    public DropBox getDropBox() {
        return _dropBox;
    }

    public List<DropBoxItem> getUploadedFiles() {
        DropBoxController dropBoxController = (DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX);
        return dropBoxController.getUploadedFiles(_dropBox);
    }

    public String deleteFile(String fileName) {
        DropBoxController dropBoxController = (DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX);
        dropBoxController.deleteFileOfDropBox(_dropBox, fileName);
        return ""; //Pages.DropBoxUpload.URL();
    }

    public String formatSize(long size) {
        int fact = 1024 * 1024 * 1024;
        if (size > fact) {
            return makeDecimal(size, fact) + " GB";
        }
        fact = 1024 * 1024;
        if (size > fact) {
            return makeDecimal(size, fact) + " MB";
        }
        fact = 1024;
        return makeDecimal(size, fact) + " KB";
    }

    private String makeDecimal(long size, long fact) {
        long fract = Math.round(size * 10 / fact);
        long p1 = fract / 10;
        long p2 = fract % 10;
        return p1 + "," + p2;
    }

    public String sealDropBox() {
        DropBoxController dropBoxController = (DropBoxController) _sessionController.getFeatureController(Feature.DROPBOX);
        try {
            _sessionController.endConversation(_conversation);
            dropBoxController.sealDropBox(_dropBoxFacade, _dropBox);
            return Pages.DropBoxUpload.URL();
        } catch (ProcessingException e) {
            _logger.log(Level.SEVERE, null, e);
            return Pages.Error.URL();
        }
    }

    public String refresh(){
        return "";
    }
       
    
}
