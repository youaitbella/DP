package org.inek.dataportal.psy.modelintention;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.psy.modelintention.entities.AdjustmentType;
import org.inek.dataportal.psy.modelintention.enums.HospitalType;
import org.inek.dataportal.psy.modelintention.enums.InsuranceAffiliation;
import org.inek.dataportal.psy.modelintention.enums.MedicalAttribute;
import org.inek.dataportal.psy.modelintention.enums.QualityUsage;
import org.inek.dataportal.psy.modelintention.enums.Region;
import org.inek.dataportal.psy.modelintention.enums.TreatmentType;
import org.inek.dataportal.psy.modelintention.enums.PiaType;
import org.inek.dataportal.psy.modelintention.enums.SettleType;
import org.inek.dataportal.psy.modelintention.facades.AdjustmentTypeFacade;

/**
 *
 * @author muellermi
 */
@Named
@Singleton
public class ModelIntentionLists{

    @Inject private AdjustmentTypeFacade _adjustmentTypeFacade;

    private List<SelectItem> _adjustmentTypes;

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
