package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.common.CostCenter;
import org.inek.dataportal.entities.common.CostType;
import org.inek.dataportal.entities.modelintention.AdjustmentType;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.enums.HospitalType;
import org.inek.dataportal.enums.InsuranceAffiliation;
import org.inek.dataportal.enums.MedicalAttribute;
import org.inek.dataportal.enums.PiaType;
import org.inek.dataportal.enums.QualityUsage;
import org.inek.dataportal.enums.Region;
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
public class ValueLists {

    @Inject CostCenterFacade _costCenterFacade;
    @Inject CostTypeFacade _costTypeFacade;
    @Inject AdjustmentTypeFacade _adjustmentTypeFacade;

    List<SelectItem> _costCenters;
    public List<SelectItem> getCostCenters() {
        ensureCostCenters();
        return _costCenters;
    }

    private void ensureCostCenters() {
        if (_costCenters == null) {
            List<CostCenter> costCenters = _costCenterFacade.findAll();
            _costCenters = new ArrayList<>();
            SelectItem emptyItem = new SelectItem(-1, "");
            emptyItem.setNoSelectionOption(true);
            _costCenters.add(emptyItem);
            for (CostCenter costCenter : costCenters) {
                if (costCenter.getIsPsy()) {
                    _costCenters.add(new SelectItem(costCenter.getId(), costCenter.getCharId() + " " + costCenter.getText()));
                }
            }
        }
    }

    public int getCostCenterId(String charId) {
        ensureCostCenters();
        for (SelectItem item : _costCenters) {
            if (item.getLabel().startsWith(charId.toLowerCase() + " ")) {
                return (int) item.getValue();
            }
        }
        return -1;
    }

    List<SelectItem> _costTypes;
    public synchronized List<SelectItem> getCostTypes() {
        ensureCostTypes();
        return _costTypes;
    }

    private void ensureCostTypes() {
        if (_costTypes == null) {
            List<CostType> costTypes = _costTypeFacade.findAll();
            _costTypes = new ArrayList<>();
            SelectItem emptyItem = new SelectItem(-1, "");
            emptyItem.setNoSelectionOption(true);
            _costTypes.add(emptyItem);
            for (CostType costType : costTypes) {
                _costTypes.add(new SelectItem(costType.getId(), costType.getCharId() + " " + costType.getText()));
            }
        }
    }

    public int getCostTypeId(String charId) {
        ensureCostTypes();
        for (SelectItem item : _costTypes) {
            if (item.getLabel().startsWith(charId.toLowerCase() + " ")) {
                return (int) item.getValue();
            }
        }
        return -1;
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

}
