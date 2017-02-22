package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.common.CostCenter;
import org.inek.dataportal.entities.common.CostType;
import org.inek.dataportal.entities.modelintention.AdjustmentType;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.enums.HospitalType;
import org.inek.dataportal.enums.InsuranceAffiliation;
import org.inek.dataportal.enums.MedicalAttribute;
import org.inek.dataportal.enums.PiaType;
import org.inek.dataportal.enums.QualityUsage;
import org.inek.dataportal.enums.Region;
import org.inek.dataportal.enums.RemunSystem;
import org.inek.dataportal.enums.SettleType;
import org.inek.dataportal.enums.TreatmentType;
import org.inek.dataportal.facades.common.CostCenterFacade;
import org.inek.dataportal.facades.common.CostTypeFacade;
import org.inek.dataportal.facades.modelintention.AdjustmentTypeFacade;

/**
 *
 * @author muellermi
 */
@Named
@Singleton
public class ValueLists{

    @Inject CostCenterFacade _costCenterFacade;
    @Inject CostTypeFacade _costTypeFacade;
    @Inject AdjustmentTypeFacade _adjustmentTypeFacade;

    @PostConstruct
    private void init() {
        loadCostCenters();

    }

    List<CostCenter> _costCenters;
    List<CostType> _costTypes;

    private void loadCostCenters() {
        _costCenters = _costCenterFacade.findAll();
        _costTypes = _costTypeFacade.findAll();
    }

    public List<SelectItem> getCostCenters(boolean includeTotal) {
        return getCostCenters(includeTotal, -1);
    }

    public List<SelectItem> getCostCentersDrg(boolean includeTotal) {
        return getCostCenters(includeTotal, 0);
    }

    public List<SelectItem> getCostCentersPsy(boolean includeTotal) {
        return getCostCenters(includeTotal, 1);
    }

