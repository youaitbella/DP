/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.DrgContentText;
import org.inek.dataportal.entities.calc.DrgDelimitationFact;
import org.inek.dataportal.entities.calc.DrgHeaderText;
import org.inek.dataportal.entities.calc.DrgNeonatData;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CalcFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditCalcBasicsDrg extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditCalcBasicsDrg");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject ApplicationTools _appTools;
    @Inject private CustomerFacade _customerFacade;

    private String _script;
    private DrgCalcBasics _calcBasics;
    private DrgCalcBasics _priorCalcBasics;

    // </editor-fold>
    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Object id = params.get("id");
        if (id == null) {
            return;
        }
        if (id.toString().equals("new")) {
            _calcBasics = newCalcBasicsDrg();
        } else {
            _calcBasics = loadCalcBasicsDrg(id);
        }
        updateIk();
    }

    public void updateIk() {
        if (_calcBasics != null) {
            Customer c = _customerFacade.getCustomerByIK(_calcBasics.getIk());
            _hospitalInfo = c.getName() + ", " + c.getTown();
            _priorCalcBasics = _calcFacade.retrievePriorCalcBasics(_calcBasics);
        }
    }
    
    private DrgCalcBasics loadCalcBasicsDrg(Object idObject) {
        try {
            int id = Integer.parseInt("" + idObject);
            DrgCalcBasics statement = _calcFacade.findCalcBasicsDrg(id);
            if (_cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, statement.getStatus(), statement.getAccountId())) {
                return statement;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newCalcBasicsDrg();
    }

    private DrgCalcBasics newCalcBasicsDrg() {
        Account account = _sessionController.getAccount();
        DrgCalcBasics calcBasic = new DrgCalcBasics();
        calcBasic.setAccountId(account.getId());
        calcBasic.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return calcBasic;
    }

    public List<DrgDelimitationFact> getDelimitationFacts() {
        if (_calcBasics.getDelimitationFacts() == null || _calcBasics.getDelimitationFacts().isEmpty()) {
            for (DrgContentText ct : _calcFacade.retrieveContentTexts(1, _calcBasics.getDataYear())) {
                DrgDelimitationFact df = new DrgDelimitationFact();
                df.setContentTextId(ct.getId());
                df.setLabel(ct.getText());
                df.setBaseInformationId(_calcBasics.getId());
                _calcBasics.getDelimitationFacts().add(df);
            }
        }
        return _calcBasics.getDelimitationFacts();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public DrgCalcBasics getCalcBasics() {
        return _calcBasics;
    }

    public void setCalcBasics(DrgCalcBasics calcBasics) {
        _calcBasics = calcBasics;
    }

    // </editor-fold>
    @Override
    protected void addTopics() {
        addTopic("lblFrontPage", Pages.CalcDrgBasics.URL());
        addTopic("lblBasicExplanation", Pages.CalcDrgBasicExplanation.URL());
        addTopic("lblCalcExternalServiceProvision", Pages.CalcDrgExternalServiceProvision.URL());
        addTopic("lblCalcOperation", Pages.CalcDrgOperation.URL());
        addTopic("lblCalcAnaestesia", Pages.CalcDrgAnaestesia.URL());
        addTopic("lblCalcMaternityRoom", Pages.CalcDrgMaternityRoom.URL());
        addTopic("lblCalcCardiology", Pages.CalcDrgCardiology.URL());
        addTopic("lblCalcEndoscopy", Pages.CalcDrgEndoscopy.URL());
        addTopic("lblCalcRadiology", Pages.CalcDrgRadiology.URL());
        addTopic("lblCalcLaboratory", Pages.CalcDrgLaboratory.URL());
        addTopic("lblCalcDiagnosticScope", Pages.CalcDrgDiagnosticScope.URL());
        addTopic("lblCalcTherapeuticScope", Pages.CalcDrgTherapeuticScope.URL());
        addTopic("lblCalcPatientAdmission", Pages.CalcDrgPatientAdmission.URL());
        addTopic("lblCalcNormalWard", Pages.CalcrgNormalWard.URL());
        addTopic("lblCalcIntensiveCare", Pages.CalcDrgIntensiveCare.URL());
        addTopic("lblCalcStrokeUnit", Pages.CalcDrgStrokeUnit.URL());
        addTopic("lblCalcMedicalInfrastructure", Pages.CalcDrgMedicalInfrastructure.URL());
        addTopic("lblCalcNonMedicalInfrastructure", Pages.CalcDrgNonMedicalInfrastructure.URL());
        addTopic("lblCalcStaffCost", Pages.CalcDrgStaffCost.URL());
        addTopic("lblCalcValvularIntervention", Pages.CalcDrgValvularIntervention.URL());
        addTopic("lblCalcNeonatology", Pages.CalcDrgNeonatology.URL());
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_calcBasics.getAccountId(), false);
    }

    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId(), _calcBasics.getIk());
    }

    public String save() {
        setModifiedInfo();
        _calcBasics = _calcFacade.saveCalcBasicsDrg(_calcBasics);

        if (isValidId(_calcBasics.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _calcBasics.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsCreateEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    /**
     * This function seals a statement od participance if possible. Sealing is
     * possible, if all mandatory fields are fulfilled. After sealing, the
     * statement od participance can not be edited anymore and is available for
     * the InEK.
     *
     * @return
     */
    public String seal() {
        if (!statementIsComplete()) {
            return getActiveTopic().getOutcome();
        }

        _calcBasics.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _calcBasics = _calcFacade.saveCalcBasicsDrg(_calcBasics);

        if (isValidId(_calcBasics.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL") + " " + _calcBasics.getId());
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_calcBasics));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean statementIsComplete() {
        // todo
        return true;
    }

    public String requestApproval() {
        if (!statementIsComplete()) {
            return null;
        }
        _calcBasics.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _calcBasics = _calcFacade.saveCalcBasicsDrg(_calcBasics);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _calcBasics.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _calcBasics = _calcFacade.saveCalcBasicsDrg(_calcBasics);
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Address">
    List<SelectItem> _iks;

    public List<SelectItem> getIks() {
        if (_iks == null) {
            Set<Integer> ids = new HashSet<>();
            ids.add(_sessionController.getAccountId());
            Set<Integer> iks = _calcFacade.obtainIks4NewBasiscs(CalcHospitalFunction.CalculationBasicsDrg, ids, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
            if (_calcBasics != null && _calcBasics.getIk() > 0) {
                iks.add(_calcBasics.getIk());
            }

            List<SelectItem> items = new ArrayList<>();
            for (int ik : iks) {
                items.add(new SelectItem(ik));
            }
            if (_calcBasics != null && _calcBasics.getIk() <= 0) {
                items.add(0, new SelectItem(""));
            }
            _iks = items;
        }
        return _iks;
    }

    String _hospitalInfo = "";

    public String getHospitalInfo() {
        return _hospitalInfo;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Neonatology">
    private void ensureNeonateData(DrgCalcBasics calcBasics) {
        if (calcBasics.getNeonateData() != null && !calcBasics.getNeonateData().isEmpty()) {
            return;
        }
        if (calcBasics.getNeonateData() == null) {
            calcBasics.setNeonateData(new Vector<>());
        }
        List<Integer> headerIds = _calcFacade.retrieveHeaderTexts(calcBasics.getDataYear(), 20, -1)
                .stream()
                .map(ht -> ht.getId())
                .collect(Collectors.toList());
        List<DrgContentText> contentTexts = _calcFacade.retrieveContentTexts(headerIds, calcBasics.getDataYear());
        for (DrgContentText contentText : contentTexts) {
            DrgNeonatData data = new DrgNeonatData();
            data.setContentTextId(contentText.getId());
            data.setContentText(contentText);
            data.setCalcBasicsId(calcBasics.getId());
            calcBasics.getNeonateData().add(data);
        }
    }

    public List<DrgHeaderText> getHeaders() {
        return _calcFacade.retrieveHeaderTexts(_calcBasics.getDataYear(), 20, -1);
    }

    public List<DrgHeaderText> getHeaders(int type) {
        return _calcFacade.retrieveHeaderTexts(_calcBasics.getDataYear(), 20, type);
    }

    public List<DrgContentText> retrieveContentTexts(int headerId) {
        return _calcFacade.retrieveContentTexts(headerId, _calcBasics.getDataYear());
    }

    public List<DrgNeonatData> retrieveNeonatData(int headerId) {
        ensureNeonateData(_calcBasics);
        return _calcBasics.getNeonateData()
                .stream()
                .filter(d -> d.getContentText().getHeaderTextId() == headerId)
                .sorted((x, y) -> x.getContentText().getSequence()- y.getContentText().getSequence())
                .collect(Collectors.toList());
    }

    public int priorData(int textId){
        int priorValue = _priorCalcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(0);
        return priorValue;
    }

    public String diffData(int textId){
        DrgNeonatData data = _calcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).findFirst().get();
        int priorValue = _priorCalcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(0);
        if (data.getContentText().isDiffAsPercent()){
            return Math.round(1000d * (data.getData() - priorValue) / priorValue) / 10d + "%";
        }
        return "" + (data.getData() - priorValue);
    }
    // </editor-fold>    
}
