/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
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
import javax.servlet.http.Part;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import static org.inek.dataportal.common.CooperationTools.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Document;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcContact;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.DrgContentText;
import org.inek.dataportal.entities.calc.DrgDelimitationFact;
import org.inek.dataportal.entities.calc.DrgHeaderText;
import org.inek.dataportal.entities.calc.DrgNeonatData;
import org.inek.dataportal.entities.calc.KGLListCentralFocus;
import org.inek.dataportal.entities.calc.KGLDocument;
import org.inek.dataportal.entities.calc.KGLListContentTextOps;
import org.inek.dataportal.entities.calc.KGLListCostCenter;
import org.inek.dataportal.entities.calc.KGLListCostCenterCost;
import org.inek.dataportal.entities.calc.KGLListEndoscopyAmbulant;
import org.inek.dataportal.entities.calc.KGLListEndoscopyDifferential;
import org.inek.dataportal.entities.calc.KGLListIntensivStroke;
import org.inek.dataportal.entities.calc.KGLListLocation;
import org.inek.dataportal.entities.calc.KGLListMedInfra;
import org.inek.dataportal.entities.calc.KGLListRadiologyLaboratory;
import org.inek.dataportal.entities.calc.KGLListObstetricsGynecology;
import org.inek.dataportal.entities.calc.KGLListServiceProvision;
import org.inek.dataportal.entities.calc.KGLListServiceProvisionType;
import org.inek.dataportal.entities.calc.KGLListSpecialUnit;
import org.inek.dataportal.entities.calc.KGLNormalFeeContract;
import org.inek.dataportal.entities.calc.KGLNormalFreelancer;
import org.inek.dataportal.entities.calc.KGLNormalStationServiceDocumentation;
import org.inek.dataportal.entities.calc.KGLNormalStationServiceDocumentationMinutes;
import org.inek.dataportal.entities.calc.KGLOpAn;
import org.inek.dataportal.entities.calc.KGLPKMSAlternative;
import org.inek.dataportal.entities.calc.KGLPersonalAccounting;
import org.inek.dataportal.entities.calc.KGLRadiologyService;
import org.inek.dataportal.entities.common.CostType;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.CalcFacade;
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
public class EditCalcBasicsDrg extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditCalcBasicsDrg");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private CalcFacade _calcFacade;
    @Inject ApplicationTools _appTools;
    @Inject private CustomerFacade _customerFacade;
    @Inject private CostTypeFacade _costTypeFacade;

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
            DrgCalcBasics calcBasics = loadCalcBasicsDrg(id);
            if (calcBasics.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _calcBasics = calcBasics;
            retrievePriorData(_calcBasics);
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
    }

    public void retrievePriorData(DrgCalcBasics calcBasics) {
        _priorCalcBasics = _calcFacade.retrievePriorCalcBasics(calcBasics);

        for (KGLPersonalAccounting ppa : _priorCalcBasics.getPersonalAccountings()) {
            for (KGLPersonalAccounting pa : calcBasics.getPersonalAccountings()) {
                if (ppa.getCostTypeID() == pa.getCostTypeID()) {
                    pa.setPriorCostAmount(ppa.getAmount());
                }
            }
        }
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
        _logger.info("start ikChanged");
        retrievePriorData(_calcBasics);
        preloadData(_calcBasics);
        _logger.info("end ikChanged");
    }

    private void preloadData(DrgCalcBasics calcBasics) {
        KGLOpAn opAn = new KGLOpAn(calcBasics.getId(), _priorCalcBasics.getOpAn());
        calcBasics.setOpAn(opAn);

        // Locations
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

        // Special units
        /*        
        calcBasics.getSpecialUnits().clear();
        for (KGLListSpecialUnit specialUnit : _priorCalcBasics.getSpecialUnits()) {
            specialUnit.setId(-1);
            specialUnit.setBaseInformationId(calcBasics.getId());
            calcBasics.getSpecialUnits().add(specialUnit);
        } Thumser, Vorjahreswerte sollen hier nicht geladen werden*/
        // Central focuses
        calcBasics.getCentralFocuses().clear();
        calcBasics.setCentralFocus(_priorCalcBasics.isCentralFocus());
        for (KGLListCentralFocus centralFocus : _priorCalcBasics.getCentralFocuses()) {
            KGLListCentralFocus cf = new KGLListCentralFocus();
            cf.setId(-1);
            cf.setBaseInformationID(calcBasics.getId());
            cf.setCaseCnt(0);
            cf.setInfraCost(0);
            cf.setMaterialcost(0);
            cf.setRemunerationAmount(0);
            cf.setRemunerationKey("");
            cf.setPersonalCost(0);
            cf.setText(centralFocus.getText());
            calcBasics.getCentralFocuses().add(cf);
        }

        // Delimitation facts
        calcBasics.getDelimitationFacts().clear();
        for (DrgContentText ct : _calcFacade.retrieveContentTexts(1, Calendar.getInstance().get(Calendar.YEAR))) {
            DrgDelimitationFact df = new DrgDelimitationFact();
            df.setBaseInformationId(calcBasics.getId());
            df.setContentTextId(ct.getId());
            df.setContentText(ct);
            for (DrgDelimitationFact pdf : _priorCalcBasics.getDelimitationFacts()) {
                if (df.getContentTextId() == pdf.getContentTextId()) {
                    df.setUsed(pdf.isUsed());
                }
            }
            calcBasics.getDelimitationFacts().add(df);
        }
        checkRequireInputsForDelimitationFact(calcBasics);

        // Personal Accounting
        calcBasics.getPersonalAccountings().clear();
        for (KGLPersonalAccounting ppa : _priorCalcBasics.getPersonalAccountings()) {
            KGLPersonalAccounting pa = new KGLPersonalAccounting();
            pa.setId(-1);
            pa.setBaseInformationID(calcBasics.getId());
            pa.setPriorCostAmount(ppa.getAmount());
            pa.setAmount(0);
            pa.setCostTypeID(ppa.getCostTypeID());
            pa.setExpertRating(ppa.isExpertRating());
            pa.setOther(ppa.isOther());
            pa.setServiceEvaluation(ppa.isServiceEvaluation());
            pa.setServiceStatistic(ppa.isServiceStatistic());
            pa.setStaffEvaluation(ppa.isStaffEvaluation());
            pa.setStaffRecording(ppa.isStaffRecording());
            calcBasics.getPersonalAccountings().add(pa);
        }
        ensurePersonalAccountingData(calcBasics);

        // Radiology & Laboratory
        calcBasics.getRadiologyLaboratories().clear();
        for (KGLListRadiologyLaboratory prl : _priorCalcBasics.getRadiologyLaboratories()) {
            KGLListRadiologyLaboratory rl = new KGLListRadiologyLaboratory();
            rl.setId(-1);
            rl.setBaseInformationID(calcBasics.getId());
            rl.setAmountPost(prl.getAmountPost());
            rl.setAmountPre(prl.getAmountPre());
            rl.setCostCenterID(prl.getCostCenterID());
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

        // Normal Ward
        calcBasics.setNormalFreelancing(_priorCalcBasics.isNormalFreelancing());
        calcBasics.getNormalFreelancers().clear();
        for (KGLNormalFreelancer pnf : _priorCalcBasics.getNormalFreelancers()) {
            KGLNormalFreelancer nf = new KGLNormalFreelancer();
            nf.setId(-1);
            nf.setBaseInformationID(calcBasics.getId());
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
            fc.setBaseInformationID(calcBasics.getId());
            fc.setCaseCnt(pfc.getCaseCnt());
            fc.setDivision(pfc.getDivision());
            fc.setDepartmentKey(pfc.getDepartmentKey());
            calcBasics.getNormalFeeContracts().add(fc);
        }

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

        // maternity
        calcBasics.setGynecology(_priorCalcBasics.isGynecology());
        calcBasics.setObstetrical(_priorCalcBasics.isObstetrical());

        // neonat
        calcBasics.setNeonatLvl(_priorCalcBasics.getNeonatLvl());
        //calcBasics.getNeonateData().clear();
        int headerId = _calcFacade.retrieveHeaderTexts(calcBasics.getDataYear(), 20, 0).get(0).getId();
        _priorCalcBasics.getNeonateData().stream().filter(old -> old.getContentText().getHeaderTextId() == headerId).forEach(old -> {
            Optional<DrgNeonatData> optDat = calcBasics.getNeonateData().stream().filter(nd -> nd.getContentTextId() == old.getContentTextId()).findFirst();
            if (optDat.isPresent()) {
                optDat.get().setData(old.getData());
            }
        });

        // NonMedicalInfrastructure
//        calcBasics.setDescNonMedicalInfra(_priorCalcBasics.getIblvMethodNonMedInfra() == 0);
//        calcBasics.setOtherMethodNonMedInfra(_priorCalcBasics.getOtherMethodNonMedInfra());
//        calcBasics.setIblvMethodNonMedInfra(_priorCalcBasics.getIblvMethodNonMedInfra());
        // MedicalInfrastructure
        calcBasics.setDescMedicalInfra(_priorCalcBasics.getIblvMethodMedInfra() == 0);
        calcBasics.setOtherMethodMedInfra(_priorCalcBasics.getOtherMethodMedInfra());
        calcBasics.setIblvMethodMedInfra(_priorCalcBasics.getIblvMethodMedInfra());

        // NormalStationServiceDocumentation
        calcBasics.getNormalStationServiceDocumentations().clear();
        for (DrgContentText ct : getNormalWardServiceDocHeaders()) {
            KGLNormalStationServiceDocumentation add = new KGLNormalStationServiceDocumentation();
            add.setContentTextID(ct.getId());
            add.setBaseInformationID(calcBasics.getId());
            for (KGLNormalStationServiceDocumentation addPrior : _priorCalcBasics.getNormalStationServiceDocumentations()) {
                if (add.getContentTextID() == addPrior.getContentTextID()) {
                    add.setUsed(addPrior.isUsed());
                    break;
                }
            }
            add.setLabel(_calcFacade.findCalcContentText(add.getContentTextID()).getText());
            calcBasics.getNormalStationServiceDocumentations().add(add);
        }
        calcBasics.getCostCenterCosts().clear();
        _priorCalcBasics.getCostCenterCosts().stream().map((ccc) -> {
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

    private DrgCalcBasics loadCalcBasicsDrg(String idObject) {
        int id = Integer.parseInt(idObject);
        DrgCalcBasics calcBasics = _calcFacade.findCalcBasicsDrg(id);
        if (hasSufficientRights(calcBasics)) {
//            calcBasics.setDescNonMedicalInfra(!calcBasics.getOtherMethodNonMedInfra().isEmpty());
            checkRequireInputsForDelimitationFact(calcBasics);
            return calcBasics;
        }
        return new DrgCalcBasics();
    }

    private boolean hasSufficientRights(DrgCalcBasics calcBasics) {
        if (_sessionController.isMyAccount(calcBasics.getAccountId(), false)) {
            return true;
        }
        return _cooperationTools.isAllowed(Feature.CALCULATION_HOSPITAL, calcBasics.getStatus(), calcBasics.getAccountId());
    }

    public boolean disableRadiologyServiceCheckboxes(KGLListRadiologyLaboratory rl) {
        return rl.isServiceDocDKG() || rl.isServiceDocDif() || rl.isServiceDocEBM() || rl.isServiceDocGOA() || rl.isServiceDocHome();
    }

    public List<KGLListRadiologyLaboratory> getLaboratories() {
        List<KGLListRadiologyLaboratory> rls = new ArrayList<>();
        for (KGLListRadiologyLaboratory rl : _calcBasics.getRadiologyLaboratories()) {
            if (rl.getCostCenterID() == 10) {
                rls.add(rl);
            }
        }
        return rls;
    }

    public List<KGLListRadiologyLaboratory> getRadiologies() {
        List<KGLListRadiologyLaboratory> rls = new ArrayList<>();
        for (KGLListRadiologyLaboratory rl : _calcBasics.getRadiologyLaboratories()) {
            if (rl.getCostCenterID() == 9) {
                rls.add(rl);
            }
        }
        return rls;
    }

    private DrgCalcBasics newCalcBasicsDrg() {
        Account account = _sessionController.getAccount();
        DrgCalcBasics calcBasics = new DrgCalcBasics();
        calcBasics.setAccountId(account.getId());
        calcBasics.setDataYear(Utils.getTargetYear(Feature.CALCULATION_HOSPITAL));
        calcBasics.getMedInfras().add(new KGLListMedInfra(-1, 170, "", "", "", 0, calcBasics.getId()));
        calcBasics.getMedInfras().add(new KGLListMedInfra(-1, 180, "", "", "", 0, calcBasics.getId()));

        if (getIks().size() == 1) {
            calcBasics.setIk((int) getIks().get(0).getValue());
        }
        ensureNeonateData(calcBasics);
        ensureRadiologyServiceData(calcBasics);
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

    private void ensureRadiologyServiceData(DrgCalcBasics calcBasics) {
        for (DrgContentText ct : _calcFacade.findAllCalcContentTexts()) {
            if (ct.getHeaderTextId() == 12) {
                KGLRadiologyService rs = new KGLRadiologyService();
                rs.setRsBaseInformationID(calcBasics.getId());
                rs.setRsContentTextID(ct.getId());
                KGLListContentTextOps ops = _calcFacade.findOpsCodeByContentTextId(ct.getId());
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

    private void checkRequireInputsForDelimitationFact(DrgCalcBasics calcBasic) {
        for (DrgDelimitationFact df : calcBasic.getDelimitationFacts()) {
            int id = df.getContentText().getId();
            if (id == 1 || id == 5 || id == 6 || id == 16 || id == 17 || id == 18) {
                df.setRequireInputs(true);
            }
        }
    }

    public DrgDelimitationFact getPriorDelimitationFact(int contentTextId) {
        for (DrgDelimitationFact df : _priorCalcBasics.getDelimitationFacts()) {
            if (df.getContentTextId() == contentTextId) {
                return df;
            }
        }
        return new DrgDelimitationFact();
    }

    public List<String> getDelimitationFactsSubTitles() {
        List<String> tmp = new ArrayList<>();
        tmp.add("Personalkosten");
        tmp.add("Sachkosten");
        tmp.add("Infrastrukturkosten");
        return tmp;
    }

    public void addLocation() {
        KGLListLocation loc = new KGLListLocation();
        loc.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getLocations().add(loc);
    }

    public void deleteLocation(KGLListLocation loc) {
        _calcBasics.getLocations().remove(loc);
    }

    public void addSpecialUnit() {
        KGLListSpecialUnit su = new KGLListSpecialUnit();
        su.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getSpecialUnits().add(su);
    }

    public void addCostCenterCosts() {
        KGLListCostCenterCost ccc = new KGLListCostCenterCost();
        ccc.setBaseInformationId(_calcBasics.getId());
        ccc.setId(-1);
        _calcBasics.getCostCenterCosts().add(ccc);
    }

    public void deleteCostCenterCosts(KGLListCostCenterCost x) {
        _calcBasics.getCostCenterCosts().remove(x);
    }

    public void deleteSpecialUnit(KGLListSpecialUnit su) {
        _calcBasics.getSpecialUnits().remove(su);
    }

    public void addCentralFocus() {
        KGLListCentralFocus cf = new KGLListCentralFocus();
        cf.setBaseInformationID(_calcBasics.getId());
        _calcBasics.getCentralFocuses().add(cf);
    }

    public void deleteCentralFocus(KGLListCentralFocus cf) {
        _calcBasics.getCentralFocuses().remove(cf);
    }

    public void addLaboratory() {
        KGLListRadiologyLaboratory rl = new KGLListRadiologyLaboratory();
        rl.setBaseInformationID(_calcBasics.getId());
        rl.setCostCenterID(10);
        _calcBasics.getRadiologyLaboratories().add(rl);
    }

    public void addRadiology() {
        KGLListRadiologyLaboratory rl = new KGLListRadiologyLaboratory();
        rl.setBaseInformationID(_calcBasics.getId());
        rl.setCostCenterID(9);
        _calcBasics.getRadiologyLaboratories().add(rl);
    }

    public void deleteLaboratory(KGLListRadiologyLaboratory rl) {
        _calcBasics.getRadiologyLaboratories().remove(rl);
    }
    
    public void addNormalStationServiceDocMinutes() {
        KGLNormalStationServiceDocumentationMinutes min = new KGLNormalStationServiceDocumentationMinutes();
        min.setBaseInformationId(_calcBasics.getId());
        _calcBasics.getNormalStationServiceDocumentationMinutes().add(min);
    }
    
    public void deleteNormalStationServiceDocMinutes(KGLNormalStationServiceDocumentationMinutes item) {
        _calcBasics.getNormalStationServiceDocumentationMinutes().remove(item);
    }

    public void addMedInfra(int costType) {
        KGLListMedInfra mif = new KGLListMedInfra();
        mif.setBaseInformationID(_calcBasics.getId());
        mif.setCostTypeID(costType);
        _calcBasics.getMedInfras().add(mif);
    }

    public void addFreelancer() {
        KGLNormalFreelancer nf = new KGLNormalFreelancer();
        nf.setBaseInformationID(_calcBasics.getId());
        _calcBasics.getNormalFreelancers().add(nf);
    }

    public void deleteFreelancer(KGLNormalFreelancer nf) {
        _calcBasics.getNormalFreelancers().remove(nf);
    }

    public void addFeeContract() {
        KGLNormalFeeContract fc = new KGLNormalFeeContract();
        fc.setBaseInformationID(_calcBasics.getId());
        _calcBasics.getNormalFeeContracts().add(fc);
    }

    public void addPkmsService() {
        KGLPKMSAlternative pkmsAlt = new KGLPKMSAlternative();
        pkmsAlt.setBaseInformationID(_calcBasics.getId());
        _calcBasics.getPkmsAlternatives().add(pkmsAlt);
    }

    public void deletePkmsService(KGLPKMSAlternative item) {
        _calcBasics.getPkmsAlternatives().remove(item);
    }

    public void deleteFeeContract(KGLNormalFeeContract fc) {
        _calcBasics.getNormalFeeContracts().remove(fc);
    }

    public List<KGLListMedInfra> getMedInfra(int costType) {
        List<KGLListMedInfra> tmp = new ArrayList<>();
        _calcBasics.getMedInfras().stream().filter((mi) -> (mi.getCostTypeID() == costType)).forEachOrdered((mi) -> {
            tmp.add(mi);
        });
        return tmp;
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

    public List<KGLListIntensivStroke> getIntensivStroke(boolean getIntensiv) {
        return _calcBasics.getIntensivStrokes().stream()
                .filter(i -> getIntensiv ? i.getIntensiveType() == 1 : i.getIntensiveType() == 2)
                .collect(Collectors.toList());
    }

    public List<KGLListIntensivStroke> addIntensivStroke(boolean isIntensiv) {
        List<KGLListIntensivStroke> result = _calcBasics.getIntensivStrokes();
        KGLListIntensivStroke item = new KGLListIntensivStroke();
        item.setBaseInformationId(_calcBasics.getId());
        if (isIntensiv) {
            item.setIntensiveType(1);
        } else {
            item.setIntensiveType(2);
        }
        result.add(item);
        return result;
    }

    public void deleteIntensivStroke(KGLListIntensivStroke item) {
        _calcBasics.getIntensivStrokes().remove(item);
    }

//    public Optional<KGLListIntensivStroke> getPriorIntensivStroke(KGLListIntensivStroke item) {
//        return _priorCalcBasics.getIntensivStrokes().stream()
//                .filter(i -> i.getCostCenterID() == item.getCostCenterID())
//                .findAny();
//    }
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

    private static final String HeadLineIntensiv
            = "Intensivstation;FAB;Anzahl_Betten;Anzahl_Fälle;Mindestmerkmale_OPS_8-980_erfüllt;"
            + "Mindestmerkmale_OPS_8-98f_erfüllt;Mindestmerkmale_nur_erfüllt_im_Zeitabschnitt;"
            + "Summe_gewichtete_Intensivstunden;Summe_ungewichtete_Intensivstunden;"
            + "Minimum;Maximum;Erläuterung;Vollkraft_ÄD;Vollkraft_PD;Vollkraft_FD;"
            + "Kosten_ÄD;Kosten_PD;Kosten_FD;Kosten_GK_Arzneimittel;Kosten_GK_med_Sachbedarf;"
            + "Kosten_med_Infra;Kosten_nicht_med_Infra";

    public void downloadTemplateIntensiv() {
        Utils.downloadText(HeadLineIntensiv + "\n", "Intensiv.csv");
    }

    private String _importMessageIntensiv = "";

    public String getImportMessageIntensiv() {
        return _importMessageIntensiv;
    }

    @Inject private Instance<IntensivDataImporter> _importIntensivProvider;

    private Part _fileIntensivCare;

    public Part getFileIntensivCare() {
        return _fileIntensivCare;
    }

    public void setFileIntensivCare(Part file) {
        _fileIntensivCare = file;
    }

    public void uploadNoticesIntensiv() {
        try {
            if (_fileIntensivCare != null) {
                //Scanner scanner = new Scanner(_file.getInputStream(), "UTF-8");
                // We assume most of the documents coded with the Windows character set
                // Thus, we read with the system default
                // in case of an UTF-8 file, all German Umlauts will be corrupted.
                // We simply replace them.
                // Drawbacks: this only converts the German Umlauts, no other chars.
                // By intention it fails for other charcters
                // Alternative: implement a library which guesses th correct character set and read properly
                // Since we support German only, we started using the simple approach
                Scanner scanner = new Scanner(_fileIntensivCare.getInputStream());
                if (!scanner.hasNextLine()) {
                    return;
                }
                IntensivDataImporter itemImporter = _importIntensivProvider.get();
                itemImporter.setCalcBasics(_calcBasics);
                while (scanner.hasNextLine()) {
                    String line = Utils.convertFromUtf8(scanner.nextLine());
                    if (!line.equals(HeadLineIntensiv)) {
                        itemImporter.tryImportLine(line);
                    }
                }
                _importMessageIntensiv = itemImporter.getMessage();
                _sessionController.alertClient(_importMessageIntensiv);
                _showJournal = false;
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

    public List<DrgContentText> getNormalWardServiceDocHeaders() {
        return _calcFacade.retrieveContentTexts(13, Calendar.getInstance().get(Calendar.YEAR));
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
        checkRequireInputsForDelimitationFact(_calcBasics);

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
        return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _calcBasics.getStatus(), _calcBasics.getAccountId());
    }

    public boolean isCopyForResendAllowed() {
        if (_calcBasics.getStatusID() < 10 || !_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return !_calcFacade.existActiveCalcBasicsDrg(_calcBasics.getIk());
    }

    public void copyForResend() {
        if (true) {
            // todo: remove this condition after implementing this feature
            Utils.showMessageInBrowser("Diese Funktion wird gerade für Sie programmiert");
            return;
        }
        _calcBasics.setId(-1);
        _calcBasics.setStatus(WorkflowStatus.New);
        _calcBasics.setAccountId(_sessionController.getAccountId());
        for (KGLListCentralFocus item : _calcBasics.getCentralFocuses()) {
            item.setId(-1);
            item.setBaseInformationID(-1);
        }
        // todo: update Ids for all Lists. Use interface or use copy constructer as alternative

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
        _calcBasics = _calcFacade.saveCalcBasicsDrg(_calcBasics);

        CalcHospitalUtils.createTransferFile(_sessionController, _calcBasics);

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
    List<SelectItem> _ikItems;

    public List<SelectItem> getIks() {
        if (_ikItems == null) {
            Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> iks = _calcFacade.obtainIks4NewBasics(CalcHospitalFunction.CalculationBasicsDrg, accountIds, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), testMode);
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
    public int priorProvisionAmount(KGLListServiceProvision current) {
        Optional<KGLListServiceProvision> prior = _priorCalcBasics.getServiceProvisions().stream().filter(p -> p.getServiceProvisionTypeID() == current.getServiceProvisionTypeID()).findAny();
        if (prior.isPresent()) {
            return prior.get().getAmount();
        }
        return 0;
    }

    public void addServiceProvision() {
        int seq = _calcBasics.getServiceProvisions().stream().mapToInt(sp -> sp.getSequence()).max().orElse(0);
        KGLListServiceProvision data = new KGLListServiceProvision();
        data.setBaseInformationId(_calcBasics.getId());
        data.setServiceProvisionTypeID(-1);
        data.setSequence(++seq);
        _calcBasics.getServiceProvisions().add(data);
    }

    public void deleteServiceProvision(KGLListServiceProvision item) {
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
        KGLListCostCenter item = new KGLListCostCenter(_calcBasics.getId(), costCenterId);
        _calcBasics.getCostCenters().add(item);
    }

    public void deleteCostCenter(KGLListCostCenter item) {
        _calcBasics.getCostCenters().remove(item);
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    private static final String HeadLine = "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;Kostenvolumen;VollkräfteÄD;Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten";

    public void downloadTemplate() {
        Utils.downloadText(HeadLine + "\n", "Kostenstellengruppe_11_12_13.csv");
    }

    private String _importMessage = "";

    public String getImportMessage() {
        return _importMessage;
    }

    @Inject private Instance<CostCenterDataImporter> _importProvider;

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
                CostCenterDataImporter itemImporter = _importProvider.get();
                itemImporter.setCalcBasics(_calcBasics);
                while (scanner.hasNextLine()) {
                    String line = Utils.convertFromUtf8(scanner.nextLine());
                    if (!line.equals(HeadLine)) {
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tab MVI">
    public String downloadDocument(String name) {
        Document document = _calcBasics.getDocuments().stream().filter(d -> d.getName().equalsIgnoreCase(name) && d.getSheetId() == 19).findAny().orElse(null);
        if (document != null) {
            return Utils.downloadDocument(document);
        }
        return "";
    }

    public String deleteDocument(String name) {
        KGLDocument document = _calcBasics.getDocuments().stream().filter(d -> d.getName().equalsIgnoreCase(name) && d.getSheetId() == 19).findAny().orElse(null);
        if (document != null) {
            _calcBasics.getDocuments().remove(document);
        }
        return null;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Tab Neonatology">
    public List<DrgHeaderText> getHeaders() {
        return _calcFacade.retrieveHeaderTexts(_calcBasics.getDataYear(), 20, -1);
    }

    public List<DrgHeaderText> getHeaders(int type) {
        return _calcFacade.retrieveHeaderTexts(Calendar.getInstance().get(Calendar.YEAR), 20, type);
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

    public BigDecimal priorData(int textId) {
        BigDecimal priorValue = _priorCalcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(BigDecimal.ZERO);
        return priorValue;
    }

    public String diffData(int textId) {
        DrgNeonatData data = _calcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).findFirst().get();
        BigDecimal priorValue = _priorCalcBasics.getNeonateData().stream().filter(d -> d.getContentTextId() == textId).map(d -> d.getData()).findFirst().orElse(BigDecimal.ZERO);
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
        item.setBaseInformationID(_calcBasics.getId());
        _calcBasics.getObstetricsGynecologies().add(item);
    }

//    public List<KGLListIntensivStroke> getIntensivStrokeDiffList() {
//        List<KGLListIntensivStroke> result = new ArrayList<>();
//        List<KGLListIntensivStroke> intensivStrokes = _calcBasics.getIntensivStrokes();
//        List<KGLListIntensivStroke> priorIntensivStrokes = _priorCalcBasics.getIntensivStrokes();
//
//        Comparator<KGLListIntensivStroke> comp = (i1, i2) -> {
//            if (i1.getDepartmentKey().equals(i2.getDepartmentKey())) {
//                return i1.getDepartmentKey().compareTo(i2.getDepartmentKey());
//            } else {
//                return i1.getCostCenterID() - i2.getCostCenterID();
//            }
//        };
//
//        intensivStrokes.sort(comp);
//        priorIntensivStrokes.sort(comp);
//
//        diffIntensivStrokeValues(intensivStrokes, priorIntensivStrokes, comp, result);
//
//        return result;
//    }
    public String calcPercentualDiff(int priorValue, int currentValue) {
        if (priorValue == 0) {
            return "";
        }
        return Math.round(1000d * (currentValue - priorValue) / priorValue) / 10d + "%";
    }

    public String calcPercentualDiff(BigDecimal priorValue, BigDecimal currentValue) {
        if (priorValue.doubleValue() == 0) {
            return "";
        }
        MathContext mc = new MathContext(2);
        return (currentValue.subtract(priorValue)).divide(priorValue, mc).multiply(new BigDecimal(100)).setScale(1, RoundingMode.HALF_UP) + "%";
    }

    public int getMedInfraSum(int type) {
        int sumAmount = 0;
        for (KGLListMedInfra m : _calcBasics.getMedInfras()) {
            if (m.getCostTypeID() == type) {
                sumAmount += m.getAmount();
            }
        }
        return sumAmount;
    }

    public String getCostTypeText(int costTypeId) {
        CostType ct = _costTypeFacade.find(costTypeId);
        if (ct != null) {
            return ct.getText();
        }
        return "Unbekannte Kostenartengruppe";
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
        return _calcFacade.findCalcContentText(id).getText();
    }

    public KGLRadiologyService getPriorRadiologyService(int contentTextId) {
        for (KGLRadiologyService rs : _priorCalcBasics.getRadiologyServices()) {
            if (rs.getRsContentTextID() == contentTextId) {
                return rs;
            }
        }
        return new KGLRadiologyService();
    }

//    private void diffIntensivStrokeValues(List<KGLListIntensivStroke> intensivStrokes,
//            List<KGLListIntensivStroke> priorIntensivStrokes,
//            Comparator<KGLListIntensivStroke> comp,
//            List<KGLListIntensivStroke> result) {
//
//        int i = 0;
//        for (KGLListIntensivStroke intensivStroke : intensivStrokes) {
//            while (i < priorIntensivStrokes.size() && comp.compare(intensivStroke, priorIntensivStrokes.get(i)) > 0) {
//                result.add(createDiffIntensivStrokeRight(new KGLListIntensivStroke(), priorIntensivStrokes.get(i)));
//                i++;
//            }
//            if (i < priorIntensivStrokes.size() && comp.compare(intensivStroke, priorIntensivStrokes.get(i)) == 0) {
//                result.add(createDiffIntensivStrokeLeft(intensivStroke, priorIntensivStrokes.get(i)));
//                i++;
//            } else {
//                result.add(createDiffIntensivStrokeLeft(intensivStroke, new KGLListIntensivStroke()));
//            }
//        }
//    }
//    private KGLListIntensivStroke createDiffIntensivStrokeRight(KGLListIntensivStroke newVal, KGLListIntensivStroke oldVal) {
//        KGLListIntensivStroke result = new KGLListIntensivStroke();
//
//        result.setBaseInformationId(oldVal.getBaseInformationId());
//        result.setIntensiveType(oldVal.getIntensiveType());
//        result.setCostCenterID(oldVal.getCostCenterID());
//        result.setCostCenterText(oldVal.getCostCenterText());
//        result.setDepartmentKey(oldVal.getDepartmentKey());
//        result.setDepartmentAssignment(oldVal.getDepartmentAssignment());
//        result.setBedCnt(newVal.getBedCnt() - oldVal.getBedCnt());
//        result.setCaseCnt(newVal.getCaseCnt() - oldVal.getCaseCnt());
//        result.setOps8980(newVal.getOps8980() ^ oldVal.getOps8980());
//        result.setOps898f(newVal.getOps898f() ^ oldVal.getOps898f());
//        result.setOps8981(newVal.getOps8981() ^ oldVal.getOps8981());
//        result.setOps898b(newVal.getOps898b() ^ oldVal.getOps898b());
//        result.setMinimumCriteriaPeriod(oldVal.getMinimumCriteriaPeriod());
//        result.setIntensivHoursWeighted(newVal.getIntensivHoursNotweighted() - oldVal.getIntensivHoursWeighted());
//        result.setIntensivHoursNotweighted(newVal.getIntensivHoursNotweighted() - oldVal.getIntensivHoursNotweighted());
//        result.setWeightMinimum(newVal.getWeightMinimum() - oldVal.getWeightMinimum());
//        result.setWeightMaximum(newVal.getWeightMaximum() - oldVal.getWeightMaximum());
//        result.setWeightDescription(oldVal.getWeightDescription());
//        result.setMedicalServiceCnt(newVal.getMedicalServiceCnt() - oldVal.getMedicalServiceCnt());
//        result.setNursingServiceCnt(newVal.getNursingServiceCnt() - oldVal.getNursingServiceCnt());
//        result.setFunctionalServiceCnt(newVal.getFunctionalServiceCnt() - oldVal.getFunctionalServiceCnt());
//        result.setMedicalServiceCost(newVal.getMedicalServiceCost() - oldVal.getMedicalServiceCost());
//        result.setNursingServiceCost(newVal.getNursingServiceCost() - oldVal.getNursingServiceCost());
//        result.setFunctionalServiceCost(newVal.getFunctionalServiceCost() - oldVal.getFunctionalServiceCost());
//        result.setOverheadsMedicine(newVal.getOverheadsMedicine() - oldVal.getOverheadsMedicine());
//        result.setOverheadMedicalGoods(newVal.getOverheadMedicalGoods() - oldVal.getOverheadMedicalGoods());
//        result.setMedicalInfrastructureCost(newVal.getMedicalInfrastructureCost() - oldVal.getMedicalInfrastructureCost());
//        result.setNonMedicalInfrastructureCost(newVal.getNonMedicalInfrastructureCost() - oldVal.getNonMedicalInfrastructureCost());
//
//        return result;
//    }
//
//    private KGLListIntensivStroke createDiffIntensivStrokeLeft(KGLListIntensivStroke newVal, KGLListIntensivStroke oldVal) {
//        KGLListIntensivStroke result = new KGLListIntensivStroke();
//
//        result.setBaseInformationId(newVal.getBaseInformationId());
//        result.setIntensiveType(newVal.getIntensiveType());
//        result.setCostCenterID(newVal.getCostCenterID());
//        result.setCostCenterText(newVal.getCostCenterText());
//        result.setDepartmentKey(newVal.getDepartmentKey());
//        result.setDepartmentAssignment(newVal.getDepartmentAssignment());
//        result.setBedCnt(newVal.getBedCnt() - oldVal.getBedCnt());
//        result.setCaseCnt(newVal.getCaseCnt() - oldVal.getCaseCnt());
//        result.setOps8980(newVal.getOps8980() ^ oldVal.getOps8980());
//        result.setOps898f(newVal.getOps898f() ^ oldVal.getOps898f());
//        result.setOps8981(newVal.getOps8981() ^ oldVal.getOps8981());
//        result.setOps898b(newVal.getOps898b() ^ oldVal.getOps898b());
//        result.setMinimumCriteriaPeriod(newVal.getMinimumCriteriaPeriod());
//        result.setIntensivHoursWeighted(newVal.getIntensivHoursNotweighted() - oldVal.getIntensivHoursWeighted());
//        result.setIntensivHoursNotweighted(newVal.getIntensivHoursNotweighted() - oldVal.getIntensivHoursNotweighted());
//        result.setWeightMinimum(newVal.getWeightMinimum() - oldVal.getWeightMinimum());
//        result.setWeightMaximum(newVal.getWeightMaximum() - oldVal.getWeightMaximum());
//        result.setWeightDescription(newVal.getWeightDescription());
//        result.setMedicalServiceCnt(newVal.getMedicalServiceCnt() - oldVal.getMedicalServiceCnt());
//        result.setNursingServiceCnt(newVal.getNursingServiceCnt() - oldVal.getNursingServiceCnt());
//        result.setFunctionalServiceCnt(newVal.getFunctionalServiceCnt() - oldVal.getFunctionalServiceCnt());
//        result.setMedicalServiceCost(newVal.getMedicalServiceCost() - oldVal.getMedicalServiceCost());
//        result.setNursingServiceCost(newVal.getNursingServiceCost() - oldVal.getNursingServiceCost());
//        result.setFunctionalServiceCost(newVal.getFunctionalServiceCost() - oldVal.getFunctionalServiceCost());
//        result.setOverheadsMedicine(newVal.getOverheadsMedicine() - oldVal.getOverheadsMedicine());
//        result.setOverheadMedicalGoods(newVal.getOverheadMedicalGoods() - oldVal.getOverheadMedicalGoods());
//        result.setMedicalInfrastructureCost(newVal.getMedicalInfrastructureCost() - oldVal.getMedicalInfrastructureCost());
//        result.setNonMedicalInfrastructureCost(newVal.getNonMedicalInfrastructureCost() - oldVal.getNonMedicalInfrastructureCost());
//
//        return result;
//    }
}