    public List<SelectItem> getCostCenters(boolean includeTotal, int remunerationDomain) {
        Stream<CostCenter> stream = _costCenters.stream();
        if (remunerationDomain == 0) {
            stream = stream.filter(c -> c.getIsDrg());
        }
        if (remunerationDomain == 1) {
            stream = stream.filter(c -> c.getIsPsy());
        }
        return stream
                .filter(c -> c.getId() >= (includeTotal ? 0 : 1))
                .map(c -> new SelectItem(c.getId(), c.getCharId() + " " + c.getText()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getCostCentersCDM(int remunerationDomain) {
        Stream<CostCenter> stream = _costCenters.stream();
        if (remunerationDomain == 0) {
            stream = stream.filter(c -> c.getIsDrg() || c.getCharId().equals("OV"));
        }
        if (remunerationDomain == 1) {
            stream = stream.filter(c -> c.getIsPsy() || c.getCharId().equals("OV"));
        }
        return stream
                .filter(c -> c.getId() >= 1 || c.getId() == -9)
                .map(c -> new SelectItem(c.getId(), c.getCharId() + " " + c.getText()))
                .collect(Collectors.toList());
    }

    public int getCostCenterId(String charId) {
        return _costCenters.stream().filter(c -> c.getCharId().equals(charId)).mapToInt(c -> c.getId()).findAny().orElse(-1);
    }

    public CostCenter getCostCenter(int id) {
        return _costCenters.stream().filter(c -> c.getId() == id).findAny().orElse(new CostCenter());
    }

    /**
     * get all cost types include or exclude the total ("Randsumme")
     *
     * @param includeTotal
     * @return
     */
    public List<SelectItem> getCostTypes(boolean includeTotal) {
        return _costTypes.stream()
                .filter(c -> c.getId() >= (includeTotal ? 0 : 1))
                .map(c -> new SelectItem(c.getId(), c.getCharId() + " " + c.getText()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getCostTypesCDM() {
        List<String> costTypes = Arrays.asList(new String[]{"4b", "5", "6b", "6c", "10"});
        return _costTypes.stream()
                .filter(c -> costTypes.contains(c.getCharId()))
                .map(c -> new SelectItem(c.getId(), c.getCharId() + " " + c.getText()))
                .collect(Collectors.toList());
    }

    public int getCostTypeId(String charId) {
        return _costTypes.stream().filter(c -> c.getCharId().equals(charId)).mapToInt(c -> c.getId()).findAny().orElse(-1);
    }

    public CostType getCostType(int id) {
        return _costTypes.stream().filter(c -> c.getId() == id).findAny().orElse(new CostType());
    }

    List<SelectItem> _adjustmentTypes;

    public synchronized List<SelectItem> getAdjustmentTypes() {
        ensureAdjustmentTypes();
        return _adjustmentTypes;
    }

    private void ensureAdjustmentTypes() {
        if (_adjustmentTypes == null) {
            List<AdjustmentType> adjustmentTypes = _adjustmentTypeFacade.findAll();
            _adjustmentTypes = new ArrayList<>();
            SelectItem emptyItem = new SelectItem(-1, "");
            emptyItem.setNoSelectionOption(true);
            _adjustmentTypes.add(emptyItem);
            for (AdjustmentType adjustmentType : adjustmentTypes) {
                _adjustmentTypes.add(new SelectItem(adjustmentType.getId(), adjustmentType.getText()));
            }
        }
    }

    public List getTreatmentTypes() {
        List<SelectItem> list = new ArrayList<>();
        for (TreatmentType val : TreatmentType.values()) {
            list.add(new SelectItem(val.id(), val.type()));
        }
        return list;
    }

    public List getRemunerationDomains() {
        List<SelectItem> list = new ArrayList<>();
        for (RemunSystem val : RemunSystem.values()) {
            list.add(new SelectItem(val.getId(), val.getName()));
        }
        return list;
    }

    public List getQualityUsage() {
        List<SelectItem> list = new ArrayList<>();
        for (QualityUsage val : QualityUsage.values()) {
            list.add(new SelectItem(val.id(), val.type()));
        }
        return list;
    }

    public SelectItem[] getGenders() {
        List<SelectItem> l = new ArrayList<>();
        Genders[] genders = Genders.values();
        for (Genders g : genders) {
            l.add(new SelectItem(g.id(), g.gender()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getRegions() {
        List<SelectItem> l = new ArrayList<>();
        Region[] regions = Region.values();
        for (Region r : regions) {
            l.add(new SelectItem(r.id(), r.region()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getMedicalAttributes() {
        List<SelectItem> l = new ArrayList<>();
        MedicalAttribute[] attrs = MedicalAttribute.values();
        for (MedicalAttribute ma : attrs) {
            l.add(new SelectItem(ma.id(), ma.attribute()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getSettledTypes() {
        List<SelectItem> l = new ArrayList<>();
        SettleType[] types = SettleType.values();
        for (SettleType t : types) {
            l.add(new SelectItem(t.id(), t.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getPiaTypes() {
        List<SelectItem> l = new ArrayList<>();
        PiaType[] types = PiaType.values();
        for (PiaType p : types) {
            l.add(new SelectItem(p.id(), p.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public SelectItem[] getHospitalTypes() {
        List<SelectItem> l = new ArrayList<>();
        HospitalType[] types = HospitalType.values();
        for (HospitalType h : types) {
            l.add(new SelectItem(h.id(), h.type()));
        }
        return l.toArray(new SelectItem[l.size()]);
    }

    public List<SelectItem> getInsuranceAffiliation() {
        List<SelectItem> l = new ArrayList<>();
        for (InsuranceAffiliation t : InsuranceAffiliation.values()) {
            l.add(new SelectItem(t.id(), t.type()));
        }
        return l;
    }

    public List<SelectItem> getFeatures() {
        List<SelectItem> l = new ArrayList<>();
        SelectItem emptyItem = new SelectItem(null, "");
        emptyItem.setNoSelectionOption(true);
        l.add(emptyItem);
        for (Feature f : Feature.values()) {
            l.add(new SelectItem(f, f.getDescription()));
        }
        return l;
    }

}
