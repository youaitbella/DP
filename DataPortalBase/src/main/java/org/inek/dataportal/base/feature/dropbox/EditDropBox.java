/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.feature.dropbox;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.helper.Const;
import org.inek.dataportal.base.feature.dropbox.entities.DropBox;
import org.inek.dataportal.base.feature.dropbox.entities.DropBoxItem;
import org.inek.dataportal.base.feature.dropbox.facade.DropBoxFacade;
import org.inek.dataportal.base.feature.dropbox.helper.DropBoxFileHelper;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditDropBox implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("EditDropBox");
    @Inject
    private SessionController _sessionController;
    @Inject
    private DropBoxFacade _dropBoxFacade;
    @Inject
    private ApplicationTools _applicationTools;

    private DropBox _dropBox;
    @Inject
    private Mailer _mailer;
    @Inject
    private AccessManager _accessManager;

    @PostConstruct
    private void init() {
        Object dbId = Utils.getFlash().get("dbId");
        _dropBox = loadDropBox(dbId);
        _dropBox.setUploadDir(getUploadDir());
    }

    private DropBox loadDropBox(Object dbId) {
        try {
            int id = Integer.parseInt("" + dbId);
            DropBox dropBox = _dropBoxFacade.findById(id);
            if (_accessManager.isAccessAllowed(Feature.DROPBOX, WorkflowStatus.New, dropBox.getAccountId(), dropBox.getIK())) {
                return dropBox;
            }
            DialogController.showAccessDeniedDialog();
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
        return null;
    }

    public DropBox getDropBox() {
        return _dropBox;
    }

    public List<DropBoxItem> getUploadedFiles() {
        return DropBoxFileHelper.getUploadedFiles(_dropBox);
    }

    private void addFileToDropbox(UploadedFile file) {
        File uploadDir = _dropBox.getUploadDir();
        uploadDir.mkdirs();
        try (FileOutputStream fos = new FileOutputStream(new File(uploadDir, file.getFileName()))) {
            StreamHelper.copyStream(file.getInputstream(), fos);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error saving File for DropBox: " + _dropBox.getDropBoxId() + " Message: " + ex.getMessage() + " " + ex);
        }
    }

    private File getUploadDir() {
        File uploadRoot = new File(_applicationTools.readConfig(ConfigKey.FolderRoot), _applicationTools.readConfig(ConfigKey.FolderUpload));
        return new File(uploadRoot, _dropBox.getDirectory());
    }

    public void handleFileUpload(FileUploadEvent event) {
        LOGGER.log(Level.INFO, "File for dropbox " + _dropBox.getDropBoxId() + " uploaded: " + event.getFile().getFileName());
        addFileToDropbox(event.getFile());
    }

    public void deleteFile(String fileName) {
        DropBoxFileHelper.deleteFileFromDropBox(_dropBox, fileName);
    }

    public String formatSize(long size) {
        if (size > Const.BINARY_GIGA) {
            return makeDecimal(size, Const.BINARY_GIGA) + " GB";
        }
        if (size > Const.BINARY_MEGA) {
            return makeDecimal(size, Const.BINARY_MEGA) + " MB";
        }
        return makeDecimal(size, Const.BINARY_KILO) + " KB";
    }

    private String makeDecimal(long size, long fact) {
        long fract = Math.round(size * 10 / fact);
        long p1 = fract / 10;
        long p2 = fract % 10;
        return p1 + "," + p2;
    }

    public void sealDropBox() {
        try {
            if (!dropBoxIsComplete()) {
                return;
            }
            File target = DropBoxFileHelper.sealDropBox(_dropBoxFacade, _dropBox,
                    _applicationTools.readConfig(ConfigKey.FolderRoot), _sessionController.getAccount());
            if (!_dropBox.getDropboxType().isNeedsIK()) {
                notifyInek(target);
            }
            DialogController.showSendDialog();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error on save DropBox " + _dropBox.getDropBoxId(), e);
        }
    }

    private boolean dropBoxIsComplete() {
        if (DropBoxFileHelper.getUploadedFiles(_dropBox).size() == 0) {
            DialogController.showInfoDialog("Keine Dateien vorhanden", "Bitte laden Sie mindestens eine Datei in die " +
                    "DropBox um diese an das InEK zu senden.");
            return false;
        }
        return true;
    }

    public boolean dropBoxContainsDecryptedFiles() {
        return DropBoxFileHelper.dropBoxContainsDecryptedFiles(_dropBox);
    }

    private void notifyInek(File target) {
        _mailer.sendMail("edv@inek-drg.de", "DropBox", "Neue Dokumente per DropBox geliefert: " + target.getAbsolutePath()
                + "\r\nAbsender: [" + _sessionController.getAccount().getId() + "] " + _sessionController.getAccount().getCompany()
                + "\r\nBeschreibung: " + _dropBox.getDescription());
    }

}
