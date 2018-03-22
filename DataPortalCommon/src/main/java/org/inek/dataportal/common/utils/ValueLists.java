package org.inek.dataportal.common.utils;

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
import org.inek.dataportal.common.data.access.CostCenterFacade;
import org.inek.dataportal.common.data.access.CostTypeFacade;
import org.inek.dataportal.common.data.common.CostCenter;
import org.inek.dataportal.common.data.common.CostType;
import org.inek.dataportal.common.enums.Genders;
import org.inek.dataportal.common.enums.RemunSystem;

/**
 *
 * @author muellermi
 */
@Named
@Singleton
public class ValueLists{

    @Inject private CostCenterFacade _costCenterFacade;
    @Inject private CostTypeFacade _costTypeFacade;

    @PostConstruct
    private void init() {
        loadCostCenters();

    }

    private List<CostCenter> _costCenters;
    private List<CostType> _costTypes;

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

    public List getRemunerationDomains() {
        List<SelectItem> list = new ArrayList<>();
        for (RemunSystem val : RemunSystem.values()) {
            list.add(new SelectItem(val.getId(), val.getName()));
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

}
