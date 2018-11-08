/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.feature.dropbox;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.feature.dropbox.entities.DropBox;
import org.inek.dataportal.base.feature.dropbox.facade.DropBoxFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Handler for list views and other non-editing views of DropBox
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class DropBoxSummary {

    @Inject
    private DropBoxFacade _dropBoxFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private ApplicationTools _applicationTools;

    private List<DropBoxListEntry> _dropBoxesSend = new ArrayList<>();
    private List<DropBoxListEntry> _dropBoxesOpen = new ArrayList<>();

    public List<DropBoxListEntry> getDropBoxesOpen() {
        return _dropBoxesOpen;
    }

    public void setDropBoxesOpen(List<DropBoxListEntry> dropBoxesOpen) {
        this._dropBoxesOpen = dropBoxesOpen;
    }

    public List<DropBoxListEntry> getDropBoxesSend() {
        return _dropBoxesSend;
    }

    public void setDropBoxesSend(List<DropBoxListEntry> dropBoxesSend) {
        this._dropBoxesSend = dropBoxesSend;
    }

    @PostConstruct
    public void init() {
        createSendedDropboxList();
        createOpenDropBoxesList();
    }

    private void createListEntrys(List<DropBoxListEntry> listEntrys, List<DropBox> dropBoxes) {
        for (DropBox dp : dropBoxes) {
            listEntrys.add(new DropBoxListEntry(dp));
        }
    }

    private void createOpenDropBoxesList() {
        List<DropBoxListEntry> dropBoxes = new ArrayList<>();

        for (int ik : _accessManager.retrieveAllowedManagedIks(Feature.DROPBOX)) {
            createListEntrys(dropBoxes, _dropBoxFacade.getDropBoxesForIk(ik, false));
        }
        for (int ik : createOwnIkList()) {
            createListEntrys(dropBoxes, _dropBoxFacade.getDropBoxesForIkAndAccount(ik, _sessionController.getAccountId(), false));
        }
        createListEntrys(dropBoxes, _dropBoxFacade.getDropBoxesForIkAndAccount(0, _sessionController.getAccountId(), false));

        setDropBoxesOpen(dropBoxes);
    }

    private void createSendedDropboxList() {
        List<DropBoxListEntry> dropBoxes = new ArrayList<>();

        for (int ik : _accessManager.retrieveAllowedManagedIks(Feature.DROPBOX)) {
            createListEntrys(dropBoxes, _dropBoxFacade.getDropBoxesForIk(ik, true));
        }
        for (int ik : createOwnIkList()) {
            createListEntrys(dropBoxes, _dropBoxFacade.getDropBoxesForIkAndAccount(ik, _sessionController.getAccountId(), true));
        }
        createListEntrys(dropBoxes, _dropBoxFacade.getDropBoxesForIkAndAccount(0, _sessionController.getAccountId(), true));

        setDropBoxesSend(dropBoxes);
    }

    private Set<Integer> createOwnIkList() {
        Set<Integer> iks = _sessionController.getAccount().getFullIkSet();
        iks.removeAll(_accessManager.retrieveAllManagedIks(Feature.DROPBOX));
        return iks;
    }

    public List<DropBox> getSealedDropBoxes() {
        return _dropBoxFacade.findAll(_sessionController.getAccountId(), true);
    }


    public String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
    }

    public String openDropBox() {
        return Pages.DropBoxUpload.URL();
    }

    public class DropBoxListEntry implements Serializable {
        private int _id;
        private int _ik;
        private String _khName;
        private String _description;
        private String _type;
        private Date _sendAt;
        private Date _validUntil;
        private String _createdBy;

        public DropBoxListEntry(DropBox dropBox) {
            setId(dropBox.getDropBoxId());
            setIk(dropBox.getIK());
            setKhName(_applicationTools.retrieveHospitalName(dropBox.getIK()));
            setDescription(dropBox.getDescription());
            setType(dropBox.getDropboxType().getName());
            setSendAt(dropBox.getSealed());
            setValidUntil(dropBox.getValidUntil());
            setCreatedBy(dropBox.getAccount().getFirstName() + " " + dropBox.getAccount().getLastName());
        }

        public int getId() {
            return _id;
        }

        public void setId(int id) {
            this._id = id;
        }

        public int getIk() {
            return _ik;
        }

        public void setIk(int ik) {
            this._ik = ik;
        }

        public String getKhName() {
            return _khName;
        }

        public void setKhName(String khName) {
            this._khName = khName;
        }

        public String getDescription() {
            return _description;
        }

        public void setDescription(String description) {
            this._description = description;
        }

        public String getType() {
            return _type;
        }

        public void setType(String type) {
            this._type = type;
        }

        public Date getSendAt() {
            return _sendAt;
        }

        public void setSendAt(Date sendAt) {
            this._sendAt = sendAt;
        }

        public Date getValidUntil() {
            return _validUntil;
        }

        public void setValidUntil(Date validUntil) {
            this._validUntil = validUntil;
        }

        public String getCreatedBy() {
            return _createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this._createdBy = createdBy;
        }
    }

}
