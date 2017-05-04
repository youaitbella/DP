package org.inek.dataportal.feature.calculationhospital;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Id;
import javax.servlet.http.Part;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.drg.DrgCalcBasics;
import org.inek.dataportal.entities.calc.drg.DrgContentText;
import org.inek.dataportal.entities.calc.drg.DrgNeonatData;
import org.inek.dataportal.entities.calc.psy.KGPListContentText;
import org.inek.dataportal.entities.calc.psy.KGPListCostCenter;
import org.inek.dataportal.entities.calc.psy.KGPListDelimitationFact;
import org.inek.dataportal.entities.calc.psy.KGPListLocation;
import org.inek.dataportal.entities.calc.psy.KgpListMedInfra;
import org.inek.dataportal.entities.calc.psy.KGPListRadiologyLaboratory;
import org.inek.dataportal.entities.calc.psy.KGPListServiceProvision;
import org.inek.dataportal.entities.calc.psy.KGPListServiceProvisionType;
import org.inek.dataportal.entities.calc.psy.KGPListStationAlternative;
import org.inek.dataportal.entities.calc.psy.KGPListStationServiceCost;
import org.inek.dataportal.entities.calc.psy.KGPListTherapy;
import org.inek.dataportal.entities.calc.psy.KGPPersonalAccounting;
import org.inek.dataportal.entities.calc.psy.PeppCalcBasics;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.TransferFileCreator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.ValueLists;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
//@SuppressWarnings("PMD.LawOfDemeter")
@SuppressWarnings("PMD")
public class EditCalcBasicsPepp extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditCalcBasicsPepp");
    private static final int ONE_HOUR = 3600;

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject private ApplicationTools _appTools;

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

    private List<SelectItem> _cachedIks;

    public List<SelectItem> getCachedIks() {
        return _cachedIks;
    }

    public void setCachedIks(List<SelectItem> iks) {
        _cachedIks = iks;
    }
    // </editor-fold>

    @PostConstruct
    private void init() {
        LOGGER.info("start init EditCalcBasicPepp");
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("id");
        if ("new".equals(id)) {
            _calcBasics = newCalcBasicsPepp();
        } else if (Utils.isInteger(id)) {
            PeppCalcBasics calcBasics = loadCalcBasicsPepp(id);
            if (calcBasics.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                LOGGER.info("end init EditCalcBasicPepp");
                return;
            }
            _calcBasics = calcBasics;
            retrievePriorData(_calcBasics);
            populateDelimitationFactsIfAbsent(_calcBasics);
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
        // session timeout extended to 1 hour (to provide enough time for an upload)
        FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(ONE_HOUR);
        LOGGER.info("end init EditCalcBasicPepp");
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

        // MedicalInfrastructure
        calcBasics.setDescMedicalInfra(!_priorCalcBasics.getOtherMethodMedInfra().isEmpty());
        //calcBasics.setDescMedicalInfra(_priorCalcBasics.getIblvMethodMedInfra() == 0);
        calcBasics.setOtherMethodMedInfra(_priorCalcBasics.getOtherMethodMedInfra());
        calcBasics.setIblvMethodMedInfra(_priorCalcBasics.getIblvMethodMedInfra());

        // Personal Accounting
        calcBasics.setPersonalAccountingDescription(_priorCalcBasics.getPersonalAccountingDescription());
        ensurePersonalAccountingData(calcBasics);

        Map<Integer, KGPPersonalAccounting> priorPersonalAccountingsAmount = new HashMap<>();
        for (KGPPersonalAccounting ppa : _priorCalcBasics.getPersonalAccountings()) {
            priorPersonalAccountingsAmount.put(ppa.getCostTypeId(), ppa);
        }

        for (KGPPersonalAccounting pa : calcBasics.getPersonalAccountings()) {
            if (priorPersonalAccountingsAmount.containsKey(pa.getCostTypeId())) {
                KGPPersonalAccounting ppa = priorPersonalAccountingsAmount.get(pa.getCostTypeId());
                pa.setPriorCostAmount(ppa.getAmount());
                pa.setExpertRating(ppa.isExpertRating());
                pa.setOther(ppa.isOther());
                pa.setServiceEvaluation(ppa.isServiceEvaluation());
                pa.setServiceStatistic(ppa.isServiceStatistic());
                pa.setStaffEvaluation(ppa.isStaffEvaluation());
                pa.setStaffRecording(ppa.isStaffRecording());
            }
        }

        preloadServiceProvision(calcBasics);

    }

    private PeppCalcBasics loadCalcBasicsPepp(String idObject) {
        int id = Integer.parseInt(idObject);
        PeppCalcBasics calcBasics = _calcFacade.findCalcBasicsPepp(id);
        if (hasSufficientRights(calcBasics)) {
            calcBasics.setDescMedicalInfra(!calcBasics.getOtherMethodMedInfra().isEmpty());
            return calcBasics;
        }
        return new PeppCalcBasics();
    }

    private boolean hasSufficientRights(PeppCalcBasics calcBasics) {
        if (_sessionController.isMyAccount(calcBasics.getAccountId(), false)) {
            return true;
        }
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL)) {
            return true;
        }
        return _cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL,
                calcBasics.getStatus(),
                calcBasics.getAccountId());
    }

    private PeppCalcBasics newCalcBasicsPepp() {
        LOGGER.info("start newCalcBasicsPepp");
        Account account = _sessionController.getAccount();
        PeppCalcBasics calcBasics = new PeppCalcBasics();
        calcBasics.setAccountId(account.getId());
        calcBasics.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        // calcBasics.getKgpMedInfraList().add(new KgpListMedInfra(-1, 170, "", "", "", 0, calcBasics.getId()));
        // calcBasics.getKgpMedInfraList().add(new KgpListMedInfra(-1, 180, "", "", "", 0, calcBasics.getId()));
        setCachedIks(getIks());
        if (getCachedIks().size() == 1) {
            calcBasics.setIk((int) getCachedIks().get(0).getValue());
        }

        retrievePriorData(calcBasics);
        preloadData(calcBasics);
        LOGGER.info("end newCalcBasicsPepp");
        return calcBasics;
    }

    @Inject private ValueLists _valueLists;

    private void ensurePersonalAccountingData(PeppCalcBasics calcBasics) {
        calcBasics.getPersonalAccountings().clear();
        calcBasics.getPersonalAccountings().add(new KGPPersonalAccounting(_valueLists.getCostType(110), 0));
        calcBasics.getPersonalAccountings().add(new KGPPersonalAccounting(_valueLists.getCostType(120), 0));
        calcBasics.getPersonalAccountings().add(new KGPPersonalAccounting(_valueLists.getCostType(130), 0));
        calcBasics.getPersonalAccountings().add(new KGPPersonalAccounting(_valueLists.getCostType(131), 0));
        calcBasics.getPersonalAccountings().add(new KGPPersonalAccounting(_valueLists.getCostType(132), 0));
        calcBasics.getPersonalAccountings().add(new KGPPersonalAccounting(_valueLists.getCostType(133), 0));
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
            data.setBaseInformationId(calcBasics.getId());
            calcBasics.getNeonateData().add(data);
        }
    }

    private void preloadServiceProvision(PeppCalcBasics calcBasics) {
        calcBasics.getServiceProvisions().clear();
        List<KGPListServiceProvisionType> provisionTypes
                = _calcFacade.retrieveServiceProvisionTypesPepp(calcBasics.getDataYear(), true);

        int seq = 0;

        // always populate mandatory entries
        for (KGPListServiceProvisionType provisionType : provisionTypes) {
            KGPListServiceProvision data = new KGPListServiceProvision();
            data.setBaseInformationId(calcBasics.getId());
            data.setServiceProvisionType(provisionType);
            data.setServiceProvisionTypeId(provisionType.getId());
            data.setSequence(++seq);
            calcBasics.getServiceProvisions().add(data);
        }

        // get prior values and additional entries
        for (KGPListServiceProvision prior : _priorCalcBasics.getServiceProvisions()) {
            Optional<KGPListServiceProvision> currentOpt = calcBasics.getServiceProvisions().stream()
                    .filter(sp -> sp.getServiceProvisionTypeId() == prior.getServiceProvisionTypeId()).findAny();
            if (currentOpt.isPresent()) {
                KGPListServiceProvision current = currentOpt.get();
                current.setProvidedTypeId(prior.getProvidedTypeId());
            } else if (!prior.isEmpty()) {
                KGPListServiceProvision data = new KGPListServiceProvision();
                data.setBaseInformationId(calcBasics.getId());
                data.setServiceProvisionType(prior.getServiceProvisionType());
                data.setServiceProvisionTypeId(prior.getServiceProvisionTypeId());
                data.setProvidedTypeId(prior.getProvidedTypeId());
                data.setSequence(++seq);
                calcBasics.getServiceProvisions().add(data);
            }
        }

        calcBasics.getDelimitationFacts().clear();
        populateDelimitationFactsIfAbsent(calcBasics);

    }

    private void populateDelimitationFactsIfAbsent(PeppCalcBasics calcBasics) {
        if (!calcBasics.getDelimitationFacts().isEmpty()) {
            return;
        }
        if (calcBasics.getId() > 0) {
            // This should not be. But sometimes we lost the delimitationFacts...
            LOGGER.log(Level.WARNING,
                    "Populate PSY DelimitationFacts for existing data: Id = {0}", calcBasics.getId());
        }
        for (KGPListContentText ct : _calcFacade.retrieveContentTextsPepp(1, calcBasics.getDataYear())) {
            KGPListDelimitationFact df = new KGPListDelimitationFact();
            df.setBaseInformationId(calcBasics.getId());
            df.setContentText(ct);
            df.setUsed(getPriorDelimitationFact(ct.getId()).isUsed());
            calcBasics.getDelimitationFacts().add(df);
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
        addTopic("TopicFrontPage", Pages.CalcPeppBasics.URL());
        addTopic("TopicBasicExplanation", Pages.CalcPeppBasicExplanation.URL());
        addTopic("TopicCalcExternalServiceProvision", Pages.CalcPeppExternalServiceProvision.URL());
        addTopic("lblCalcTherapyScope", Pages.CalcPeppTherapyScope.URL());
        addTopic("TopicCalcRadiology", Pages.CalcPeppRadiology.URL());
        addTopic("TopicCalcLaboratory", Pages.CalcPeppLaboratory.URL());
        addTopic("TopicCalcDiagnosticScope", Pages.CalcPeppDiagnosticScope.URL());
        addTopic("TopicCalcTherapeuticScope", Pages.CalcPeppTherapeuticScope.URL());
        addTopic("TopicCalcPatientAdmission", Pages.CalcPeppPatientAdmission.URL());
        addTopic("TopicCalcStation", Pages.CalcPeppStation.URL());
        addTopic("TopicCalcMedicalInfrastructure", Pages.CalcPeppMedicalInfrastructure.URL());
        addTopic("TopicCalcNonMedicalInfrastructure", Pages.CalcPeppNonMedicalInfrastructure.URL());
        addTopic("TopicCalcStaffCost", Pages.CalcPeppStaffCost.URL());
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_calcBasics.getAccountId(), false);
    }

    public boolean isReadOnly() {
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return true;
        }
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL,
                _calcBasics.getStatus(),
                _calcBasics.getAccountId());
    }

    @Override
    protected void topicChanged() {
        if (_sessionController.getAccount().isAutoSave() && !isReadOnly()) {
            saveData();
        }
    }

    public String save() {
        saveData();

        if (isValidId(_calcBasics.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    public void saveData() {
        setModifiedInfo();
        _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);
    }

    private void setModifiedInfo() {
        _calcBasics.setLastChanged(Calendar.getInstance().getTime());
        _calcBasics.setAccountIdLastChange(_sessionController.getAccountId());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsPsySendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL,
                _calcBasics.getStatus(),
                _calcBasics.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsPsySendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL,
                _calcBasics.getStatus(),
                _calcBasics.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsPsySendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL,
                _calcBasics.getStatus(),
                _calcBasics.getAccountId());
    }

    public boolean isTakeEnabled() {
        return false;
        // todo: do not allow consultant
        //return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isCopyForResendAllowed() {
        if (_calcBasics.getStatusId() < 10 || !_appTools.isEnabled(ConfigKey.IsCalculationBasicsPsySendEnabled)) {
            return false;
        }
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return false;
        }
        return !_calcFacade.existActiveCalcBasicsPsy(_calcBasics.getIk());
    }

    @SuppressWarnings("unchecked")
    public void copyForResend() {
        if (false) {
            // in a first approch, we do not copy the data
            // just reset the status to "CorrectionRequested"
            _calcBasics.setStatus(WorkflowStatus.CorrectionRequested);
            _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);
            return;
        }

        _calcBasics.setStatus(WorkflowStatus.Retired);
        _calcFacade.saveCalcBasicsPepp(_calcBasics);

        _calcFacade.detach(_calcBasics);
        _calcBasics.setId(-1);
        _calcBasics.setStatus(WorkflowStatus.New);

        // this loop seems to be really complicated,
        // but we need to copy the lists on a defined order (by id)
        // otherwise most list within the copy become shuffled
        for (Field field : _calcBasics.getClass().getDeclaredFields()) {
            try {
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                if (field.getAnnotation(Id.class) != null) {
                    continue;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(_calcBasics);
                if (!(value instanceof List)) {
                    continue;
                }
                List list = (List) value;
                if (list.isEmpty() || !(list.get(0) instanceof BaseIdValue)) {
                    continue;
                }
                List<BaseIdValue> data = new ArrayList();
                for (Object object : list) {
                    _calcFacade.detach(object);
                    BaseIdValue baseIdValue = (BaseIdValue) object;
                    data.add(baseIdValue);
                }
                List<BaseIdValue> dataCopy = new ArrayList();
                data.stream().sorted((b1, b2) -> Integer.compare(b1.getId(), b2.getId())).forEach(
                        baseIdValue -> {
                            baseIdValue.setId(-1);
                            baseIdValue.setBaseInformationId(-1);
                            dataCopy.add(baseIdValue);
                        });
                field.set(_calcBasics, dataCopy);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "error during setDataToNew: {0}", field.getName());
            }
        }

        _calcBasics.setAccountId(_sessionController.getAccountId());

        _calcBasics.setStatus(WorkflowStatus.CorrectionRequested);
        try {
            _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Exception during setDataToNew: {0}", ex.getMessage());
        }
        //_baseLine = null;
    }

    /**
     * This function seals a statement od participance if possible. Sealing is possible, if all mandatory fields are
     * fulfilled. After sealing, the statement od participance can not be edited anymore and is available for the InEK.
     *
     * @return
     */
    public String seal() {
        if (!calcBasicsIsComplete()) {
            return "";
        }
        CalcBasicsPsyValueCleaner.clearUnusedFields(_calcBasics);

        _calcBasics.setStatus(WorkflowStatus.Provided);
        _calcBasics.setSealed(Calendar.getInstance().getTime());
        saveData();

        TransferFileCreator.createCalcBasicsTransferFile(_sessionController, _calcBasics);

        if (isValidId(_calcBasics.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_calcBasics));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean calcBasicsIsComplete() {
        MessageContainer message = CalcBasicsPsyValidator.composeMissingFieldsMessage(_calcBasics);
        if (message.containsMessage()) {
            message.setMessage(Utils.getMessage("infoMissingFields") + "\\r\\n" + message.getMessage());
            if (!message.getTopic().isEmpty()) {
                setActiveTopic(message.getTopic());
            }
            String script = "alert ('" + message.getMessage() + "');";
            if (!message.getElementId().isEmpty()) {
                script += "\r\n document.getElementById('" + message.getElementId() + "').focus();";
            }
            _sessionController.setScript(script);
        }
        return !message.containsMessage();
    }

    public String requestApproval() {
        if (!calcBasicsIsComplete()) {
            return null;
        }
        _calcBasics.setStatus(WorkflowStatus.ApprovalRequested);
        saveData();
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _calcBasics.setAccountId(_sessionController.getAccountId());
        saveData();
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Address">
    private List<SelectItem> _ikItems;

    public List<SelectItem> getIks() {
        if (_ikItems == null) {
            //Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> iks = _calcFacade
                    .obtainIks4NewBasics(CalcHospitalFunction.CalculationBasicsPepp,
                            _sessionController.getAccountId(),
                            Utils.getTargetYear(Feature.CALCULATION_HOSPITAL),
                            testMode);
            if (_calcBasics != null && _calcBasics.getIk() > 0) {
                iks.add(_calcBasics.getIk());
            }

            _ikItems = new ArrayList<>();
            for (int ik : iks) {
                _ikItems.add(new SelectItem(ik));
            }
        }
        return _ikItems;
    }

    // </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Tab ServiceProvision">
    public int priorProvisionAmount(KGPListServiceProvision current) {
        Optional<KGPListServiceProvision> prior = _priorCalcBasics.getServiceProvisions().stream()
                .filter(p -> p.getServiceProvisionTypeId() == current.getServiceProvisionTypeId())
                .findAny();
        if (prior.isPresent()) {
            return prior.get().getAmount();
        }
        return 0;
    }

    public void addServiceProvision() {
        int seq = _calcBasics.getServiceProvisions().stream().mapToInt(sp -> sp.getSequence()).max().orElse(0);
        KGPListServiceProvision data = new KGPListServiceProvision();
        data.setBaseInformationId(_calcBasics.getId());
        data.setServiceProvisionTypeId(-1);
        data.setSequence(++seq);
        _calcBasics.getServiceProvisions().add(data);
    }

    public void deleteServiceProvision(KGPListServiceProvision item) {
        _calcBasics.getServiceProvisions().remove(item);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Operation">
    public void checkOption(AjaxBehaviorEvent event) {
        HtmlSelectOneMenu component = (HtmlSelectOneMenu) event.getComponent();
        if (component.getValue().equals(3)) {
            //_sessionController.setScript("alert('Bitte beachten Sie, dass die Erfassung der Rüstzeit als Einheitswert keine leistungsgerechte Verteilung der Kosten gewährleistet.')");
            Utils.showMessageInBrowser("Bitte beachten Sie, dass die Erfassung der Rüstzeit als Einheitswert keine leistungsgerechte Verteilung der Kosten gewährleistet.");
        }
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tab Diagnostics / therapy / patient admission">
    public void addCostCenter(int costCenterId) {
        KGPListCostCenter item = new KGPListCostCenter(_calcBasics.getId(), costCenterId);
        _calcBasics.getCostCenters().add(item);
    }

    public void deleteCostCenter(KGPListCostCenter item) {
        _calcBasics.getCostCenters().remove(item);
    }

    public void deleteCostCenters(int costCenterId) {
        for (Iterator<KGPListCostCenter> itr = _calcBasics.getCostCenters().iterator(); itr.hasNext();) {
            KGPListCostCenter center = itr.next();
            if (center.getCostCenterId() == costCenterId) {
                itr.remove();
            }
        }
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    private Part _fileTherapyPepp;

    public Part getFileTherapyPepp() {
        return _fileTherapyPepp;
    }

    public void setFileTherapyPepp(Part file) {
        _fileTherapyPepp = file;
    }

    private static final String HEADLINE_THERAPY = "KST-Gruppe;Leistungsinhalt der Kostenstelle;Fremdvergabe (keine, teilweise, vollständig);Leistungsschlüssel;KoArtG 1 Summe Leistungseinheiten;KoArtG 1 Personalkosten;KoArtG 3a Summe Leistungseinheiten;KoArtG 3a Personalkosten;KoArtG 2 Summe Leistungseinheiten;KoArtG 2 Personalkosten;KoArtG 3b Summe Leistungseinheiten;KoArtG 3b Personalkosten;KoArtG 3c Summe Leistungseinheiten;KoArtG 3c Personalkosten;KoArtG 3 Summe Leistungseinheiten;KoArtG 3 Personalkosten";

    public void downloadTemplateTherapy() {
        Utils.downloadText(HEADLINE_THERAPY + "\n", "Therapeutischer_Bereich_.csv");
    }
    private static final String HEADLINE = "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;Kostenvolumen;VollkräfteÄD;Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten";

    public void downloadTemplate() {
        Utils.downloadText(HEADLINE + "\n", "Kostenstellengruppe_11_12_13.csv");
    }

    private String _importMessage = "";

    public String getImportMessage() {
        return _importMessage;
    }

    private String _importMessageTherapy = "";

    public String getImportMessageTherapy() {
        return _importMessageTherapy;
    }

    @Inject private Instance<CostCenterDataImporterPepp> _importProvider;
    @Inject private Instance<TherapyDataImporterPepp> _importProviderTherapyPepp;
    @Inject private Instance<StationDataImporterPepp> _stationProviderPepp;

    private Part _fileStation;

    public Part getFileStation() {
        return _fileStation;
    }

    public void setFileStation(Part fileStation) {
        this._fileStation = fileStation;
    }

    private String _importMessageStation;

    public String getImportMessageStation() {
        return _importMessageStation;
    }

    public void setImportMessageStation(String importMessageStation) {
        this._importMessageStation = importMessageStation;
    }

    private boolean _showJournalStation;

    public boolean isShowJournalStation() {
        return _showJournalStation;
    }

    public void setShowJournalStation(boolean showJournalStation) {
        this._showJournalStation = showJournalStation;
    }

    public void toggleJournalStation() {
        this._showJournalStation = !this._showJournalStation;
    }

    public void getDownloadTemplateStation() {
        StationDataImporterPepp stationProvider = _stationProviderPepp.get();
        stationProvider.downloadTemplate();
    }

    public void uploadNoticesStation() {
        StationDataImporterPepp stationProvider = _stationProviderPepp.get();
        stationProvider.setFile(_fileStation);
        stationProvider.setCalcBasics(_calcBasics);
        stationProvider.uploadNotices();
        _importMessageStation = stationProvider.getMessage();
        _sessionController.alertClient(_importMessageStation);
        _showJournalStation = _importMessageStation.contains("Fehler");
    }

    public void uploadNoticesTherapy() {
        try {
            if (_fileTherapyPepp != null) {
                //Scanner scanner = new Scanner(_file.getInputStream(), "UTF-8");
                // We assume most of the documents coded with the Windows character set
                // Thus, we read with the system default
                // in case of an UTF-8 file, all German Umlauts will be corrupted.
                // We simply replace them.
                // Drawbacks: this only converts the German Umlauts, no other chars.
                // By intention it fails for other charcters
                // Alternative: implement a library which guesses th correct character set and read properly
                // Since we support German only, we started using the simple approach
                Scanner scanner = new Scanner(_fileTherapyPepp.getInputStream());
                if (!scanner.hasNextLine()) {
                    return;
                }
                TherapyDataImporterPepp itemImporter = _importProviderTherapyPepp.get();
                itemImporter.setCalcBasics(_calcBasics);
                while (scanner.hasNextLine()) {
                    String line = Utils.convertFromUtf8(scanner.nextLine());
                    if (!line.equals(HEADLINE_THERAPY)) {
                        itemImporter.tryImportLine(line);
                    }
                }
                _importMessageTherapy = itemImporter.getMessage();
                _sessionController.alertClient(_importMessageTherapy);
                _showJournalTherapy = _importMessageTherapy.contains("Fehler");
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

    public void uploadNotices() {
        try {
            if (_file != null) {
                //Scanner scanner = new Scanner(_file.getInputStream(), "UTF-8");
                // We assume most of the documents coded with the Windows character set
                // Thus, we read with the system default
                // in case of an UTF-8 file, all German Umlauts will be corrupted.
                // We simply replace them.
                // Drawbacks: this only converts the German Umlauts, no other chars.
                // By intention it fails for other charcters
                // Alternative: implement a library which guesses th correct character set and read properly
                // Since we support German only, we started using the simple approach
                Scanner scanner = new Scanner(_file.getInputStream());
                if (!scanner.hasNextLine()) {
                    return;
                }
                CostCenterDataImporterPepp itemImporter = _importProvider.get();
                itemImporter.setCalcBasics(_calcBasics);
                while (scanner.hasNextLine()) {
                    String line = Utils.convertFromUtf8(scanner.nextLine());
                    if (!line.equals(HEADLINE)) {
                        itemImporter.tryImportLine(line);
                    }
                }
                _importMessage = itemImporter.getMessage();
                _sessionController.alertClient(_importMessage);
                _showJournal = false;
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

    public void toggleJournal() {
        _showJournal = !_showJournal;
    }

    private boolean _showJournal = false;

    public boolean isShowJournal() {
        return _showJournal;
    }

    public void setShowJournal(boolean showJournal) {
        this._showJournal = showJournal;
    }

    public void toggleJournalTherapy() {
        _showJournalTherapy = !_showJournalTherapy;
    }

    private boolean _showJournalTherapy = false;

    public boolean isShowJournalTherapy() {
        return _showJournalTherapy;
    }

    public void setShowJournalTherapy(boolean showJournal) {
        this._showJournalTherapy = showJournal;
    }
    //</editor-fold>

    public String calcPercentualDiff(int priorValue, int currentValue) {
        if (priorValue == 0) {
            return "";
        }
        return Math.round(1000d * (currentValue - priorValue) / priorValue) / 10d + "%";
    }

    public String calcRatio(int nominator, int denominator) {
        if (denominator == 0) {
            return "";
        }
        return "" + Math.round(nominator * 100d / denominator) / 100d;
    }

    public KGPListDelimitationFact getPriorDelimitationFact(int contentTextId) {
        return _priorCalcBasics.getDelimitationFacts().stream()
                .filter(f -> f.getContentTextId() == contentTextId)
                .findAny().orElse(new KGPListDelimitationFact());
    }

    public void addLocation() {
        KGPListLocation loc = new KGPListLocation();
        loc.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getLocations().add(loc);
    }

    public void deleteLocation(KGPListLocation loc) {
        _calcBasics.getLocations().remove(loc);
    }

    public List<KgpListMedInfra> getMedInfra(int costType) {
        List<KgpListMedInfra> tmp = new ArrayList<>();
        _calcBasics.getKgpMedInfraList().stream()
                .filter((mi) -> (mi.getCostTypeId() == costType))
                .forEachOrdered((mi) -> {
                    tmp.add(mi);
                });
        return tmp;
    }

    public void addMedInfra(int costTypeId) {
        KgpListMedInfra mif = new KgpListMedInfra(_calcBasics.getId(), costTypeId);
        _calcBasics.getKgpMedInfraList().add(mif);
    }

    public void deleteKgpMedInfraList(int costTypeId){
        _calcBasics.deleteKgpMedInfraList(costTypeId);
    }    
    
    public void deleteMedInfra(KgpListMedInfra mif) {
        _calcBasics.getKgpMedInfraList().remove(mif);
    }

    @Inject private DataImporterPool importerPool;

    public Part getMedInfraFile() {
        return importerPool.getDataImporter("peppmedinfra").getFile();
    }

    public void setMedInfraFile(Part file) {
        importerPool.getDataImporter("peppmedinfra").setFile(file);
    }

    public void downloadTemplateHeadlineMedInfra() {
        importerPool.getDataImporter("peppmedinfra").downloadTemplate();
    }

    public String getImportMessageMedInfra() {
        return importerPool.getDataImporter("peppmedinfra").getMessage();
    }

    public Part getNonMedInfraFile() {
        return importerPool.getDataImporter("peppnonmedinfra").getFile();
    }

    public void setNonMedInfraFile(Part file) {
        importerPool.getDataImporter("peppnonmedinfra").setFile(file);
    }

    public void downloadTemplateHeadlineNonMedInfra() {
        importerPool.getDataImporter("peppnonmedinfra").downloadTemplate();
    }

    public String getImportMessageNonMedInfra() {
        return importerPool.getDataImporter("peppnonmedinfra").getMessage();
    }

    public void uploadNoticesMedInfra(int costType) {
        DataImporter<? extends BaseIdValue> dataImporter;
        if (170 == costType) {
            dataImporter = importerPool.getDataImporter("peppmedinfra");
        } else {
            dataImporter = importerPool.getDataImporter("peppnonmedinfra");
        }
        dataImporter.uploadNoticesPepp(_calcBasics);
        _sessionController.alertClient(dataImporter.getMessage());
        _showJournal = dataImporter.containsError();
    }

//    @Inject private Instance<MedInfraDataImporterPepp> _importMedInfraPepp;
//
//    private static final String HEADLINE_MED_INFRA
//            = "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen";
//
//    public void downloadTemplateHeadlineMedInfra() {
//        Utils.downloadText(HEADLINE_MED_INFRA + "\n", "Med_Infra.csv");
//    }
//
//    public void downloadTemplateHeadlineNonMedInfra() {
//        Utils.downloadText(HEADLINE_MED_INFRA + "\n", "Nicht_Med_Infra.csv");
//    }
//
//    private String _importMessageMedInfra = "";
//
//    public String getImportMessageMedInfra() {
//        return _importMessageMedInfra;
//    }
//
//    private String _importMessageNonMedInfra = "";
//
//    public String getImportMessageNonMedInfra() {
//        return _importMessageNonMedInfra;
//    }
//
//    public void uploadNoticesMedInfra(int costType) {
//        try {
//            if (_file != null) {
//                //Scanner scanner = new Scanner(_file.getInputStream(), "UTF-8");
//                // We assume most of the documents coded with the Windows character set
//                // Thus, we read with the system default
//                // in case of an UTF-8 file, all German Umlauts will be corrupted.
//                // We simply replace them.
//                // Drawbacks: this only converts the German Umlauts, no other chars.
//                // By intention it fails for other charcters
//                // Alternative: implement a library which guesses th correct character set and read properly
//                // Since we support German only, we started using the simple approach
//                Scanner scanner = new Scanner(_file.getInputStream());
//                if (!scanner.hasNextLine()) {
//                    return;
//                }
//                MedInfraDataImporterPepp itemImporter = _importMedInfraPepp.get();
//                itemImporter.setCostTypeId(costType);
//                itemImporter.setCalcBasics(_calcBasics);
//                while (scanner.hasNextLine()) {
//                    String line = Utils.convertFromUtf8(scanner.nextLine());
//                    if (!line.equals(HEADLINE_MED_INFRA)) {
//                        itemImporter.tryImportLine(line);
//                    }
//                }
//                if (costType == 170) {
//                    _importMessageMedInfra = itemImporter.getMessage();
//                    _sessionController.alertClient(_importMessageMedInfra);
//                } else {
//                    _importMessageNonMedInfra = itemImporter.getMessage();
//                    _sessionController.alertClient(_importMessageNonMedInfra);
//                }
//                _showJournal = itemImporter.containsError();
//            }
//        } catch (IOException | NoSuchElementException e) {
//        }
//    }
    public boolean renderPersonalAccountingDescription() {
        for (KGPPersonalAccounting pa : _calcBasics.getPersonalAccountings()) {
            if (pa.isExpertRating() || pa.isServiceStatistic() || pa.isOther()) {
                return true;
            }
        }
        return false;
    }

    public int getMedInfraSum(int type) {
        int sumAmount = 0;
        for (KgpListMedInfra m : _calcBasics.getKgpMedInfraList()) {
            if (m.getCostTypeId() == type) {
                sumAmount += m.getAmount();
            }
        }
        return sumAmount;
    }

    public List<KGPListTherapy> getTherapies(int costCenterId) {
        return _calcBasics.getTherapies().stream()
                .filter(t -> t.getCostCenterId() == costCenterId).collect(Collectors.toList());
    }

    public void deleteTherapy(KGPListTherapy item) {
        _calcBasics.getTherapies().remove(item);
    }

    public void addTherapyCost(int costCenterId) {
        KGPListTherapy result = new KGPListTherapy();
        result.setCostCenterId(costCenterId);
        result.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getTherapies().add(result);
    }

    public void addStationAlternative() {
        KGPListStationAlternative sa = new KGPListStationAlternative();
        sa.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getKgpStationDepartmentList().add(sa);
    }

    public void deleteStationAlternative(KGPListStationAlternative sa) {
        _calcBasics.getKgpStationDepartmentList().remove(sa);
    }

    public void addStationServiceCost() {
        KGPListStationServiceCost sc = new KGPListStationServiceCost(_calcBasics.getId());
        _calcBasics.getStationServiceCosts().add(sc);
    }

    public void deleteStationServiceCost(KGPListStationServiceCost item) {
        _calcBasics.getStationServiceCosts().remove(item);
    }

    public void clearStationServiceCosts(){
        _calcBasics.clearStationServiceCosts();
    }
    
    public List<KGPListRadiologyLaboratory> getRadiologyLaboritories(int costCenter) {
        return _calcBasics.getRadiologyLaboratories().stream()
                .filter(r -> r.getCostCenterId() == costCenter)
                .collect(Collectors.toList());
    }

    public void deleteRadiologyLaboratory(KGPListRadiologyLaboratory item) {
        _calcBasics.getRadiologyLaboratories().remove(item);
    }

    public void addRadiologyLaboratoy(int costCenter) {
        KGPListRadiologyLaboratory item = new KGPListRadiologyLaboratory();
        item.setCostCenterId(costCenter);
        item.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getRadiologyLaboratories().add(item);
    }
}
