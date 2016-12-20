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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcBasicsDrg;
import org.inek.dataportal.entities.calc.CalcContentText;
import org.inek.dataportal.entities.calc.CalcDelimitationFact;
import org.inek.dataportal.entities.calc.CalcHeaderText;
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
@SessionScoped
public class EditCalcBasicsDrg extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditCalcBasicsDrg");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject ApplicationTools _appTools;
    @Inject private CustomerFacade _customerFacade;

    private String _script;
    private CalcBasicsDrg _calcBasics;

    // </editor-fold>
    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Object id = params.get("id");
        if (id.toString().equals("new")) {
            _calcBasics = newCalcBasicsDrg();
        } else {
            _calcBasics = loadCalcBasicsDrg(id);
        }

    }

    public Set<Integer> getCalcIks() {
        Set<Integer> ids = new HashSet<>();
        ids.add(_sessionController.getAccountId());
        return _calcFacade.obtainIks4NewBasiscs(CalcHospitalFunction.CalculationBasicsDrg,
                ids,
                _calcBasics.getDataYear());
    }

    private CalcBasicsDrg loadCalcBasicsDrg(Object idObject) {
        try {
            int id = Integer.parseInt("" + idObject);
            CalcBasicsDrg statement = _calcFacade.findCalcBasicsDrg(id);
            if (_cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, statement.getStatus(), statement.getAccountId())) {
                return statement;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newCalcBasicsDrg();
    }

    private CalcBasicsDrg newCalcBasicsDrg() {
        Account account = _sessionController.getAccount();
        CalcBasicsDrg calcBasic = new CalcBasicsDrg();
        calcBasic.setAccountId(account.getId());
        calcBasic.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        return calcBasic;
    }

    public List<CalcDelimitationFact> getDelimitationFacts() {
        if (_calcBasics.getDelimitationFacts() == null || _calcBasics.getDelimitationFacts().isEmpty()) {
            for (CalcContentText ct : _calcFacade.retrieveContentTexts(1, _calcBasics.getDataYear())) {
                CalcDelimitationFact df = new CalcDelimitationFact();
                df.setContentTextId(ct.getId());
                df.setLabel(ct.getText());
                df.setBaseInformationId(_calcBasics.getId());
                _calcBasics.getDelimitationFacts().add(df);
            }
        }
        return _calcBasics.getDelimitationFacts();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public CalcBasicsDrg getCalcBasics() {
        return _calcBasics;
    }

    public void setCalcBasics(CalcBasicsDrg calcBasics) {
        _calcBasics = calcBasics;
    }

    // </editor-fold>
    @Override
    protected void addTopics() {
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
    public List<SelectItem> getIks() {
        Account account = _sessionController.getAccount();
        Set<Integer> iks = _sessionController.getAccount().getAdditionalIKs().stream().map(i -> i.getIK()).collect(Collectors.toSet());
        List<SelectItem> items = new ArrayList<>();
        if (account.getIK() != null) {
            iks.add(account.getIK());
        }
        if (_calcBasics.getIk() > 0) {
            iks.add(_calcBasics.getIk());
        }
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        if (_calcBasics.getIk() <= 0) {
            items.add(0, new SelectItem(""));
        }
        return items;
    }

    String _hospitalInfo = "";

    public String getHospitalInfo() {
        return _hospitalInfo;
    }

    public void changedIk() {
        if (_calcBasics != null) {
            Customer c = _customerFacade.getCustomerByIK(_calcBasics.getIk());
            _hospitalInfo = c.getName() + ", " + c.getTown();
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Neonatology">
    public List<CalcHeaderText> getHeaders() {
        return _calcFacade.retrieveHeaderTexts(_calcBasics.getDataYear(), 20, -1); 
    }
    
    public List<CalcContentText> retrieveContentTexts(int headerId) {
        return _calcFacade.retrieveContentTexts(headerId, _calcBasics.getDataYear()); 
    }
    
    // </editor-fold>    

}
