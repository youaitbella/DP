/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.backingbean;

import org.inek.dataportal.calc.entities.drg.*;
import org.inek.dataportal.calc.facades.CalcDrgFacade;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author muellermi
 */
public class PreloadFunctionsCalcBasicsDrg {

    private static final Logger LOGGER = Logger.getLogger("PreloadFunctionsCalcBasicsDrg");

    static void preloadData(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        KglOpAn opAn = new KglOpAn(calcBasics.getId(), priorCalcBasics.getOpAn());
        calcBasics.setOpAn(opAn);

        preloadLocations(calcBasics, priorCalcBasics);
        preloadCentralFocuses(calcBasics, priorCalcBasics);
        calcBasics.getDelimitationFacts().clear();
        populateDelimitationFactsIfAbsent(calcDrgFacade, calcBasics, priorCalcBasics);
        preloadRadiologyAndLab(calcBasics, priorCalcBasics);
        preloadObstetricsGynecologies(calcBasics, priorCalcBasics);
        preloadNormalWard(calcBasics, priorCalcBasics);
        initServiceProvision(calcDrgFacade, calcBasics, priorCalcBasics);
        // cardiology
        calcBasics.setCardiology(priorCalcBasics.isCardiology());
        // endoscopy
        calcBasics.setEndoscopy(priorCalcBasics.isEndoscopy());
        calcBasics.setEndoscopyCaseCnt(priorCalcBasics.getEndoscopyCaseCnt());
        calcBasics.setEndoscopyRoomCnt(priorCalcBasics.getEndoscopyRoomCnt());
        // maternity
        calcBasics.setGynecology(priorCalcBasics.isGynecology());
        calcBasics.setObstetrical(priorCalcBasics.isObstetrical());
        preloadNeonat(calcDrgFacade, calcBasics, priorCalcBasics);
        // MedicalInfrastructure
        calcBasics.setDescMedicalInfra(priorCalcBasics.getIblvMethodMedInfra() == 0);
        calcBasics.setOtherMethodMedInfra(priorCalcBasics.getOtherMethodMedInfra());
        calcBasics.setIblvMethodMedInfra(priorCalcBasics.getIblvMethodMedInfra());
        preloadNormalWardServiceDocumentation(calcDrgFacade, calcBasics, priorCalcBasics);   
    }

    private static void preloadLocations(DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.setLocationCnt(priorCalcBasics.getLocationCnt());
        calcBasics.setDifLocationSupply(priorCalcBasics.isDifLocationSupply());
        calcBasics.setSpecialUnit(priorCalcBasics.isSpecialUnit());
        calcBasics.getLocations().clear();
        for (KGLListLocation location : priorCalcBasics.getLocations()) {
            KGLListLocation loc = new KGLListLocation();
            loc.setId(-1);
            loc.setBaseInformationId(calcBasics.getId());
            loc.setLocation(location.getLocation());
            loc.setLocationNo(location.getLocationNo());
            calcBasics.getLocations().add(loc);
        }
    }

