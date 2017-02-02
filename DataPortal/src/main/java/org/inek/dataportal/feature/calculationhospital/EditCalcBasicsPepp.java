/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
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
import org.inek.dataportal.entities.calc.DrgNeonatData;
import org.inek.dataportal.entities.calc.KGLListLocation;
import org.inek.dataportal.entities.calc.KGLPersonalAccounting;
import org.inek.dataportal.entities.calc.KGPListDelimitationFact;
import org.inek.dataportal.entities.calc.KGPListServiceProvision;
import org.inek.dataportal.entities.calc.KGPListServiceProvisionType;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.entities.common.CostType;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CalcFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.common.CostTypeFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditCalcBasicsPepp extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditCalcBasicsPepp");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject ApplicationTools _appTools;
    @Inject private CustomerFacade _customerFacade;
    @Inject private CostTypeFacade _costTypeFacade;

    private String _script;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    private PeppCalcBasics _priorCalcBasics;
    
    public PeppCalcBasics getCalcBasics() {
        return _calcBasics;
    }

    public void setCalcBasics(PeppCalcBasics calcBasics) {
        _calcBasics = calcBasics;
    }

    private PeppCalcBasics _calcBasics;
    
    public PeppCalcBasics getPriorCalcBasics() {
        return _priorCalcBasics;
    }

    public void setPriorCalcBasics(PeppCalcBasics priorCalcBasics) {
        this._priorCalcBasics = priorCalcBasics;
    }
    // </editor-fold>

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if (id.equals("new")) {
            _calcBasics = newCalcBasicsPepp();
        } else if (Utils.isInteger(id)) {
            _calcBasics = loadCalcBasicsPepp(id);
            retrievePriorData(_calcBasics);
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
            return;
        }
    }

    public void retrievePriorData(PeppCalcBasics calcBasics) {
        _priorCalcBasics = _calcFacade.retrievePriorCalcBasics(calcBasics);
    }

    public void ikChanged() {
        retrievePriorData(_calcBasics);
        preloadData(_calcBasics);
    }

    private void preloadData(PeppCalcBasics calcBasics) {

        // Locations
        calcBasics.setLocationCnt(_priorCalcBasics.getLocationCnt());
        calcBasics.setDifLocationSupply(_priorCalcBasics.isDifLocationSupply());

        calcBasics.setOtherMethodNonMedInfra(_priorCalcBasics.getOtherMethodNonMedInfra());
        calcBasics.setApproximationMethodNonMedInfra(_priorCalcBasics.isApproximationMethodNonMedInfra());
        calcBasics.setStepladderMethodNonMedInfra(_priorCalcBasics.isStepladderMethodNonMedInfra());
        calcBasics.setExtensionMethodNonMedInfra(_priorCalcBasics.isExtensionMethodNonMedInfra());

        // MedicalInfrastructure
        calcBasics.setOtherMethodMedInfra(_priorCalcBasics.getOtherMethodMedInfra());
        calcBasics.setApproximationMethodMedInfra(_priorCalcBasics.isApproximationMethodMedInfra());
        calcBasics.setStepladderMethodMedInfra(_priorCalcBasics.isStepladderMethodMedInfra());
        calcBasics.setExtensionMethodMedInfra(_priorCalcBasics.isExtensionMethodMedInfra());
        
        preloadServiceProvision(calcBasics);
        
    }

    private PeppCalcBasics loadCalcBasicsPepp(String idObject) {
        try {
            int id = Integer.parseInt(idObject);
            PeppCalcBasics statement = _calcFacade.findCalcBasicsPepp(id);
            if (_cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, statement.getStatus(), statement.getAccountId())) {
                return statement;
            }
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newCalcBasicsPepp();
    }

    private PeppCalcBasics newCalcBasicsPepp() {
        Account account = _sessionController.getAccount();
        PeppCalcBasics calcBasics = new PeppCalcBasics();
        calcBasics.setAccountId(account.getId());
        calcBasics.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
//        calcBasics.getMedInfras().add(new KGLListMedInfra(-1, 170, "", "", "", 0, calcBasics.getId()));
//        calcBasics.getMedInfras().add(new KGLListMedInfra(-1, 180, "", "", "", 0, calcBasics.getId()));

        if (getIks().size() == 1) {
            calcBasics.setIk((int) getIks().get(0).getValue());
        }
//        ensureNeonateData(calcBasics);
        retrievePriorData(calcBasics);
        preloadData(calcBasics);
        return calcBasics;
    }

    private void ensurePersonalAccountingData(DrgCalcBasics calcBasics) {
        if (calcBasics.getPersonalAccountings().size() == 3) {
            return;
        }

        calcBasics.getPersonalAccountings().add(new KGLPersonalAccounting(110, 0));
        calcBasics.getPersonalAccountings().add(new KGLPersonalAccounting(120, 0));
        calcBasics.getPersonalAccountings().add(new KGLPersonalAccounting(130, 0));
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

    private void preloadServiceProvision(PeppCalcBasics calcBasics) {
        calcBasics.getServiceProvisions().clear();
        List<KGPListServiceProvisionType> provisionTypes = _calcFacade.retrieveServiceProvisionTypesPepp(calcBasics.getDataYear(), true);

        int seq = 0;

        // always populate mandatory entries
        for (KGPListServiceProvisionType provisionType : provisionTypes) {
            KGPListServiceProvision data = new KGPListServiceProvision();
            data.setBaseInformationId(calcBasics.getId());
            data.setServiceProvisionType(provisionType);
            data.setServiceProvisionTypeId(provisionType.getId());
            //data.setSequence(++seq);
            calcBasics.getServiceProvisions().add(data);
        }

        // get prior values and additional entries
        for (KGPListServiceProvision prior : _priorCalcBasics.getServiceProvisions()) {
            Optional<KGPListServiceProvision> currentOpt = calcBasics.getServiceProvisions().stream().filter(sp -> sp.getServiceProvisionTypeId()== prior.getServiceProvisionTypeId()).findAny();
            if (currentOpt.isPresent()) {
                KGPListServiceProvision current = currentOpt.get();
                current.setProvidedTypeId(prior.getProvidedTypeId());
            } else {
                KGPListServiceProvision data = new KGPListServiceProvision();
                data.setBaseInformationId(calcBasics.getId());
                data.setServiceProvisionType(prior.getServiceProvisionType());
                data.setServiceProvisionTypeId(prior.getServiceProvisionTypeId());
                data.setProvidedTypeId(prior.getProvidedTypeId());
                //data.setSequence(++seq);
                calcBasics.getServiceProvisions().add(data);
            }
        }
        
    }


    public List<String> getDelimitationFactsSubTitles() {
        List<String> tmp = new ArrayList<>();
        tmp.add("Personalkosten");
        tmp.add("Sachkosten");
        tmp.add("Infrastrukturkosten");
        return tmp;
    }

    


    @Override
    protected void addTopics() {
        addTopic("lblFrontPage", Pages.CalcPeppBasics.URL());
        addTopic("lblBasicExplanation", Pages.CalcPeppBasicExplanation.URL());
        addTopic("lblCalcExternalServiceProvision", Pages.CalcPeppExternalServiceProvision.URL());
        addTopic("lblCalcTherapyScope", Pages.CalcPeppTherapyScope.URL());
        addTopic("lblCalcRadiology", Pages.CalcPeppRadiology.URL());
        addTopic("lblCalcLaboratory", Pages.CalcPeppLaboratory.URL());
        addTopic("lblCalcDiagnosticScope", Pages.CalcPeppDiagnosticScope.URL());
        addTopic("lblCalcTherapeuticScope", Pages.CalcPeppTherapeuticScope.URL());
        addTopic("lblCalcPatientAdmission", Pages.CalcPeppPatientAdmission.URL());
        addTopic("lblCalcStation", Pages.CalcPeppStation.URL());
        addTopic("lblCalcMedicalInfrastructure", Pages.CalcPeppMedicalInfrastructure.URL());
        addTopic("lblCalcNonMedicalInfrastructure", Pages.CalcPeppNonMedicalInfrastructure.URL());
        addTopic("lblCalcStaffCost", Pages.CalcPeppStaffCost.URL());

//        addTopic("lblCalcExternalServiceProvision", Pages.CalcDrgExternalServiceProvision.URL());
//        //addTopic("lblCalcOperation", Pages.CalcDrgOperation.URL());
//        //addTopic("lblCalcAnaestesia", Pages.CalcDrgAnaestesia.URL());
//        addTopic("lblCalcOpAn", Pages.CalcDrgOperation.URL());
//        addTopic("lblCalcMaternityRoom", Pages.CalcDrgMaternityRoom.URL());
//        addTopic("lblCalcCardiology", Pages.CalcDrgCardiology.URL());
//        addTopic("lblCalcEndoscopy", Pages.CalcDrgEndoscopy.URL());
//        addTopic("lblCalcRadiology", Pages.CalcDrgRadiology.URL());
//        addTopic("lblCalcLaboratory", Pages.CalcDrgLaboratory.URL());
//        addTopic("lblCalcDiagnosticScope", Pages.CalcDrgDiagnosticScope.URL());
//        addTopic("lblCalcTherapeuticScope", Pages.CalcDrgTherapeuticScope.URL());
//        addTopic("lblCalcPatientAdmission", Pages.CalcDrgPatientAdmission.URL());
//        addTopic("lblCalcNormalWard", Pages.CalcrgNormalWard.URL());
//        addTopic("lblCalcIntensiveCare", Pages.CalcDrgIntensiveCare.URL());
//        addTopic("lblCalcStrokeUnit", Pages.CalcDrgStrokeUnit.URL());
//        addTopic("lblCalcMedicalInfrastructure", Pages.CalcDrgMedicalInfrastructure.URL());
//        addTopic("lblCalcNonMedicalInfrastructure", Pages.CalcDrgNonMedicalInfrastructure.URL());
//        addTopic("lblCalcStaffCost", Pages.CalcDrgStaffCost.URL());
//        addTopic("lblCalcValvularIntervention", Pages.CalcDrgValvularIntervention.URL());
//        addTopic("lblCalcNeonatology", Pages.CalcDrgNeonatology.URL());
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
        _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);

        if (isValidId(_calcBasics.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    public void exportTest() throws IOException {
        createTransferFile(_calcBasics);
    }

    private void createTransferFile(PeppCalcBasics calcBasics) {
        File dir = new File(_sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), _sessionController.getApplicationTools().readConfig(ConfigKey.FolderUpload));
        File file;
        Date ts;
        do {
            ts = Calendar.getInstance().getTime();
            file = new File(dir, "Transfer" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ts) + ".txt");
        } while (file.exists());

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file))) {
            if (_sessionController.getAccount().isReportViaPortal()) {
                pw.println("Account.Mail=" + _sessionController.getAccount().getEmail());
            }
            pw.println("From=" + _sessionController.getAccount().getEmail());
            pw.println("Received=" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ts));
            pw.println("Subject=KGL_" + calcBasics.getIk());

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(calcBasics);
            pw.println("Content=" + json);

            pw.flush();
        } catch (FileNotFoundException | JsonProcessingException ex) {
            throw new IllegalStateException(ex);
        } finally {
        }
    }

    private void setModifiedInfo() {
        _calcBasics.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsPsySendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsPsySendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalationBasicsPsySendEnabled)) {
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
        populateDefaultsForUnreachableFields();
        if (!statementIsComplete()) {
            return getActiveTopic().getOutcome();
        }

        _calcBasics.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);

        if (isValidId(_calcBasics.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL") + " " + _calcBasics.getId());
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_calcBasics));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private void populateDefaultsForUnreachableFields() {
        // Some input fields can't be reached depending on other fields.
        // But they might contain elder values, which became obsolte by setting the other field 
        // Such fields will be populated with default values

        // todo
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
        _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _calcBasics.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Address">
    public List<SelectItem> getIks() {
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
        return items;
    }

    public String getHospitalInfo() {
        Customer c = _customerFacade.getCustomerByIK(_calcBasics.getIk());
        if (c == null || c.getName() == null) {
            return "";
        }
        return c.getName() + ", " + c.getTown();
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tab ServiceProvision">
    public int priorProvisionAmount(KGPListServiceProvision current) {
        Optional<KGPListServiceProvision> prior = _priorCalcBasics.getServiceProvisions().stream().filter(p -> p.getServiceProvisionTypeId() == current.getServiceProvisionTypeId()).findAny();
        if (prior.isPresent()) {
            return prior.get().getAmount();
        }
        return 0;
    }

    public void addServiceProvision() {
        int seq = _calcBasics.getServiceProvisions().stream().mapToInt(sp -> sp.getServiceProvisionType().getSequence()).max().orElse(0);
        KGPListServiceProvision data = new KGPListServiceProvision();
        data.setBaseInformationId(_calcBasics.getId());
        data.getServiceProvisionType().setSequence(++seq);
        _calcBasics.getServiceProvisions().add(data);
    }

    public void deleteServiceProvision(KGPListServiceProvision item) {
        _calcBasics.getServiceProvisions().remove(item);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Operation">
    public void checkOption(AjaxBehaviorEvent event){
        HtmlSelectOneMenu component =  (HtmlSelectOneMenu) event.getComponent();
        if (component.getValue().equals(3)){
            //_sessionController.setScript("alert('Bitte beachten Sie, dass die Erfassung der Rüstzeit als Einheitswert keine leistungsgerechte Verteilung der Kosten gewährleistet.')");
            Utils.showMessageInBrowser("Bitte beachten Sie, dass die Erfassung der Rüstzeit als Einheitswert keine leistungsgerechte Verteilung der Kosten gewährleistet.");
        }
    }
    // </editor-fold>
    

    public String calcPercentualDiff(int priorValue, int currentValue) {
        if (priorValue == 0) {
            return "";
        }
        return Math.round(1000d * (currentValue - priorValue) / priorValue) / 10d + "%";
    }

    public String getCostTypeText(int costTypeId) {
        CostType ct = _costTypeFacade.find(costTypeId);
        if (ct != null) {
            return ct.getText();
        }
        return "Unbekannte Kostenartengruppe";
    }
    
    public KGPListDelimitationFact getPriorDelimitationFact(int contentTextId) {
        for (KGPListDelimitationFact df : _priorCalcBasics.getDelimitationFacts()) {
            if (df.getContentTextId() == contentTextId) {
                return df;
            }
        }
        return new KGPListDelimitationFact();
    }

    public void addLocation() {
        KGLListLocation loc = new KGLListLocation();
        loc.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getLocations().add(loc);
    }

    public void deleteLocation(KGLListLocation loc) {
        _calcBasics.getLocations().remove(loc);
    }

}