package org.inek.dataportal.feature.dropbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.inek.dataportal.controller.AbstractFeatureController;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.DropBox;
import org.inek.dataportal.entities.DropBoxItem;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.DropBoxFacade;
import org.inek.dataportal.helper.*;
import org.inek.dataportal.utils.PropertyKey;
import org.inek.dataportal.utils.PropertyManager;

/**
 *
 * @author muellermi
 */
public class DropBoxController extends AbstractFeatureController {

    private DropBox _currentDropBox;

    public DropBoxController(SessionController sessionController) {
        super(sessionController);
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public DropBox getCurrentDropBox() {
        return _currentDropBox;
    }

    public void setCurrentDropBox(DropBox currentDropBox) {
        this._currentDropBox = currentDropBox;
    }

    public String getUploadRoot() {
        return PropertyManager.INSTANCE.getProperty(PropertyKey.FolderRoot);
    }
    // </editor-fold>

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(getMsg().getString("lblDropBoxSystem"), Pages.DropBoxSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartDropBox.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.DROPBOX;
    }

    public File getUploadDir() throws IllegalStateException {
        return getUploadDir(_currentDropBox);
    }
    
    public File getUploadDir(DropBox dropBox) throws IllegalStateException {
        if (dropBox == null) {
            throw new IllegalStateException("no valid dropBox available");
        }
        File uploadRoot = new File(getUploadRoot(), PropertyManager.INSTANCE.getProperty(PropertyKey.FolderUpload));
        File path = new File(uploadRoot, dropBox.getDirectory());
        return path;
    }

    public boolean containsFiles(DropBox dropBox) {
        File dir = getUploadDir(dropBox);
        return dir.exists() && dir.listFiles().length > 0;
    }

    /**
     * gets a list of files uploaded to the current DropBox
     *
     * @param dropBox
     * @return
     */
    public List<DropBoxItem> getUploadedFiles(DropBox dropBox) {
        if (dropBox == null){
            _logger.log(Level.WARNING, "MissingDropBox");
            return new ArrayList<>();
        }
        if (!dropBox.isComplete()) {
            files2items(dropBox);
        }
        return dropBox.getItems();
    }

    public void sealDropBox(DropBoxFacade _dropBoxFacade, DropBox dropBox) throws ProcessingException {
        files2items(dropBox);
        dropBox.setComplete(true);
        addEmailInfo(dropBox);
        moveFiles2Target(dropBox);
        _dropBoxFacade.updateDropBox(dropBox);
    }

    private void moveFiles2Target(DropBox dropBox) throws ProcessingException {
        File sourceDir = getUploadDir(dropBox);
        File workingFile = new File(sourceDir.getAbsolutePath() + ".zip");
        new Compress().compressFiles(sourceDir.listFiles(), workingFile);
        File targetParent = new File(getUploadRoot(), dropBox.getDropboxType().getFolder());
        targetParent.mkdirs();
        File target;
        do {
            Date ts = Calendar.getInstance().getTime();
            target = new File(targetParent, "Box" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ts) + ".zip");
        } while (target.exists());
        workingFile.renameTo(target);
        if (!deleteDir(sourceDir)) {
            throw new ProcessingException("Could not delete " + sourceDir.getAbsolutePath());
        }
    }

    private void files2items(DropBox dropBox) throws IllegalStateException {
        List<DropBoxItem> files = dropBox.getItems();
        File dir = getUploadDir(dropBox);
        if (dir.exists()) {
            files.clear();
            for (File f : dir.listFiles()) {
                DropBoxItem item = new DropBoxItem();
                item.setName(f.getName());
                item.setSize(f.length());
                files.add(item);
            }
        }
    }
    
    private void addEmailInfo(DropBox dropBox) {
        PrintWriter pw = null;
        try {
            File dir = getUploadDir(dropBox);
            Date ts = Calendar.getInstance().getTime();
            String fileName = "EMailInfo" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";
            File file = new File(dir, fileName);
            pw = new PrintWriter(new FileOutputStream(file));
            pw.println("From=" + getAccount().getEmail());
            pw.println("Accept=Dropbox");
            pw.println("Received=" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ts));
            pw.println("Subject=Dropbox_" + dropBox.getIK());
            pw.flush();
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException(ex);
        } finally {
            pw.close();
        }
    }

    public void deleteFileOfDropBox(DropBox dropBox, String fileName) {
        File file = new File(getUploadDir(dropBox), fileName);
        file.delete();
    }


    private boolean deleteDir(File dir) {
        boolean isDeleted = true;
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                isDeleted &= deleteDir(file);
            } else {
                isDeleted &= file.delete();
            }
        }
        isDeleted &= dir.delete();
        return isDeleted;
    }

}
