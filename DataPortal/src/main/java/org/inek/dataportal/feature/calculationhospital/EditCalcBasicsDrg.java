/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import org.inek.dataportal.entities.calc.KGLListKstTop;
import org.inek.dataportal.entities.calc.KGLListServiceProvision;
import org.inek.dataportal.entities.calc.KGLListServiceProvisionType;
import org.inek.dataportal.entities.calc.KGLOpAn;
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
        String id = "" + params.get("id");
        if (id.equals("new")) {
            _calcBasics = newCalcBasicsDrg();
        } else if (Utils.isInteger(id)) {
            _calcBasics = loadCalcBasicsDrg(id);
            retrievePriorData(_calcBasics);
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
            return;
        }
        ensureTopList();
    }

    public void retrievePriorData(DrgCalcBasics calcBasics) {
        _priorCalcBasics = _calcFacade.retrievePriorCalcBasics(calcBasics);
    }

    public void ikChanged() {
        retrievePriorData(_calcBasics);
        preloadData(_calcBasics);
    }

    private void preloadData(DrgCalcBasics calcBasics) {
        KGLOpAn opAn = new KGLOpAn(calcBasics.getId(), _priorCalcBasics.getOpAn());
        calcBasics.setOpAn(opAn);

        // ServiceProvision
        preloadServiceProvision(calcBasics);

        // cardiology
        calcBasics.setCardiology(_priorCalcBasics.isCardiology());
        calcBasics.setCardiologyCaseCnt(_priorCalcBasics.getCardiologyCaseCnt());
        calcBasics.setCardiologyRoomCnt(_priorCalcBasics.getCardiologyRoomCnt());

        // endoscopy
        calcBasics.setEndoscopy(_priorCalcBasics.isEndoscopy());
        calcBasics.setEndoscopyCaseCnt(_priorCalcBasics.getEndoscopyCaseCnt());
        calcBasics.setEndoscopyRoomCnt(_priorCalcBasics.getEndoscopyRoomCnt());

        // neonat
        calcBasics.setNeonatLvl(_priorCalcBasics.getNeonatLvl());
        int headerId = _calcFacade.retrieveHeaderTexts(calcBasics.getDataYear(), 20, 0).get(0).getId();
        _priorCalcBasics.getNeonateData().stream().filter(old -> old.getContentText().getHeaderTextId() == headerId).forEach(old -> {
            Optional<DrgNeonatData> optDat = calcBasics.getNeonateData().stream().filter(nd -> nd.getContentTextId() == old.getContentTextId()).findFirst();
            if (optDat.isPresent()) {
                optDat.get().setData(old.getData());
            }
        });

    }

    private DrgCalcBasics loadCalcBasicsDrg(String idObject) {
        try {
            int id = Integer.parseInt(idObject);
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
        DrgCalcBasics calcBasics = new DrgCalcBasics();
        calcBasics.setAccountId(account.getId());
        calcBasics.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        if (getIks().size() == 1) {
            calcBasics.setIk((int) getIks().get(0).getValue());
        }
        ensureNeonateData(calcBasics);
        retrievePriorData(calcBasics);
        preloadData(calcBasics);
        return calcBasics;
    }

    private void ensureNeonateData(DrgCalcBasics calcBasics) {
        if (!calcBasics.getNeonateData().isEmpty()) {
            return;
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

    private void preloadServiceProvision(DrgCalcBasics calcBasics) {
        calcBasics.getServiceProvisions().clear();
        List<KGLListServiceProvisionType> provisionTypes = _calcFacade.retrieveServiceProvisionTypes(calcBasics.getDataYear(), true);

        int seq = 0;

        // always populate mandatory entries
        for (KGLListServiceProvisionType provisionType : provisionTypes) {
            KGLListServiceProvision data = new KGLListServiceProvision();
            data.setBaseInformationId(calcBasics.getId());
            data.setServiceProvisionType(provisionType);
            data.setServiceProvisionTypeID(provisionType.getId());
            data.setSequence(++seq);
            calcBasics.getServiceProvisions().add(data);
        }

        // get prior values and additional entries
        for (KGLListServiceProvision prior : _priorCalcBasics.getServiceProvisions()) {
            Optional<KGLListServiceProvision> currentOpt = calcBasics.getServiceProvisions().stream().filter(sp -> sp.getServiceProvisionTypeID() == prior.getServiceProvisionTypeID()).findAny();
            if (currentOpt.isPresent()) {
                KGLListServiceProvision current = currentOpt.get();
                current.setProvidedTypeID(prior.getProvidedTypeID());
            } else {
                KGLListServiceProvision data = new KGLListServiceProvision();
                data.setBaseInformationId(calcBasics.getId());
                data.setServiceProvisionType(prior.getServiceProvisionType());
                data.setServiceProvisionTypeID(prior.getServiceProvisionTypeID());
                data.setProvidedTypeID(prior.getProvidedTypeID());
                data.setSequence(++seq);
                calcBasics.getServiceProvisions().add(data);
            }
        }
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

    public List<String> getDelimitationFactsSubTitles() {
        List<String> tmp = new ArrayList<>();
        tmp.add("Personalkosten");
        tmp.add("Sachkosten");
        tmp.add("Infrastrukturkosten");
        return tmp;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public DrgCalcBasics getCalcBasics() {
        return _calcBasics;
    }

    public void setCalcBasics(DrgCalcBasics calcBasics) {
        _calcBasics = calcBasics;
    }

    public DrgCalcBasics getPriorCalcBasics() {
        return _priorCalcBasics;
    }

    public void setPriorCalcBasics(DrgCalcBasics priorCalcBasics) {
        this._priorCalcBasics = priorCalcBasics;
    }
    // </editor-fold>

    @Override
    protected void addTopics() {
        addTopic("lblFrontPage", Pages.CalcDrgBasics.URL());
        addTopic("lblBasicExplanation", Pages.CalcDrgBasicExplanation.URL());
        addTopic("lblCalcExternalServiceProvision", Pages.CalcDrgExternalServiceProvision.URL());
        //addTopic("lblCalcOperation", Pages.CalcDrgOperation.URL());
        //addTopic("lblCalcAnaestesia", Pages.CalcDrgAnaestesia.URL());
        addTopic("lblCalcOpAn", Pages.CalcDrgOperation.URL());
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

    public void exportTest() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // this produces read errors. enable once the data access is complete
        mapper.writeValue(new File("d:/transfer/kgl" + _calcBasics.getId() + ".json"), _calcBasics);
        // mapper.writeValue(new File("d:/transfer/kgl" + _calcBasics.getId() +  ".json"), _calcBasics.getOpAn());

        // later on, we need to embedd the json string into an info file
        //String json = mapper.writeValueAsString(_calcBasics);
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

    // todo: move into entity
    private void ensureTopList() {
        if (_calcBasics.getKstTop() == null) {
            _calcBasics.setKstTop(new Vector<>());
        }
        ensureTopListCostCenter(4, 3);
        ensureTopListCostCenter(6, 5);
    }

    private void ensureTopListCostCenter(int costCenterId, int count) {
        if (_calcBasics.getKstTop().stream().filter(e -> e.getKtCostCenterID() == costCenterId).count() == 0) {
            for (int i = 0; i < count; i++) {
                KGLListKstTop item = new KGLListKstTop();
                item.setBaseInformationID(_calcBasics.getId());
                item.setKtCostCenterID(costCenterId);
                _calcBasics.getKstTop().add(item);
            }
        }
    }

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
            _iks = items;
        }
        return _iks;
    }

    public String getHospitalInfo() {
        Customer c = _customerFacade.getCustomerByIK(_calcBasics.getIk());
        if (c == null || c.getName() == null) {
            return "";
        }
        return c.getName() + ", " + c.getTown();
    }
    // </editor-fold>

    public String priorProvisionAmount(KGLListServiceProvision current) {
        Optional<KGLListServiceProvision> prior = _priorCalcBasics.getServiceProvisions().stream().filter(p -> p.getServiceProvisionTypeID() == current.getServiceProvisionTypeID()).findAny();
        if (prior.isPresent()) {
            return "" + prior.get().getAmount();
        }
        return "";
    }

    // <editor-fold defaultstate="collapsed" desc="Tab Neonatology">
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
        return _calcBasics.getNeonateData()
                .stream()
                .filter(d -> d.getContentText().getHeaderTextId() == headerId)
                .sorted((x, y) -> x.getContentText().getSequence() - y.getContentText().getSequence())
                .collect(Collectors.toList());
    }

    public int priorData(int textId) {
        int priorValue = _priorCalcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(0);
        return priorValue;
    }

    public String diffData(int textId) {
        DrgNeonatData data = _calcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).findFirst().get();
        int priorValue = _priorCalcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(0);
        if (data.getContentText().isDiffAsPercent()) {
            return calcPercentualDiff(priorValue, data.getData());
        }
        return "" + (data.getData() - priorValue);
    }
    // </editor-fold>    

    public String calcPercentualDiff(int priorValue, int currentValue) {
        return Math.round(1000d * (currentValue - priorValue) / priorValue) / 10d + "%";
    }

}
