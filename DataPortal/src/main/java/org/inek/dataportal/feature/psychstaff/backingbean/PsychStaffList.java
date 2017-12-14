/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.backingbean;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;
import org.inek.dataportal.feature.psychstaff.facade.PsychStaffFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class PsychStaffList implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger(PsychStaffList.class.getName());

// todo    @Inject private Facade Facade;
    @Inject private ApplicationTools _appTools;
    // </editor-fold>

    @Inject private PsychStaffFacade _psychFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;

    public List<StaffProof> getOpenPersonals() {
        return _psychFacade.getStaffProofs(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<StaffProof> getProvidedPersonals() {
        return _psychFacade.getStaffProofs(_sessionController.getAccountId(), DataSet.AllSealed);
    }

    private List<StaffProof> _inekStaffProofs;

    public List<StaffProof> getInekListPersonals() {
        if (_inekStaffProofs == null) {
            _inekStaffProofs = _psychFacade.getStaffProofs(_filter);
        }
        return _inekStaffProofs;
    }

    private String _filter = "";

    public String getFilter() {
        return _filter;
    }

    public void setFilter(String filter) {
        _inekStaffProofs = null;
        _filter = filter == null ? "" : filter;
    }

    public boolean isNewAllowed() {
        return _appTools.isEnabled(ConfigKey.IsPsychStaffCreateEnabled);
    }

    public String print(StaffProof request) {
        Utils.getFlash().put("headLine", Utils.getMessage("namePSYCH_STAFF"));
        Utils.getFlash().put("targetPage", Pages.PsychStaffSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(request));
        return Pages.PrintView.URL();
    }

    public String getConfirmMessage(int id) {
        StaffProof proof = _psychFacade.findStaffProof(id);
        String msg = "Meldung für " + proof.getIk() + "\n"
                + (proof.getStatus().getId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }

    public void delete(int id) {
        StaffProof data = _psychFacade.findStaffProof(id);
        if (data == null) {
            return;
        }
        if (data.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            data.setStatus(WorkflowStatus.Retired);
            _psychFacade.saveStaffProof(data);
        } else {
            _psychFacade.delete(data);
        }
    }

    public String edit() {
        return Pages.PsychStaffBaseData.URL();
    }

}
