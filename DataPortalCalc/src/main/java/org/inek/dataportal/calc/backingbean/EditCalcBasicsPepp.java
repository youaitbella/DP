package org.inek.dataportal.calc.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.calc.entities.psy.*;
import org.inek.dataportal.calc.facades.CalcPsyFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.ObjectComparer;
import org.inek.dataportal.common.helper.ObjectCopier;
import org.inek.dataportal.common.helper.TransferFileCreator;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.FieldValues;
import org.inek.dataportal.common.helper.structures.MessageContainer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.common.utils.ValueLists;

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
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@ViewScoped
@SuppressWarnings("PMD")
public class EditCalcBasicsPepp extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("EditCalcBasicsPepp");
    private static final int ONE_HOUR = 3600;

    private AccessManager _accessManager;
    private SessionController _sessionController;
    private CalcPsyFacade _calcFacade;
    private ApplicationTools _appTools;
    private ValueLists _valueLists;
    private DataImporterPool _importerPool;
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

    public EditCalcBasicsPepp(){}
    @Inject
    public EditCalcBasicsPepp(AccessManager accessManager,
                              SessionController sessionController,
                              CalcPsyFacade calcFacade,
                              ApplicationTools appTools,
                              ValueLists valueLists,
                              DataImporterPool importerPool){
        _accessManager = accessManager;
        _sessionController = sessionController;
        _calcFacade = calcFacade;
        _appTools = appTools;
        _valueLists = valueLists;
        _importerPool = importerPool;
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
            _calcBasics = newCalcBasicsPepp();
            Utils.navigate(Pages.Error.RedirectURL());
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

        preloadServiceProvision(calcBasics);
        ensureOverviewPersonal(_calcFacade, calcBasics);
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
        if (getIks().size() == 1) {
            calcBasics.setIk((int) getIks().get(0).getValue());
        }

        ensureOverviewPersonal(_calcFacade, calcBasics);

        retrievePriorData(calcBasics);
        preloadData(calcBasics);
        return calcBasics;
    }

    private void ensureOverviewPersonal(CalcPsyFacade calcPsyFacade, PeppCalcBasics calcBasics) {
        if (calcBasics.getOverviewPersonals().size() > 0) {
            return;
        }

        for (KGPListOverviewPersonalType spt : calcPsyFacade.retrieveOverviewPersonalTypes(calcBasics.getDataYear())) {
            KGPListOverviewPersonal op = new KGPListOverviewPersonal(calcBasics, spt);
            calcBasics.addOverviewPersonal(op);
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
        addTopic("TopicCalcOverviewPersonal", Pages.CalcPeppOverviewPersonal.URL());
        addTopic("TopicCalcExternalServiceProvision", Pages.CalcPeppExternalServiceProvision.URL());
        addTopic("TopicCalcRadiology", Pages.CalcPeppRadiology.URL());
        addTopic("TopicCalcLaboratory", Pages.CalcPeppLaboratory.URL());
        addTopic("TopicCalcDiagnosticScope", Pages.CalcPeppDiagnosticScope.URL());
        addTopic("TopicCalcTherapeuticScope", Pages.CalcPeppTherapeuticScope.URL());
        addTopic("TopicCalcPatientAdmission", Pages.CalcPeppPatientAdmission.URL());
        addTopic("lblCalcTherapyScope", Pages.CalcPeppTherapyScope.URL());
        addTopic("TopicCalcStationEquivalentTreatment", Pages.CalcStationEquivalentTreatment.URL());
        addTopic("TopicCalcStation", Pages.CalcPeppStation.URL());
        addTopic("TopicCalcMedicalInfrastructure", Pages.CalcPeppMedicalInfrastructure.URL());
        addTopic("TopicCalcNonMedicalInfrastructure", Pages.CalcPeppNonMedicalInfrastructure.URL());
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
        if (!isReadOnly()) {
            saveData(false);
        }
    }

    public String save() {
        return saveData(true);
    }

    public String saveData(boolean showSaveMessage) {
        _calcBasics.removeEmptyServiceProvisions();

        if (_baseLine != null && ObjectComparer.getDifferences(_baseLine, _calcBasics, null).isEmpty()) {
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
            _sessionController.logMessage("ConcurrentUpdate [DatasetDeleted], CalcBasicsPsy: " + modifiedCalcBasics.
                    getId());
            Utils.navigate(Pages.CalculationHospitalSummary.URL());
            return Utils.getMessage("msgDatasetDeleted");
        }
        if (_calcBasics.isSealed()) {
            _sessionController.logMessage("ConcurrentUpdate [DatasetSealed], CalcBasicsPsy: " + modifiedCalcBasics.
                    getId());
            Utils.navigate(Pages.CalculationHospitalSummary.URL());
            return Utils.getMessage("msgDatasetSealed");
        }
        Map<String, FieldValues> differencesPartner = getDifferencesPartner(getExcludedTypes());
        Map<String, FieldValues> differencesUser = getDifferencesUser(modifiedCalcBasics, getExcludedTypes());

        List<String> collisions = updateFields(differencesUser, differencesPartner, modifiedCalcBasics);

        Map<String, String> documentationFields = DocumentationUtil.getFieldTranslationMap(_calcBasics);

        String msgKey = collisions.isEmpty() ? "msgMergeOk" : "msgMergeCollision";
        _sessionController.
                logMessage("ConcurrentUpdate [" + msgKey.substring(3) + "], CalcBasicsDrg: " + modifiedCalcBasics.
                        getId());
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
        Map<String, FieldValues> differencesUser = ObjectComparer.getDifferences(_baseLine, modifiedCalcBasics, excludedTypes);
        return differencesUser;
    }

    private Map<String, FieldValues> getDifferencesPartner(List<Class> excludedTypes) {
        Map<String, FieldValues> differencesPartner = ObjectComparer.getDifferences(_baseLine, _calcBasics, excludedTypes);
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
            ObjectCopier.copyFieldValue(field, modifiedCalcBasics, _calcBasics);
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

        TransferFileCreator.createObjectTransferFile(_sessionController, _calcBasics, _calcBasics.getIk(), "KGP");

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
        try {
            _sessionController.requestApproval(_calcBasics.getIk(), Feature.CALCULATION_HOSPITAL);
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
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
    public List<SelectItem> getIks() {
        boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
        int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        Set<Integer> iks = _calcFacade.obtainIks4NewBasicsPepp(_sessionController.getAccountId(), year, testMode);

        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.CALCULATION_HOSPITAL);
        iks.removeIf(ik -> !allowedIks.contains(ik));

        List<SelectItem> ikItems = new ArrayList<>();
        for (int ik : iks) {
            ikItems.add(new SelectItem(ik));
        }
        return ikItems;
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

    public DataImporter<?, ?> getImporter(String importerName) {
        return _importerPool.getDataImporter(importerName.toLowerCase());
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