    private static void preloadCentralFocuses(DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.getCentralFocuses().clear();
        calcBasics.setCentralFocus(priorCalcBasics.isCentralFocus());
        for (KGLListCentralFocus centralFocus : priorCalcBasics.getCentralFocuses()) {
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

    static void populateDelimitationFactsIfAbsent(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        if (!calcBasics.getDelimitationFacts().isEmpty()) {
            return;
        }
        if (calcBasics.getId() > 0) {
            // This should not be. But sometimes we lost the delimitationFacts...
            LOGGER.log(Level.WARNING, "Populate DRG DelimitationFacts for existing data: Id = {0}", calcBasics.getId());
        }
        for (DrgContentText ct : calcDrgFacade.retrieveContentTexts(1, calcBasics.getDataYear())) {
            DrgDelimitationFact df = new DrgDelimitationFact();
            df.setBaseInformationId(calcBasics.getId());
            df.setContentText(ct);
            df.setUsed(getPriorDelimitationFact(priorCalcBasics, ct.getId()).isUsed());
            calcBasics.getDelimitationFacts().add(df);
        }
    }

    public static DrgDelimitationFact getPriorDelimitationFact(DrgCalcBasics priorCalcBasics, int contentTextId) {
        return priorCalcBasics.getDelimitationFacts().stream()
                .filter(f -> f.getContentTextId() == contentTextId)
                .findAny().orElse(new DrgDelimitationFact());
    }


    private static void preloadRadiologyAndLab(DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.getRadiologyLaboratories().clear();
        for (KGLListRadiologyLaboratory prl : priorCalcBasics.getRadiologyLaboratories()) {
            KGLListRadiologyLaboratory rl = new KGLListRadiologyLaboratory();
            rl.setId(-1);
            rl.setBaseInformationId(calcBasics.getId());
            rl.setCostCenterId(prl.getCostCenterId());
            rl.setCostCenterNumber(prl.getCostCenterNumber());
            rl.setCostCenterText(prl.getCostCenterText());
            calcBasics.getRadiologyLaboratories().add(rl);
        }
    }

    private static void preloadObstetricsGynecologies(DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.getObstetricsGynecologies().clear();
        for (KGLListObstetricsGynecology pObst : priorCalcBasics.getObstetricsGynecologies()) {
            KGLListObstetricsGynecology obst = new KGLListObstetricsGynecology();
            obst.setBaseInformationId(calcBasics.getId());
            obst.setCostCenterText(pObst.getCostCenterText());
            obst.setCostTypeId(pObst.getCostTypeId());
            calcBasics.getObstetricsGynecologies().add(obst);
        }
    }

    private static void preloadNormalWard(DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.setNormalFreelancing(priorCalcBasics.isNormalFreelancing());
        calcBasics.getNormalFreelancers().clear();
        for (KGLNormalFreelancer pnf : priorCalcBasics.getNormalFreelancers()) {
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
        for (KGLNormalFeeContract pfc : priorCalcBasics.getNormalFeeContracts()) {
            KGLNormalFeeContract fc = new KGLNormalFeeContract();
            fc.setId(-1);
            fc.setBaseInformationId(calcBasics.getId());
            fc.setCaseCnt(pfc.getCaseCnt());
            fc.setDivision(pfc.getDivision());
            fc.setDepartmentKey(pfc.getDepartmentKey());
            calcBasics.getNormalFeeContracts().add(fc);
        }                
        calcBasics.setDeliveryRoomOrganizationalStructure(priorCalcBasics.getDeliveryRoomOrganizationalStructure());
    }

    private static void initServiceProvision(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.getServiceProvisions().clear();

        List<KGLListServiceProvisionType> provisionTypes = calcDrgFacade.retrieveServiceProvisionTypes(calcBasics.getDataYear(), true);

        loadMandatoryServiceProvision(provisionTypes, calcBasics);
        addMissingPriorProvisionTypes(calcBasics, priorCalcBasics);

    }

    private static void loadMandatoryServiceProvision(List<KGLListServiceProvisionType> provisionTypes, DrgCalcBasics calcBasics) {
        // always populate mandatory entries
        for (KGLListServiceProvisionType provisionType : provisionTypes) {
            KGLListServiceProvision data = new KGLListServiceProvision(calcBasics.getId());
            data.setServiceProvisionType(provisionType);
            calcBasics.getServiceProvisions().add(data);
        }
    }

    private static void addMissingPriorProvisionTypes(DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        // get prior values and additional entries
        for (KGLListServiceProvision prior : priorCalcBasics.getServiceProvisions()) {
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

    private static void preloadNeonat(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.setNeonatLvl(priorCalcBasics.getNeonatLvl());

        int headerIdQuality = calcDrgFacade.retrieveHeaderTexts(calcBasics.getDataYear(), 20, 0).get(0).getId();
        priorCalcBasics.getNeonateData().stream().filter(old -> old.getContentText().getHeaderTextId() == headerIdQuality)
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

    private static void preloadNormalWardServiceDocumentation(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics, DrgCalcBasics priorCalcBasics) {
        calcBasics.getNormalStationServiceDocumentations().clear();
        List<DrgContentText> normalWardServiceDocHeaders = calcDrgFacade.retrieveContentTexts(13, Calendar.getInstance().get(Calendar.YEAR));
        for (DrgContentText ct : normalWardServiceDocHeaders) {
            KGLNormalStationServiceDocumentation add = new KGLNormalStationServiceDocumentation();
            add.setContentText(ct);
            add.setBaseInformationId(calcBasics.getId());
            for (KGLNormalStationServiceDocumentation addPrior : priorCalcBasics.getNormalStationServiceDocumentations()) {
                if (add.getContentTextId() == addPrior.getContentTextId()) {
                    add.setUsed(addPrior.isUsed());
                    break;
                }
            }
            calcBasics.getNormalStationServiceDocumentations().add(add);
        }
        calcBasics.getCostCenterCosts().clear();
        priorCalcBasics
                .getCostCenterCosts()
                .stream()
                .map((ccc) -> {
                    KGLListCostCenterCost c = new KGLListCostCenterCost();
                    c.setPrior(ccc);
                    c.setCostCenterNumber(ccc.getCostCenterNumber());
                    c.setCostCenterText(ccc.getCostCenterText());
                    c.setCostCenter(ccc.getCostCenter());
                    c.setPriorId(ccc.getPriorId());
                    c.setDepartmentKey(ccc.getDepartmentKey());
                    return c;
                }).forEachOrdered((c) -> {
                    calcBasics.getCostCenterCosts().add(c);
                });
    }

    public static void ensureNeonateData(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics) {
        if (!calcBasics.getNeonateData().isEmpty()) {
            return;
        }
        List<Integer> headerIds = calcDrgFacade.retrieveHeaderTexts(calcBasics.getDataYear(), 20, -1)
                .stream()
                .map(ht -> ht.getId())
                .collect(Collectors.toList());
        List<DrgContentText> contentTexts = calcDrgFacade.retrieveContentTexts(headerIds, calcBasics.getDataYear());
        for (DrgContentText contentText : contentTexts) {
            DrgNeonatData data = new DrgNeonatData();
            data.setContentTextId(contentText.getId());
            data.setContentText(contentText);
            data.setBaseInformationId(calcBasics.getId());
            calcBasics.getNeonateData().add(data);
        }
    }

    public static void ensureRadiologyServiceData(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics) {
        for (DrgContentText ct : calcDrgFacade.findAllCalcContentTexts()) {
            if (ct.getHeaderTextId() == 12 && ct.getLastYear() >= calcBasics.getDataYear()) {
                KGLRadiologyService rs = new KGLRadiologyService();
                rs.setBaseInformationId(calcBasics.getId());
                rs.setRsContentTextID(ct.getId());
                rs.setOpsCode(ct.getOpsCode());
                calcBasics.getRadiologyServices().add(rs);
            }
        }
    }

    static void ensureOverviewPersonal(CalcDrgFacade calcDrgFacade, DrgCalcBasics calcBasics) {
        for (KGLListOverviewPersonalType spt : calcDrgFacade.retrieveOverviewPersonalTypes(calcBasics.getDataYear())) {
            KGLListOverviewPersonal op = new KGLListOverviewPersonal();
            op.setBaseInformationId(calcBasics.getId());
            op.setOverviewPersonalType(spt);
            calcBasics.addOverviewPersonal(op);
        }        
    }

}
