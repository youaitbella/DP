package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
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
import javax.persistence.Id;
import javax.persistence.OptimisticLockException;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.psy.KGPListContentText;
import org.inek.dataportal.entities.calc.psy.KGPListCostCenter;
import org.inek.dataportal.entities.calc.psy.KGPListDelimitationFact;
import org.inek.dataportal.entities.calc.psy.KGPListLocation;
import org.inek.dataportal.entities.calc.psy.KGPListRadiologyLaboratory;
import org.inek.dataportal.entities.calc.psy.KGPListServiceProvision;
import org.inek.dataportal.entities.calc.psy.KGPListServiceProvisionType;
import org.inek.dataportal.entities.calc.psy.KGPListStationAlternative;
import org.inek.dataportal.entities.calc.psy.KGPListStationServiceCost;
import org.inek.dataportal.entities.calc.psy.KGPListTherapy;
import org.inek.dataportal.entities.calc.psy.KGPPersonalAccounting;
import org.inek.dataportal.entities.calc.psy.KgpListMedInfra;
import org.inek.dataportal.entities.calc.psy.PeppCalcBasics;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.CalcPsyFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.ObjectUtils;
import org.inek.dataportal.helper.TransferFileCreator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.FieldValues;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.ValueLists;

@Named
@ViewScoped
@SuppressWarnings("PMD")
public class EditCalcBasicsPepp extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditCalcBasicsPepp");
    private static final int ONE_HOUR = 3600;

    @Inject private AccessManager _accessManager;
    @Inject private SessionController _sessionController;
    @Inject private CalcPsyFacade _calcFacade;
    @Inject private ApplicationTools _appTools;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    private PeppCalcBasics _calcBasics;

    public PeppCalcBasics getCalcBasics() {
        return _calcBasics;
    }

    public void setCalcBasics(PeppCalcBasics calcBasics) {
        _calcBasics = calcBasics;
    }

    private PeppCalcBasics _baseLine;

    private PeppCalcBasics _priorCalcBasics;

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
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("id");
        if ("new".equals(id)) {
            _calcBasics = newCalcBasicsPepp();
            _baseLine = null;
        } else if (Utils.isInteger(id)) {
            PeppCalcBasics calcBasics = loadCalcBasicsPepp(id);
            if (calcBasics.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _calcBasics = calcBasics;
            _baseLine = _calcFacade.findCalcBasicsPepp(_calcBasics.getId());
            retrievePriorData(_calcBasics);
            populateDelimitationFactsIfAbsent(_calcBasics);
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
        // session timeout extended to 1 hour (to provide enough time for an upload)
        FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(ONE_HOUR);
    }

    public void retrievePriorData(PeppCalcBasics calcBasics) {
        _priorCalcBasics = _calcFacade.retrievePriorCalcBasics(calcBasics);
    }

    public void ikChanged() {
        retrievePriorData(_calcBasics);
        preloadData(_calcBasics);
    }

    private void preloadData(PeppCalcBasics calcBasics) {
        //Station Costs

        for (KGPListStationServiceCost ssc : _priorCalcBasics.getStationServiceCosts()) {
            KGPListStationServiceCost newCost = new KGPListStationServiceCost();
            newCost.setCostCenterNumber(ssc.getCostCenterNumber());
            newCost.setStation(ssc.getStation());
            calcBasics.addStationServiceCost(newCost);
        }

        // Locations
        calcBasics.setLocationCnt(_priorCalcBasics.getLocationCnt());
        calcBasics.setDifLocationSupply(_priorCalcBasics.isDifLocationSupply());

        // MedicalInfrastructure
        calcBasics.setDescMedicalInfra(!_priorCalcBasics.getOtherMethodMedInfra().isEmpty());
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
        return _accessManager.isAccessAllowed(Feature.CALCULATION_HOSPITAL,
                        calcBasics.getStatus(),
                        calcBasics.getAccountId(),
                        calcBasics.getIk());
    }

    private PeppCalcBasics newCalcBasicsPepp() {
        Account account = _sessionController.getAccount();
        PeppCalcBasics calcBasics = new PeppCalcBasics();
        calcBasics.setAccountId(account.getId());
        calcBasics.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        setCachedIks(getIks());
        if (getCachedIks().size() == 1) {
            calcBasics.setIk((int) getCachedIks().get(0).getValue());
        }

        retrievePriorData(calcBasics);
        preloadData(calcBasics);
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
        for (KGPListContentText ct : _calcFacade.retrieveContentTextsPepp(1, calcBasics.getDataYear())) {
            KGPListDelimitationFact df = new KGPListDelimitationFact();
            df.setBaseInformationId(calcBasics.getId());
            df.setContentText(ct);
            df.setUsed(getPriorDelimitationFact(ct.getId()).isUsed());
            calcBasics.getDelimitationFacts().add(df);
        }
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
    public boolean isReadOnly() {
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return true;
        }
        return _accessManager.isReadOnly(Feature.CALCULATION_HOSPITAL,
                _calcBasics.getStatus(),
                _calcBasics.getAccountId(),
                _calcBasics.getIk());
    }

    @Override
    protected void topicChanged() {
        if (_sessionController.getAccount().isAutoSave() && !isReadOnly()) {
            saveData(false);
        }
    }

    public String save() {
        return saveData(true);
    }

    public String saveData(boolean showSaveMessage) {
        _calcBasics.removeEmptyServiceProvisions();

        if (_baseLine != null && ObjectUtils.getDifferences(_baseLine, _calcBasics, null).isEmpty()) {
            // nothing is changed, but we will reload the data if changed by somebody else (as indicated by a new version)
            if (_baseLine.getVersion() != _calcFacade.getCalcBasicsPsyVersion(_calcBasics.getId())) {
                _baseLine = _calcFacade.findCalcBasicsPepp(_calcBasics.getId());
                _calcBasics = _calcFacade.findCalcBasicsPepp(_calcBasics.getId());
            }
            return null;
        }

        setModifiedInfo();
        String msg = "";
        try {
            _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);
            if (!isValidId(_calcBasics.getId())) {
                return Pages.Error.RedirectURL();
            }
            if (showSaveMessage) {
                msg = Utils.getMessage("msgSaveAndMentionSend");
            }
        } catch (Exception ex) {
            if (!(ex instanceof OptimisticLockException) && !(ex.getCause() instanceof OptimisticLockException)) {
                throw ex;
            }
            msg = mergeAndReportChanges();
        }
        _baseLine = _calcFacade.findCalcBasicsPepp(_calcBasics.getId());
        if (!msg.isEmpty()) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
        }
        return null;
    }

    private String mergeAndReportChanges() {
        PeppCalcBasics modifiedCalcBasics = _calcBasics;
        _calcBasics = _calcFacade.findCalcBasicsPepp(modifiedCalcBasics.getId());
        if (_calcBasics == null) {
            _sessionController.logMessage("ConcurrentUpdate [DatasetDeleted], CalcBasicsPsy: " + modifiedCalcBasics.getId());
            Utils.navigate(Pages.CalculationHospitalSummary.URL());
            return Utils.getMessage("msgDatasetDeleted");
        }
        if (_calcBasics.isSealed()) {
            _sessionController.logMessage("ConcurrentUpdate [DatasetSealed], CalcBasicsPsy: " + modifiedCalcBasics.getId());
            Utils.navigate(Pages.CalculationHospitalSummary.URL());
            return Utils.getMessage("msgDatasetSealed");
        }
        Map<String, FieldValues> differencesPartner = getDifferencesPartner(getExcludedTypes());
        Map<String, FieldValues> differencesUser = getDifferencesUser(modifiedCalcBasics, getExcludedTypes());

        List<String> collisions = updateFields(differencesUser, differencesPartner, modifiedCalcBasics);

        Map<String, String> documentationFields = DocumentationUtil.getFieldTranslationMap(_calcBasics);

        String msgKey = collisions.isEmpty() ? "msgMergeOk" : "msgMergeCollision";
        _sessionController.logMessage("ConcurrentUpdate [" + msgKey.substring(3) + "], CalcBasicsDrg: " + modifiedCalcBasics.getId());
        String msg = Utils.getMessage(msgKey);
        for (String fieldName : collisions) {
            msg += "\r\n### " + documentationFields.get(fieldName) + " ###";
        }
        for (String fieldName : differencesPartner.keySet()) {
            msg += "\r\n" + documentationFields.get(fieldName);
        }
        if (!_calcBasics.isSealed()) {
            _calcBasics = _calcFacade.saveCalcBasicsPepp(_calcBasics);
        }
        return msg;
    }

    private List<Class> getExcludedTypes() {
        List<Class> excludedTypes = new ArrayList<>();
        excludedTypes.add(Date.class);
        return excludedTypes;
    }

    private Map<String, FieldValues> getDifferencesUser(PeppCalcBasics modifiedCalcBasics, List<Class> excludedTypes) {
        Map<String, FieldValues> differencesUser = ObjectUtils.getDifferences(_baseLine, modifiedCalcBasics, excludedTypes);
        differencesUser.remove("_statusId");
        return differencesUser;
    }

    private Map<String, FieldValues> getDifferencesPartner(List<Class> excludedTypes) {
        Map<String, FieldValues> differencesPartner = ObjectUtils.getDifferences(_baseLine, _calcBasics, excludedTypes);
        differencesPartner.remove("_statusId");
        differencesPartner.remove("_version");
        return differencesPartner;
    }

    private List<String> updateFields(Map<String, FieldValues> differencesUser,
            Map<String, FieldValues> differencesPartner, PeppCalcBasics modifiedCalcBasics) {
        List<String> collisions = new ArrayList<>();
        for (String fieldName : differencesUser.keySet()) {
            if (differencesPartner.containsKey(fieldName) || _calcBasics.isSealed()) {
                collisions.add(fieldName);
                differencesPartner.remove(fieldName);
                continue;
            }
            FieldValues fieldValues = differencesUser.get(fieldName);
            Field field = fieldValues.getField();
            ObjectUtils.copyFieldValue(field, modifiedCalcBasics, _calcBasics);
        }
        return collisions;
    }

    private void setModifiedInfo() {
        _calcBasics.setLastChanged(Calendar.getInstance().getTime());
        _calcBasics.setAccountIdLastChange(_sessionController.getAccountId());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        return isSendEnabled()
                && _accessManager.isSealedEnabled(Feature.CALCULATION_HOSPITAL,
                        _calcBasics.getStatus(),
                        _calcBasics.getAccountId(),
                        _calcBasics.getIk());
    }

    private boolean isSendEnabled() {
        return _appTools.isEnabled(ConfigKey.IsCalculationBasicsPsySendEnabled);
    }

    public boolean isApprovalRequestEnabled() {
        return isSendEnabled()
                && _accessManager.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL,
                        _calcBasics.getStatus(),
                        _calcBasics.getAccountId(),
                        _calcBasics.getIk());
    }

    public boolean isRequestCorrectionEnabled() {
        return isSendEnabled()
                && _accessManager.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL,
                        _calcBasics.getStatus(),
                        _calcBasics.getAccountId(),
                        _calcBasics.getIk());
    }

    public boolean isTakeEnabled() {
        return false;
        // todo: do not allow consultant
        //return _accessManager.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isCopyForResendAllowed() {
        if (_calcBasics.getStatusId() < 10 || !isSendEnabled()) {
            return false;
        }
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return false;
        }
        return !_calcFacade.existActiveCalcBasicsPsy(_calcBasics.getIk());
    }

    @SuppressWarnings("unchecked")
    public void copyForResend() {
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

        // do not set current account: _calcBasics.setAccountId(_sessionController.getAccountId());
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
        saveData(false);

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
        saveData(false);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _calcBasics.setAccountId(_sessionController.getAccountId());
        saveData(false);
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Address">
    private List<SelectItem> _ikItems;

    public List<SelectItem> getIks() {
        if (_ikItems == null) {
            //Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
            Set<Integer> iks = _calcFacade.obtainIks4NewBasicsPepp(_sessionController.getAccountId(), year, testMode);
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
            Utils.showMessageInBrowser("Bitte beachten Sie, dass die Erfassung der Rüstzeit als "
                    + "Einheitswert keine leistungsgerechte Verteilung der Kosten gewährleistet.");
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
        _calcBasics.deleteCostCenters(costCenterId);
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

    public void deleteKgpMedInfraList(int costTypeId) {
        _calcBasics.deleteKgpMedInfraList(costTypeId);
    }

    public void deleteMedInfra(KgpListMedInfra mif) {
        _calcBasics.getKgpMedInfraList().remove(mif);
    }

    @Inject private DataImporterPool importerPool;

    public DataImporter<?, ?> getImporter(String importerName) {
        return importerPool.getDataImporter(importerName.toLowerCase());
    }

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
        KGPListTherapy result = new KGPListTherapy(_calcBasics.getId(), costCenterId);
        _calcBasics.getTherapies().add(result);
    }

    public void clearTherapyCosts() {
        _calcBasics.clearTheapies();
    }

    public void addStationAlternative() {
        KGPListStationAlternative sa = new KGPListStationAlternative(_calcBasics.getId());
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

    public void clearStationServiceCosts() {
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
        KGPListRadiologyLaboratory item = new KGPListRadiologyLaboratory(_calcBasics.getId(), costCenter);
        _calcBasics.getRadiologyLaboratories().add(item);
    }

    public void clearRadiologyLaboratory(int costCenter) {
        _calcBasics.clearRadiologyLaboratory(costCenter);
    }

    public List<SelectItem> getOccupancyItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(-1, "Bitte wählen..."));
        items.add(new SelectItem(1, "vollstationär"));
        items.add(new SelectItem(2, "teilstationär"));
        items.add(new SelectItem(3, "voll- und teilstationär"));

        return items;
    }
}
