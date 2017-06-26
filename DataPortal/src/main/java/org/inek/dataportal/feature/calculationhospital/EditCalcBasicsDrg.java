package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Document;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.drg.DrgCalcBasics;
import org.inek.dataportal.entities.calc.drg.DrgContentText;
import org.inek.dataportal.entities.calc.drg.DrgDelimitationFact;
import org.inek.dataportal.entities.calc.drg.DrgHeaderText;
import org.inek.dataportal.entities.calc.drg.DrgNeonatData;
import org.inek.dataportal.entities.calc.drg.KGLDocument;
import org.inek.dataportal.entities.calc.drg.KGLListCentralFocus;
import org.inek.dataportal.entities.calc.drg.KGLListContentTextOps;
import org.inek.dataportal.entities.calc.drg.KGLListCostCenter;
import org.inek.dataportal.entities.calc.drg.KGLListCostCenterCost;
import org.inek.dataportal.entities.calc.drg.KGLListEndoscopyAmbulant;
import org.inek.dataportal.entities.calc.drg.KGLListEndoscopyDifferential;
import org.inek.dataportal.entities.calc.drg.KGLListIntensivStroke;
import org.inek.dataportal.entities.calc.drg.KGLListLocation;
import org.inek.dataportal.entities.calc.drg.KGLListMedInfra;
import org.inek.dataportal.entities.calc.drg.KGLListObstetricsGynecology;
import org.inek.dataportal.entities.calc.drg.KGLListRadiologyLaboratory;
import org.inek.dataportal.entities.calc.drg.KGLListServiceProvision;
import org.inek.dataportal.entities.calc.drg.KGLListServiceProvisionType;
import org.inek.dataportal.entities.calc.drg.KGLListSpecialUnit;
import org.inek.dataportal.entities.calc.drg.KGLNormalFeeContract;
import org.inek.dataportal.entities.calc.drg.KGLNormalFreelancer;
import org.inek.dataportal.entities.calc.drg.KGLNormalStationServiceDocumentation;
import org.inek.dataportal.entities.calc.drg.KGLNormalStationServiceDocumentationMinutes;
import org.inek.dataportal.entities.calc.drg.KGLPersonalAccounting;
import org.inek.dataportal.entities.calc.drg.KGLRadiologyService;
import org.inek.dataportal.entities.calc.drg.KglOpAn;
import org.inek.dataportal.entities.calc.psy.KglPkmsAlternative;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.CalcDrgFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.ObjectUtils;
import org.inek.dataportal.helper.TransferFileCreator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.FieldValues;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditCalcBasicsDrg extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger LOGGER = Logger.getLogger("EditCalcBasicsDrg");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcDrgFacade _calcDrgFacade;
    @Inject private ApplicationTools _appTools;

    private DrgCalcBasics _calcBasics;
    private DrgCalcBasics _baseLine;
    private DrgCalcBasics _priorCalcBasics;
    // </editor-fold>

    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        String id = "" + params.get("id");
        if ("new".equals(id)) {
            _calcBasics = newCalcBasicsDrg();
            _baseLine = null;
        } else if (Utils.isInteger(id)) {
            DrgCalcBasics calcBasics = loadCalcBasicsDrg(id);
            if (calcBasics.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _calcBasics = calcBasics;
            _baseLine = _calcDrgFacade.findCalcBasicsDrg(_calcBasics.getId());
            retrievePriorData(_calcBasics);
            populateDelimitationFactsIfAbsent(_calcBasics);
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
        // extend session timeout to 1 hour (to provide enough time for an upload)
        FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(3600); 
    }

    public void retrievePriorData(DrgCalcBasics calcBasics) {
        _priorCalcBasics = _calcDrgFacade.retrievePriorCalcBasics(calcBasics);

        for (KGLListCostCenterCost ccc : _priorCalcBasics.getCostCenterCosts()) {
            calcBasics.getCostCenterCosts().stream()
                    .filter((c) -> (c.getPriorId() == ccc.getPriorId()))
                    .forEachOrdered((c) -> {
                        c.setPrior(ccc);
                    });
        }
        calcBasics.setNoDeliveryRoomHabitation(_priorCalcBasics.isNoDeliveryRoomHabitation());
    }

    public void ikChanged() {
        retrievePriorData(_calcBasics);
        preloadData(_calcBasics);
    }

    private void preloadData(DrgCalcBasics calcBasics) {
        KglOpAn opAn = new KglOpAn(calcBasics.getId(), _priorCalcBasics.getOpAn());
        calcBasics.setOpAn(opAn);

        preloadLocations(calcBasics);
        preloadCentralFocuses(calcBasics);
        calcBasics.getDelimitationFacts().clear();
        populateDelimitationFactsIfAbsent(calcBasics);
        preloadPersonalAccounting(calcBasics);
        preloadRadiologyAndLab(calcBasics);
        preloadObstetricsGynecologies(calcBasics);
        preloadNormalWard(calcBasics);
        initServiceProvision(calcBasics);
        // cardiology
        calcBasics.setCardiology(_priorCalcBasics.isCardiology());
        calcBasics.setCardiologyCaseCnt(_priorCalcBasics.getCardiologyCaseCnt());
        calcBasics.setCardiologyRoomCnt(_priorCalcBasics.getCardiologyRoomCnt());
        // endoscopy
        calcBasics.setEndoscopy(_priorCalcBasics.isEndoscopy());
        calcBasics.setEndoscopyCaseCnt(_priorCalcBasics.getEndoscopyCaseCnt());
        calcBasics.setEndoscopyRoomCnt(_priorCalcBasics.getEndoscopyRoomCnt());
        // maternity
        calcBasics.setGynecology(_priorCalcBasics.isGynecology());
        calcBasics.setObstetrical(_priorCalcBasics.isObstetrical());
        preloadNeonat(calcBasics);
        // MedicalInfrastructure
        calcBasics.setDescMedicalInfra(_priorCalcBasics.getIblvMethodMedInfra() == 0);
        calcBasics.setOtherMethodMedInfra(_priorCalcBasics.getOtherMethodMedInfra());
        calcBasics.setIblvMethodMedInfra(_priorCalcBasics.getIblvMethodMedInfra());
        preloadNormalWardServiceDocumentation(calcBasics);
    }

    private void preloadNormalWardServiceDocumentation(DrgCalcBasics calcBasics) {
        calcBasics.getNormalStationServiceDocumentations().clear();
        for (DrgContentText ct : getNormalWardServiceDocHeaders()) {
            KGLNormalStationServiceDocumentation add = new KGLNormalStationServiceDocumentation();
            add.setContentText(ct);
            add.setBaseInformationId(calcBasics.getId());
            for (KGLNormalStationServiceDocumentation addPrior : _priorCalcBasics.getNormalStationServiceDocumentations()) {
                if (add.getContentTextId() == addPrior.getContentTextId()) {
                    add.setUsed(addPrior.isUsed());
                    break;
                }
            }
            calcBasics.getNormalStationServiceDocumentations().add(add);
        }
        calcBasics.getCostCenterCosts().clear();
        _priorCalcBasics
                .getCostCenterCosts()
                .stream()
                .map((ccc) -> {
                    KGLListCostCenterCost c = new KGLListCostCenterCost();
                    c.setPrior(ccc);
                    c.setCostCenterText(ccc.getCostCenterText());
                    c.setCostCenter(ccc.getCostCenter());
                    c.setPriorId(ccc.getPriorId());
                    c.setDepartmentKey(ccc.getDepartmentKey());
                    return c;
                }).forEachOrdered((c) -> {
                    calcBasics.getCostCenterCosts().add(c);
                });
    }

    private void preloadNormalWard(DrgCalcBasics calcBasics) {
        calcBasics.setNormalFreelancing(_priorCalcBasics.isNormalFreelancing());
        calcBasics.getNormalFreelancers().clear();
        for (KGLNormalFreelancer pnf : _priorCalcBasics.getNormalFreelancers()) {
            KGLNormalFreelancer nf = new KGLNormalFreelancer(calcBasics.getId());
            nf.setId(-1);
            nf.setAmount(pnf.getAmount());
            nf.setCostType1(pnf.isCostType1());
            nf.setCostType6c(pnf.isCostType6c());
            nf.setDivision(pnf.getDivision());
            nf.setFullVigorCnt(pnf.getFullVigorCnt());
            calcBasics.getNormalFreelancers().add(nf);
        }
        calcBasics.getNormalFeeContracts().clear();
        for (KGLNormalFeeContract pfc : _priorCalcBasics.getNormalFeeContracts()) {
            KGLNormalFeeContract fc = new KGLNormalFeeContract();
            fc.setId(-1);
            fc.setBaseInformationId(calcBasics.getId());
            fc.setCaseCnt(pfc.getCaseCnt());
            fc.setDivision(pfc.getDivision());
            fc.setDepartmentKey(pfc.getDepartmentKey());
            calcBasics.getNormalFeeContracts().add(fc);
        }
    }

    private void preloadObstetricsGynecologies(DrgCalcBasics calcBasics) {
        calcBasics.getObstetricsGynecologies().clear();
        for (KGLListObstetricsGynecology pObst : _priorCalcBasics.getObstetricsGynecologies()) {
            KGLListObstetricsGynecology obst = new KGLListObstetricsGynecology();
            obst.setBaseInformationId(calcBasics.getId());
            obst.setCostCenterText(pObst.getCostCenterText());
            obst.setCostTypeId(pObst.getCostTypeId());
            calcBasics.getObstetricsGynecologies().add(obst);
        }
    }

    private void preloadRadiologyAndLab(DrgCalcBasics calcBasics) {
        calcBasics.getRadiologyLaboratories().clear();
        for (KGLListRadiologyLaboratory prl : _priorCalcBasics.getRadiologyLaboratories()) {
            KGLListRadiologyLaboratory rl = new KGLListRadiologyLaboratory();
            rl.setId(-1);
            rl.setBaseInformationId(calcBasics.getId());
            rl.setAmountPost(prl.getAmountPost());
            rl.setAmountPre(prl.getAmountPre());
            rl.setCostCenterId(prl.getCostCenterId());
            rl.setCostCenterNumber(prl.getCostCenterNumber());
            rl.setCostCenterText(prl.getCostCenterText());
            rl.setDescription(prl.getDescription());
            rl.setServiceDocDKG(prl.isServiceDocDKG());
            rl.setServiceDocDif(prl.isServiceDocDif());
            rl.setServiceDocEBM(prl.isServiceDocEBM());
            rl.setServiceDocGOA(prl.isServiceDocGOA());
            rl.setServiceDocHome(prl.isServiceDocHome());
            rl.setServiceVolumePost(prl.getServiceVolumePost());
            rl.setServiceVolumePre(prl.getServiceVolumePre());
            calcBasics.getRadiologyLaboratories().add(rl);
        }
    }

    private void preloadPersonalAccounting(DrgCalcBasics calcBasics) {
        calcBasics.setPersonalAccountingDescription(_priorCalcBasics.getPersonalAccountingDescription());

        ensurePersonalAccountingData(calcBasics);
        for (KGLPersonalAccounting ppa : _priorCalcBasics.getPersonalAccountings()) {
            for (KGLPersonalAccounting pa : calcBasics.getPersonalAccountings()) {
                if (ppa.getCostTypeId() == pa.getCostTypeId()) {
                    pa.setPriorCostAmount(ppa.getAmount());
                    pa.setCostTypeId(ppa.getCostTypeId());
                    pa.setExpertRating(ppa.isExpertRating());
                    pa.setOther(ppa.isOther());
                    pa.setServiceEvaluation(ppa.isServiceEvaluation());
                    pa.setServiceStatistic(ppa.isServiceStatistic());
                    pa.setStaffEvaluation(ppa.isStaffEvaluation());
                    pa.setStaffRecording(ppa.isStaffRecording());
                }
            }
        }
    }

    private void preloadCentralFocuses(DrgCalcBasics calcBasics) {
        calcBasics.getCentralFocuses().clear();
        calcBasics.setCentralFocus(_priorCalcBasics.isCentralFocus());
        for (KGLListCentralFocus centralFocus : _priorCalcBasics.getCentralFocuses()) {
            KGLListCentralFocus cf = new KGLListCentralFocus();
            cf.setId(-1);
            cf.setBaseInformationId(calcBasics.getId());
            cf.setCaseCnt(0);
            cf.setInfraCost(0);
            cf.setMaterialcost(0);
            cf.setRemunerationAmount(0);
            cf.setRemunerationKey("");
            cf.setPersonalCost(0);
            cf.setText(centralFocus.getText());
            calcBasics.getCentralFocuses().add(cf);
        }
    }

    private void preloadLocations(DrgCalcBasics calcBasics) {
        calcBasics.setLocationCnt(_priorCalcBasics.getLocationCnt());
        calcBasics.setDifLocationSupply(_priorCalcBasics.isDifLocationSupply());
        calcBasics.setSpecialUnit(_priorCalcBasics.isSpecialUnit());
        calcBasics.getLocations().clear();
        for (KGLListLocation location : _priorCalcBasics.getLocations()) {
            KGLListLocation loc = new KGLListLocation();
            loc.setId(-1);
            loc.setBaseInformationId(calcBasics.getId());
            loc.setLocation(location.getLocation());
            loc.setLocationNo(location.getLocationNo());
            calcBasics.getLocations().add(loc);
        }
    }

    private void populateDelimitationFactsIfAbsent(DrgCalcBasics calcBasics) {
        if (!calcBasics.getDelimitationFacts().isEmpty()) {
            return;
        }
        if (calcBasics.getId() > 0) {
            // This should not be. But sometimes we lost the delimitationFacts...
            LOGGER.log(Level.WARNING, "Populate DRG DelimitationFacts for existing data: Id = {0}", calcBasics.getId());
        }
        for (DrgContentText ct : _calcDrgFacade.retrieveContentTexts(1, calcBasics.getDataYear())) {
            DrgDelimitationFact df = new DrgDelimitationFact();
            df.setBaseInformationId(calcBasics.getId());
            df.setContentText(ct);
            df.setUsed(getPriorDelimitationFact(ct.getId()).isUsed());
            calcBasics.getDelimitationFacts().add(df);
        }
    }

    private void preloadNeonat(DrgCalcBasics calcBasics) {
        calcBasics.setNeonatLvl(_priorCalcBasics.getNeonatLvl());

        int headerIdQuality = _calcDrgFacade.retrieveHeaderTexts(calcBasics.getDataYear(), 20, 0).get(0).getId();
        _priorCalcBasics.getNeonateData().stream().filter(old -> old.getContentText().getHeaderTextId() == headerIdQuality)
                .forEach(old -> {
                    Optional<DrgNeonatData> optDat = calcBasics.getNeonateData().stream()
                            .filter(nd -> nd.getContentTextId() == old.getContentTextId()).findFirst();
                    if (optDat.isPresent()) {
                        if (old.getData() == null) {
                            optDat.get().setData(new BigDecimal(0));
                        } else {
                            optDat.get().setData(old.getData());
                        }
                    }
                });
    }

    private DrgCalcBasics loadCalcBasicsDrg(String idObject) {
        int id = Integer.parseInt(idObject);
        DrgCalcBasics calcBasics = _calcDrgFacade.findCalcBasicsDrg(id);
        if (hasSufficientRights(calcBasics)) {
            return calcBasics;
        }
        return new DrgCalcBasics();
    }

    private boolean hasSufficientRights(DrgCalcBasics calcBasics) {
        if (_sessionController.isMyAccount(calcBasics.getAccountId(), false)) {
            return true;
        }
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL)) {
            return true;
        }
        return _cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, calcBasics.getStatus(), calcBasics.getAccountId());
    }

    public boolean disableRadiologyServiceCheckboxes(KGLListRadiologyLaboratory rl) {
        return rl.isServiceDocDKG() || rl.isServiceDocDif() || rl.isServiceDocEBM() || rl.isServiceDocGOA() || rl.isServiceDocHome();
    }

    public List<KGLListRadiologyLaboratory> getLaboratories() {
        return getRadLab(10);
    }

    public List<KGLListRadiologyLaboratory> getRadiologies() {
        return getRadLab(9);
    }

    private List<KGLListRadiologyLaboratory> getRadLab(int costCenterId) {
        return _calcBasics.getRadiologyLaboratories().stream().filter(c -> c.getCostCenterId() == costCenterId).collect(Collectors.toList());
    }

    private DrgCalcBasics newCalcBasicsDrg() {
        Account account = _sessionController.getAccount();
        DrgCalcBasics calcBasics = new DrgCalcBasics();
        calcBasics.setAccountId(account.getId());
        calcBasics.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        calcBasics.getMedInfras().add(new KGLListMedInfra(calcBasics.getId(), 170));
        calcBasics.getMedInfras().add(new KGLListMedInfra(calcBasics.getId(), 180));

        if (getIks().size() == 1) {
            calcBasics.setIk((int) getIks().get(0).getValue());
        }
        ensureNeonateData(calcBasics);
        ensureRadiologyServiceData(calcBasics);
        retrievePriorData(calcBasics);
        preloadData(calcBasics);
        KGLNormalStationServiceDocumentationMinutes min = new KGLNormalStationServiceDocumentationMinutes();
        min.setBaseInformationId(calcBasics.getId());
        calcBasics.getNormalStationServiceDocumentationMinutes().add(min);
        return calcBasics;
    }

    private void ensurePersonalAccountingData(DrgCalcBasics calcBasics) {
        calcBasics.getPersonalAccountings().clear();
        calcBasics.getPersonalAccountings().add(new KGLPersonalAccounting(110, 0));
        calcBasics.getPersonalAccountings().add(new KGLPersonalAccounting(120, 0));
        calcBasics.getPersonalAccountings().add(new KGLPersonalAccounting(130, 0));
    }

    private void ensureRadiologyServiceData(DrgCalcBasics calcBasics) {
        for (DrgContentText ct : _calcDrgFacade.findAllCalcContentTexts()) {
            if (ct.getHeaderTextId() == 12) {
                KGLRadiologyService rs = new KGLRadiologyService();
                rs.setBaseInformationId(calcBasics.getId());
                rs.setRsContentTextID(ct.getId());
                KGLListContentTextOps ops = _calcDrgFacade.findOpsCodeByContentTextId(ct.getId());
                if (ops != null) {
                    rs.setOpsCode(ops.getOpsCode());
                }
                calcBasics.getRadiologyServices().add(rs);
            }
        }
    }

    private void ensureNeonateData(DrgCalcBasics calcBasics) {
        if (!calcBasics.getNeonateData().isEmpty()) {
            return;
        }
        List<Integer> headerIds = _calcDrgFacade.retrieveHeaderTexts(calcBasics.getDataYear(), 20, -1)
                .stream()
                .map(ht -> ht.getId())
                .collect(Collectors.toList());
        List<DrgContentText> contentTexts = _calcDrgFacade.retrieveContentTexts(headerIds, calcBasics.getDataYear());
        for (DrgContentText contentText : contentTexts) {
            DrgNeonatData data = new DrgNeonatData();
            data.setContentTextId(contentText.getId());
            data.setContentText(contentText);
            data.setBaseInformationId(calcBasics.getId());
            calcBasics.getNeonateData().add(data);
        }
    }

    private void initServiceProvision(DrgCalcBasics calcBasics) {
        calcBasics.getServiceProvisions().clear();

        List<KGLListServiceProvisionType> provisionTypes = _calcDrgFacade.retrieveServiceProvisionTypes(calcBasics.getDataYear(), true);

        loadMandatoryServiceProvision(provisionTypes, calcBasics);
        addMissingPriorProvisionTypes(provisionTypes, calcBasics);

    }

    private void loadMandatoryServiceProvision(List<KGLListServiceProvisionType> provisionTypes, DrgCalcBasics calcBasics) {
        // always populate mandatory entries
        for (KGLListServiceProvisionType provisionType : provisionTypes) {
            KGLListServiceProvision data = new KGLListServiceProvision(calcBasics.getId());
            data.setServiceProvisionType(provisionType);
            calcBasics.getServiceProvisions().add(data);
        }
    }

    private void addMissingPriorProvisionTypes(List<KGLListServiceProvisionType> provisionTypes, DrgCalcBasics calcBasics) {
        // get prior values and additional entries
        for (KGLListServiceProvision prior : _priorCalcBasics.getServiceProvisions()) {
            Optional<KGLListServiceProvision> currentOpt = calcBasics.getServiceProvisions().stream()
                    .filter(sp -> sp.getServiceProvisionTypeId() == prior.getServiceProvisionTypeId()).findAny();
            if (currentOpt.isPresent()) {
                KGLListServiceProvision current = currentOpt.get();
                current.setProvidedTypeId(prior.getProvidedTypeId());
            } else if (!prior.isEmpty()) {
                // take old entries only, if they contain values!
                KGLListServiceProvision newSP = new KGLListServiceProvision(calcBasics.getId());
                newSP.setServiceProvisionType(prior.getServiceProvisionType());
                newSP.setProvidedTypeId(prior.getProvidedTypeId());
                calcBasics.getServiceProvisions().add(newSP);
            }
        }
    }

    // used by page only
    public void addCostCenterCosts() {
        KGLListCostCenterCost ccc = new KGLListCostCenterCost();
        ccc.setBaseInformationId(_calcBasics.getId());
        ccc.setId(-1);
        _calcBasics.getCostCenterCosts().add(ccc);
    }

    public void deleteCostCenterCosts(KGLListCostCenterCost x) {
        _calcBasics.getCostCenterCosts().remove(x);
    }

    public void deleteAllCostCenterCosts() {
        _calcBasics.getCostCenterCosts().clear();
    }

    public void deleteSpecialUnit(KGLListSpecialUnit su) {
        _calcBasics.getSpecialUnits().remove(su);
    }

    public void addCentralFocus() {
        KGLListCentralFocus cf = new KGLListCentralFocus();
        cf.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getCentralFocuses().add(cf);
    }

    public void deleteCentralFocus(KGLListCentralFocus cf) {
        _calcBasics.getCentralFocuses().remove(cf);
    }

    public void addLaboratory() {
        KGLListRadiologyLaboratory rl = new KGLListRadiologyLaboratory(_calcBasics.getId(), 10);
        _calcBasics.getRadiologyLaboratories().add(rl);
    }

    public void addRadiology() {
        KGLListRadiologyLaboratory rl = new KGLListRadiologyLaboratory(_calcBasics.getId(), 9);
        _calcBasics.getRadiologyLaboratories().add(rl);
    }

    public void deleteLaboratory(KGLListRadiologyLaboratory rl) {
        _calcBasics.getRadiologyLaboratories().remove(rl);
    }

    public void deleteLaboratories() {
        _calcBasics.deleteLaboratories();
    }

    public void deleteRadiologies() {
        _calcBasics.deleteRadiologies();
    }

    public void addNormalStationServiceDocMinutes() {
        KGLNormalStationServiceDocumentationMinutes min = new KGLNormalStationServiceDocumentationMinutes();
        min.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getNormalStationServiceDocumentationMinutes().add(min);
    }

    public void deleteNormalStationServiceDocMinutes(KGLNormalStationServiceDocumentationMinutes item) {
        _calcBasics.getNormalStationServiceDocumentationMinutes().remove(item);
    }

    public void addMedInfra(int costTypeId) {
        _calcBasics.addMedInfra(costTypeId);
    }

    public void deleteMedInfraItems(int costTypeId) {
        _calcBasics.deleteMedInfraItems(costTypeId);
    }

    public void addFreelancer() {
        KGLNormalFreelancer nf = new KGLNormalFreelancer(_calcBasics.getId());
        _calcBasics.getNormalFreelancers().add(nf);
    }

    public void deleteFreelancer(KGLNormalFreelancer nf) {
        _calcBasics.getNormalFreelancers().remove(nf);
    }

    public void addFeeContract() {
        KGLNormalFeeContract fc = new KGLNormalFeeContract(_calcBasics.getId());
        _calcBasics.getNormalFeeContracts().add(fc);
    }

    public void addPkmsService() {
        _calcBasics.addPkmsAlternative();
    }

    public void deletePkmsService(KglPkmsAlternative item) {
        _calcBasics.removePkmsAlternative(item);
    }

    public void deleteFeeContract(KGLNormalFeeContract fc) {
        _calcBasics.getNormalFeeContracts().remove(fc);
    }

    public List<KGLListMedInfra> getMedInfra(int costType) {
        return _calcBasics.getMedInfras()
                .stream()
                .filter(mi -> mi.getCostTypeId() == costType)
                .collect(Collectors.toList());
    }

    public void deleteMedInfra(KGLListMedInfra mif) {
        _calcBasics.getMedInfras().remove(mif);
    }

    public List<KGLListEndoscopyDifferential> getEndoscopyDifferentials() {
        List<KGLListEndoscopyDifferential> result = _calcBasics.getEndoscopyDifferentials();
        if (result.isEmpty()) {
            KGLListEndoscopyDifferential item = new KGLListEndoscopyDifferential();
            item.setBaseInformationId(_calcBasics.getId());
            result.add(item);
        }
        return result;
    }

    public List<SelectItem> getEndoscopyAmbulantTypes() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(-1, "Bitte wählen..."));
        items.add(new SelectItem(1, "DGVS"));
        items.add(new SelectItem(2, "Leistungszeit"));
        items.add(new SelectItem(3, "Punktesystem"));
        items.add(new SelectItem(4, "Sonstige"));
        return items;
    }

    public List<KGLListEndoscopyDifferential> addEndoscopyDifferentials() {
        List<KGLListEndoscopyDifferential> result = _calcBasics.getEndoscopyDifferentials();
        KGLListEndoscopyDifferential item = new KGLListEndoscopyDifferential();
        item.setBaseInformationId(_calcBasics.getId());
        result.add(item);
        return result;
    }

    public void addEndoscopyAmbulant() {
        KGLListEndoscopyAmbulant a = new KGLListEndoscopyAmbulant();
        a.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getEndoscopyAmbulant().add(a);
    }

    public void deleteEndoscopyAmbulant(KGLListEndoscopyAmbulant a) {
        _calcBasics.getEndoscopyAmbulant().remove(a);
    }

    public void deleteEndoscopyDifferential(KGLListEndoscopyDifferential differential) {
        List<KGLListEndoscopyDifferential> result = _calcBasics.getEndoscopyDifferentials();
        result.remove(differential);
    }

    public List<KGLListIntensivStroke> getIntensivStroke(int intensiveType) {
        List<KGLListIntensivStroke> collect = _calcBasics.getIntensivStrokes().stream()
                .filter(i -> i.getIntensiveType() == intensiveType)
                .collect(Collectors.toList());
        return collect;
    }

    public void addIntensivStroke(int intensiveType) {
        KGLListIntensivStroke item = new KGLListIntensivStroke(_calcBasics.getId(), intensiveType);
        _calcBasics.getIntensivStrokes().add(item);
    }

    public void deleteIntensivStrokeItems(int intensiveType) {
        List<KGLListIntensivStroke> itemsToDelete = _calcBasics.getIntensivStrokes().stream()
                .filter(i -> i.getIntensiveType() == intensiveType).collect(Collectors.toList());
        _calcBasics.getIntensivStrokes().removeAll(itemsToDelete);
    }

    public void deleteIntensivStroke(KGLListIntensivStroke item) {
        _calcBasics.getIntensivStrokes().remove(item);
    }

    public int getSumIntensivStrokeWeighted() {
        List<KGLListIntensivStroke> intensivStrokes = _calcBasics.getIntensivStrokes();
        int result = 0;
        for (KGLListIntensivStroke intensivStroke : intensivStrokes) {
            result += intensivStroke.getIntensivHoursWeighted();
        }
        return result;
    }

    public int getSumIntensivStrokeNotWeighted() {
        List<KGLListIntensivStroke> intensivStrokes = _calcBasics.getIntensivStrokes();
        int result = 0;
        for (KGLListIntensivStroke intensivStroke : intensivStrokes) {
            result += intensivStroke.getIntensivHoursNotweighted();
        }
        return result;
    }

    @SuppressWarnings("MultipleStringLiterals")
    private static final String HEADLINE_RADIOLOGY = "KostenstelleNummer;KostenstelleName;"
            + "Leistungsdokumentation;Beschreibung;LeistungsvolumenVor;"
            + "KostenvolumenVor;LeistungsvolumenNach;KostenvolumenNach";

    public void downloadRadiologyTemplate() {
        Utils.downloadText(HEADLINE_RADIOLOGY + "\n", "Radiology.csv");
    }

    @SuppressWarnings("MultipleStringLiterals")
    private static final String HEADLINE_LABORATY = "KostenstelleNummer;KostenstelleName;"
            + "Leistungsdokumentation;Beschreibung;LeistungsvolumenVor;"
            + "KostenvolumenVor;KostenvolumenNach";

    public void downloadLaboratoryTemplate() {
        Utils.downloadText(HEADLINE_LABORATY + "\n", "Laboratory.csv");
    }

    public List<DrgContentText> getNormalWardServiceDocHeaders() {
        return _calcDrgFacade.retrieveContentTexts(13, Calendar.getInstance().get(Calendar.YEAR));
    }

    public List<Double> getCostCenterCostsSums() {
        List<Double> l = new ArrayList<>();
        Integer sumBeds = 0;
        Integer sumPprWeight = 0;
        Double sumMedicalService = 0.0;
        Double sumNurseService = 0.0;
        Double sumFuncService = 0.0;
        Integer sumMedicalService2 = 0;
        Integer sumNurseService2 = 0;
        Integer sumFuncService2 = 0;
        Integer sumSharedMedicineCost = 0;
        Integer sumSharedMedStuffCost = 0;
        Integer sumMedInfra = 0;
        Integer sumNonMedInfra = 0;
        for (KGLListCostCenterCost c : _calcBasics.getCostCenterCosts()) {
            sumBeds += c.getBedCnt();
            sumPprWeight += c.getPprWeight();
            sumMedicalService += c.getMedicalServiceCnt();
            sumNurseService += c.getNursingServiceCnt();
            sumFuncService += c.getFunctionalServiceCnt();
            sumMedicalService2 += c.getMedicalServiceAmount();
            sumNurseService2 += c.getNursingServiceAmount();
            sumFuncService2 += c.getFunctionalServiceAmount();
            sumSharedMedicineCost += c.getOverheadsMedicine();
            sumSharedMedStuffCost += c.getOverheadsMedicalGoods();
            sumMedInfra += c.getMedicalInfrastructureCost();
            sumNonMedInfra += c.getNonMedicalInfrastructureCost();
        }
        l.add((double) sumBeds);
        l.add((double) sumPprWeight);
        l.add(sumMedicalService);
        l.add(sumNurseService);
        l.add(sumFuncService);
        l.add((double) sumMedicalService2);
        l.add((double) sumNurseService2);
        l.add((double) sumFuncService2);
        l.add((double) sumSharedMedicineCost);
        l.add((double) sumSharedMedStuffCost);
        l.add((double) sumMedInfra);
        l.add((double) sumNonMedInfra);
        return l;
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
        addTopic("TopicFrontPage", Pages.CalcDrgBasics.URL());
        addTopic("TopicBasicExplanation", Pages.CalcDrgBasicExplanation.URL());
        addTopic("TopicCalcExternalServiceProvision", Pages.CalcDrgExternalServiceProvision.URL());
        addTopic("TopicCalcOpAn", Pages.CalcDrgOperation.URL());
        addTopic("TopicCalcMaternityRoom", Pages.CalcDrgMaternityRoom.URL());
        addTopic("TopicCalcCardiology", Pages.CalcDrgCardiology.URL());
        addTopic("TopicCalcEndoscopy", Pages.CalcDrgEndoscopy.URL());
        addTopic("TopicCalcRadiology", Pages.CalcDrgRadiology.URL());
        addTopic("TopicCalcLaboratory", Pages.CalcDrgLaboratory.URL());
        addTopic("TopicCalcDiagnosticScope", Pages.CalcDrgDiagnosticScope.URL());
        addTopic("TopicCalcTherapeuticScope", Pages.CalcDrgTherapeuticScope.URL());
        addTopic("TopicCalcPatientAdmission", Pages.CalcDrgPatientAdmission.URL());
        addTopic("ToipicCalcNormalWard", Pages.CalcrgNormalWard.URL());
        addTopic("TopicCalcIntensiveCare", Pages.CalcDrgIntensiveCare.URL());
        addTopic("TopicCalcStrokeUnit", Pages.CalcDrgStrokeUnit.URL());
        addTopic("TopicCalcMedicalInfrastructure", Pages.CalcDrgMedicalInfrastructure.URL());
        addTopic("TopicCalcNonMedicalInfrastructure", Pages.CalcDrgNonMedicalInfrastructure.URL());
        addTopic("TopicCalcStaffCost", Pages.CalcDrgStaffCost.URL());
        addTopic("TopicCalcValvularIntervention", Pages.CalcDrgValvularIntervention.URL());
        addTopic("TopicCalcNeonatology", Pages.CalcDrgNeonatology.URL());
    }

    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_calcBasics.getAccountId(), false);
    }

    public boolean isReadOnly() {
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return true;
        }
        // todo apply rights depending on ik?
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
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

    private String saveData(boolean showSaveMessage) {
        _calcBasics.removeEmptyServiceProvisions();
        
        if (_baseLine != null && ObjectUtils.getDifferences(_baseLine, _calcBasics, null).isEmpty()) {
            // nothing is changed, but we will reload the data if changed by somebody else (as indicated by a new version)
            if (_baseLine.getVersion() != _calcDrgFacade.getCalcBasicsDrgVersion(_calcBasics.getId())) {
                _baseLine = _calcDrgFacade.findCalcBasicsDrg(_calcBasics.getId());
                _calcBasics = _calcDrgFacade.findCalcBasicsDrg(_calcBasics.getId());
            }
            return null;
        }
        setModifiedInfo();
        String msg = "";
        try {
            _calcBasics = _calcDrgFacade.saveCalcBasicsDrg(_calcBasics);
            if (!isValidId(_calcBasics.getId())) {
                return Pages.Error.RedirectURL();
            }
            if (showSaveMessage) {
                msg = Utils.getMessage("msgSave");
            }
        } catch (Exception ex) {
            if (!(ex instanceof OptimisticLockException) && !(ex.getCause() instanceof OptimisticLockException)) {
                throw ex;
            }
            msg = mergeAndReportChanges();
        }
        _baseLine = _calcDrgFacade.findCalcBasicsDrg(_calcBasics.getId());
        if (!msg.isEmpty()) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + msg.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
        }
        return null;
    }

    private String mergeAndReportChanges() {
        DrgCalcBasics modifiedCalcBasics = _calcBasics;
        _calcBasics = _calcDrgFacade.findCalcBasicsDrg(modifiedCalcBasics.getId());
        if (_calcBasics == null) {
            _sessionController.logMessage("ConcurrentUpdate [DatasetDeleted], CalcBasicsDrg: " + modifiedCalcBasics.getId());
            Utils.navigate(Pages.CalculationHospitalSummary.URL());
            return Utils.getMessage("msgDatasetDeleted");
        }
        if (_calcBasics.isSealed()) {
            _sessionController.logMessage("ConcurrentUpdate [DatasetSealed], CalcBasicsDrg: " + modifiedCalcBasics.getId());
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
            _calcBasics = _calcDrgFacade.saveCalcBasicsDrg(_calcBasics);
        }
        return msg;
    }

    private List<Class> getExcludedTypes() {
        List<Class> excludedTypes = new ArrayList<>();
        excludedTypes.add(Date.class);
        return excludedTypes;
    }

    private Map<String, FieldValues> getDifferencesUser(DrgCalcBasics modifiedCalcBasics, List<Class> excludedTypes) {
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

    private List<String> updateFields(Map<String, FieldValues> differencesUser, Map<String, 
            FieldValues> differencesPartner, DrgCalcBasics modifiedCalcBasics) {
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
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isTakeEnabled() {
        return false;
        // todo: do not allow consultant
        //return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isCopyForResendAllowed() {
        if (_calcBasics.getStatusId() < 10 || _calcBasics.getStatusId() > 20 || !_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        if (_sessionController.isInekUser(Feature.CALCULATION_HOSPITAL) && !_appTools.isEnabled(ConfigKey.TestMode)) {
            return false;
        }
        return !_calcDrgFacade.existActiveCalcBasicsDrg(_calcBasics.getIk());
    }

    @SuppressWarnings("unchecked")
    public void copyForResend() {
        _calcBasics.setStatus(WorkflowStatus.Retired);
        _calcDrgFacade.saveCalcBasicsDrg(_calcBasics);

        _calcDrgFacade.detach(_calcBasics);
        _calcBasics.setId(-1);
        _calcBasics.setStatus(WorkflowStatus.New);

        _calcDrgFacade.detach(_calcBasics.getOpAn());
        _calcBasics.getOpAn().setBaseInformationId(-1);

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
                    _calcDrgFacade.detach(object);
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
            _calcBasics = _calcDrgFacade.saveCalcBasicsDrg(_calcBasics);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Exception during setDataToNew: {0}", ex.getMessage());
        }
        _baseLine = null;
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
        CalcBasicsDrgValueCleaner.clearUnusedFields(_calcBasics);

        _calcBasics.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _calcBasics.setSealed(Calendar.getInstance().getTime());
        _calcBasics = _calcDrgFacade.saveCalcBasicsDrg(_calcBasics);

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
        MessageContainer message = CalcBasicsDrgValidator.composeMissingFieldsMessage(_calcBasics);
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
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
            Set<Integer> iks = _calcDrgFacade.obtainIks4NewBasicsDrg(_sessionController.getAccountId(), year, testMode);
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

    // <editor-fold defaultstate="collapsed" desc="Tab BasicExplanation">
    public DrgDelimitationFact getPriorDelimitationFact(int contentTextId) {
        return _priorCalcBasics.getDelimitationFacts().stream()
                .filter(f -> f.getContentTextId() == contentTextId)
                .findAny().orElse(new DrgDelimitationFact());
    }

    public List<String> getDelimitationFactsSubTitles() {
        List<String> tmp = new ArrayList<>();
        tmp.add("Personalkosten");
        tmp.add("Sachkosten");
        tmp.add("Infrastrukturkosten");
        return tmp;
    }

    public void changeLocationCount(AjaxBehaviorEvent event) {
        removeEmptyLocations();
        int diff = _calcBasics.getLocationCnt() - _calcBasics.getLocations().size();
        while (diff > 0) {
            addLocation();
            diff--;
        }
    }

    public void addLocation() {
        KGLListLocation loc = new KGLListLocation();
        loc.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getLocations().add(loc);
    }

    public void deleteLocation(KGLListLocation loc) {
        _calcBasics.getLocations().remove(loc);
    }

    private void removeEmptyLocations() {
        Iterator<KGLListLocation> iter = _calcBasics.getLocations().iterator();
        while (iter.hasNext()) {
            KGLListLocation location = iter.next();
            if (location.getLocationNo() == 0 && location.getLocation().isEmpty()) {
                iter.remove();
            }
        }
    }

    public void addSpecialUnit() {
        KGLListSpecialUnit su = new KGLListSpecialUnit();
        su.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getSpecialUnits().add(su);
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tab ServiceProvision">
    public int priorProvisionAmount(KGLListServiceProvision current) {
        Optional<KGLListServiceProvision> prior = _priorCalcBasics.getServiceProvisions().stream()
                .filter(p -> p.getServiceProvisionTypeId() == current.getServiceProvisionTypeId()).findAny();
        if (prior.isPresent()) {
            return prior.get().getAmount();
        }
        return 0;
    }

    public void addServiceProvision() {
        _calcBasics.getServiceProvisions().add(new KGLListServiceProvision(_calcBasics.getId()));
    }

    public void deleteServiceProvision(KGLListServiceProvision item) {
        _calcBasics.getServiceProvisions().remove(item);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Operation">
    public void checkOption(AjaxBehaviorEvent event) {
        HtmlSelectOneMenu component = (HtmlSelectOneMenu) event.getComponent();
        if (component.getValue().equals(3)) {
            Utils.showMessageInBrowser("Bitte beachten Sie, dass die Erfassung der Rüstzeit als Einheitswert "
                    + "keine leistungsgerechte Verteilung der Kosten gewährleistet.");
        }
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tab Diagnostics / therapy / patient admission">
    public void addCostCenter(int costCenterId) {
        KGLListCostCenter item = new KGLListCostCenter(_calcBasics.getId(), costCenterId);
        _calcBasics.getCostCenters().add(item);
    }

    public void deleteCostCenter(KGLListCostCenter item) {
        _calcBasics.getCostCenters().remove(item);
    }

    public void deleteCostCenters(int costCenterId) {
        List<KGLListCostCenter> centersToDelete = _calcBasics.getCostCenters()
                .stream()
                .filter(c -> c.getCostCenterId() == costCenterId)
                .collect(Collectors.toList());
        _calcBasics.getCostCenters().removeAll(centersToDelete);
    }

    public void downloadNormalStation() {
        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        String content = getImporter("drgNormalWard").getHeader() + "\r\n";
        for (KGLListCostCenterCost costCenterCost : _calcBasics.getCostCenterCosts()) {
            String line = costCenterCost.getCostCenterNumber() + ";"
                    + costCenterCost.getCostCenterText() + ";"
                    + costCenterCost.getDepartmentKey() + ";"
                    + costCenterCost.getDepartmentAssignment() + ";"
                    + costCenterCost.getBedCnt() + ";"
                    + costCenterCost.getCareDays() + ";"
                    + costCenterCost.getPprMinutes() + ";"
                    + costCenterCost.getPprWeight() + ";"
                    + formatter.format(costCenterCost.getMedicalServiceCnt()) + ";"
                    + formatter.format(costCenterCost.getNursingServiceCnt()) + ";"
                    + formatter.format(costCenterCost.getFunctionalServiceCnt()) + ";"
                    + costCenterCost.getMedicalServiceAmount() + ";"
                    + costCenterCost.getNursingServiceAmount() + ";"
                    + costCenterCost.getFunctionalServiceAmount() + ";"
                    + costCenterCost.getOverheadsMedicine() + ";"
                    + costCenterCost.getOverheadsMedicalGoods() + ";"
                    + costCenterCost.getMedicalInfrastructureCost() + ";"
                    + costCenterCost.getNonMedicalInfrastructureCost() + "\n";
            content += line;
        }
        Utils.downloadText(content, "Normalstation.csv");
    }

    @Inject private DataImporterPool importerPool;

    public DataImporter<?, ?> getImporter(String importerName) {
        return importerPool.getDataImporter(importerName.toLowerCase());
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tab MVI">
    public String downloadDocument(String name) {
        Document document = _calcBasics.getDocuments().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name) && d.getSheetId() == 19).findAny().orElse(null);
        if (document != null) {
            return Utils.downloadDocument(document);
        }
        return "";
    }

    public String deleteDocument(String name) {
        KGLDocument document = _calcBasics.getDocuments().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name) && d.getSheetId() == 19).findAny().orElse(null);
        if (document != null) {
            _calcBasics.getDocuments().remove(document);
        }
        return null;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Neonatology">
    public List<DrgHeaderText> getHeaders() {
        return _calcDrgFacade.retrieveHeaderTexts(_calcBasics.getDataYear(), 20, -1);
    }

    public List<DrgHeaderText> getHeaders(int type) {
        return _calcDrgFacade.retrieveHeaderTexts(Calendar.getInstance().get(Calendar.YEAR), 20, type);
    }

    public List<DrgContentText> retrieveContentTexts(int headerId) {
        return _calcDrgFacade.retrieveContentTexts(headerId, _calcBasics.getDataYear());
    }

    public List<DrgNeonatData> retrieveNeonatData(int headerId) {
        return _calcBasics.getNeonateData()
                .stream()
                .filter(d -> d.getContentText().getHeaderTextId() == headerId)
                .sorted((x, y) -> x.getContentText().getSequence() - y.getContentText().getSequence())
                .collect(Collectors.toList());
    }

    public BigDecimal priorData(int textId) {
        BigDecimal priorValue = _priorCalcBasics.getNeonateData().stream()
                .filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(BigDecimal.ZERO);
        return priorValue;
    }

    public String diffData(int textId) {
        DrgNeonatData data = _calcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).findFirst().get();
        BigDecimal priorValue = _priorCalcBasics.getNeonateData().stream()
                .filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(BigDecimal.ZERO);
        if (data.getContentText().isDiffAsPercent()) {
            return calcPercentualDiff(priorValue, data.getData());
        }
        return "" + (data.getData().subtract(priorValue));
    }
    // </editor-fold>

    public void deleteObstreticsGynecology(KGLListObstetricsGynecology item) {
        _calcBasics.getObstetricsGynecologies().remove(item);
    }

    public void addObstreticsGynecology() {
        //int seq = (int) _calcBasics.getObstetricsGynecologies().stream().mapToInt(i -> i.getId()).max().orElse(0);
        KGLListObstetricsGynecology item = new KGLListObstetricsGynecology();
        item.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getObstetricsGynecologies().add(item);
    }

    public String calcPercentualDiff(int priorValue, int currentValue) {
        if (priorValue == 0) {
            return "";
        }
        return Math.round(1000d * (currentValue - priorValue) / priorValue) / 10d + "%";
    }

    public String calcPercentualDiff(BigDecimal priorValue, BigDecimal currentValue) {
        if (priorValue == null || priorValue.doubleValue() == 0) {
            return "";
        }
        MathContext mc = new MathContext(2);
        return (currentValue.subtract(priorValue)).divide(priorValue, mc).multiply(new BigDecimal(100)).setScale(1, RoundingMode.HALF_UP) + "%";
    }

    public int getMedInfraSum(int type) {
        int sumAmount = _calcBasics.getMedInfras()
                .stream()
                .filter(m -> m.getCostTypeId() == type)
                .mapToInt(m -> m.getAmount())
                .sum();
        return sumAmount;
    }

    public boolean renderPersonalAccountingDescription() {
        for (KGLPersonalAccounting pa : _calcBasics.getPersonalAccountings()) {
            if (pa.isExpertRating() || pa.isServiceStatistic() || pa.isOther()) {
                return true;
            }
        }
        return false;
    }

    public String getContentText(int id) {
        return _calcDrgFacade.findCalcContentText(id).getText();
    }

    public KGLRadiologyService getPriorRadiologyService(int contentTextId) {
        for (KGLRadiologyService rs : _priorCalcBasics.getRadiologyServices()) {
            if (rs.getRsContentTextID() == contentTextId) {
                return rs;
            }
        }
        return new KGLRadiologyService();
    }
    
    public List<SelectItem> getRadLabServices() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(0, ""));
        items.add(new SelectItem(1, "Hauskatalog"));
        items.add(new SelectItem(2, "DKG-NT"));
        items.add(new SelectItem(3, "EBM"));
        items.add(new SelectItem(4, "GOÄ"));
        items.add(new SelectItem(5, "Sonstige"));
        return items;
    }
}
