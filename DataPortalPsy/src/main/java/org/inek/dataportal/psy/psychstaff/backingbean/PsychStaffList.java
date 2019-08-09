package org.inek.dataportal.psy.psychstaff.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.helper.FeatureMessageHandler;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.facade.PsychStaffFacade;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class PsychStaffList implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger(PsychStaffList.class.getName());
    private static final long serialVersionUID = 1L;

// todo    @Inject private Facade Facade;
    @Inject private ApplicationTools _appTools;
    // </editor-fold>

    @Inject private PsychStaffFacade _psychFacade;
    @Inject private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;

    private List<StaffProof> _openPersonals;

    public List<StaffProof> getOpenPersonals() {
        if (_openPersonals == null) {
            _openPersonals = obtainStaffProofs(DataSet.AllOpen);
        }
        return _openPersonals;
    }

    private List<StaffProof> _providedPersonals;

    public List<StaffProof> getProvidedPersonals() {
        if (_providedPersonals == null) {
            _providedPersonals = obtainStaffProofs(DataSet.AllSealed);
        }
        return _providedPersonals;
    }

    private List<StaffProof> obtainStaffProofs(DataSet dataSet) {
        Set<Integer> allowedIks = _accessManager.retrieveAllowedManagedIks(Feature.PSYCH_STAFF);
        Set<Integer> deniedIks = _accessManager.retrieveDeniedManagedIks(Feature.PSYCH_STAFF);
        return _psychFacade.getStaffProofs(_sessionController.getAccountId(), dataSet, allowedIks, deniedIks);
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
        Utils.getFlash().put("headLine", FeatureMessageHandler.getMessage("namePSYCH_STAFF"));
        Utils.getFlash().put("targetPage", Pages.PsychStaffSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(request));
        return Pages.PrintView.URL();
    }

    public String getConfirmMessage(int id) {
        StaffProof proof = _psychFacade.findStaffProof(id);
        if (proof == null) {
            // this method might get called AFTER data is deleted.
            // in such a case prrof is null
            return "";
        }
        String msg = "Meldung für " + proof.getIk() + "\n"
                + (proof.getStatus().getId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.
                getMessage("msgConfirmRetire"));
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
