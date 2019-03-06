package org.inek.dataportal.base.feature.dropbox;

import org.inek.dataportal.base.feature.dropbox.helper.DropBoxFileHelper;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.helper.Topics;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.inek.dataportal.common.controller.AbstractFeatureController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.base.feature.dropbox.entities.DropBox;
import org.inek.dataportal.base.feature.dropbox.entities.DropBoxItem;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.base.feature.dropbox.facade.DropBoxFacade;

/**
 *
 * @author muellermi
 */
public class DropBoxController extends AbstractFeatureController {

    private DropBox _currentDropBox;

    private static final Logger LOGGER = Logger.getLogger(DropBoxController.class.getName());

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
        return getSessionController().getApplicationTools().readConfig(ConfigKey.FolderRoot);
    }
    // </editor-fold>

    @Override
    protected void addTopics(Topics topics) {
        topics.addTopic(Utils.getMessage("lblDropBoxSystem"), Pages.DropBoxSummary.URL());
    }

    @Override
    public String getMainPart() {
        return Pages.PartDropBox.URL();
    }

    @Override
    public Feature getFeature() {
        return Feature.DROPBOX;
    }

    public File getUploadDir(DropBox dropBox) throws IllegalStateException {
        if (dropBox == null) {
            throw new IllegalStateException("no valid dropBox available");
        }

        File uploadRoot = new File(getUploadRoot(), getSessionController().getApplicationTools().readConfig(ConfigKey.FolderUpload));
        File path = new File(uploadRoot, dropBox.getDirectory());
        return path;
    }


    public void sealDropBox(DropBoxFacade dropBoxFacade, DropBox dropBox, Account account) {
        try {
            DropBoxFileHelper.sealDropBox(dropBoxFacade, dropBox, getUploadRoot(),account);
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during save DropBox: " + ex.getMessage());
        }
    }
}
