package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.common.CostCenter;
import org.inek.dataportal.entities.common.CostType;
import org.inek.dataportal.facades.common.CostCenterFacade;
import org.inek.dataportal.facades.common.CostTypeFacade;

/**
 *
 * @author muellermi
 */
@Named
@Singleton
public class ValueLists {

    @Inject CostCenterFacade _costCenterFacade;
    @Inject CostTypeFacade _costTypeFacade;

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
            for (CostCenter costCenter : costCenters){
                _costCenters.add(new SelectItem(costCenter.getId(), costCenter.getCharId() + " " + costCenter.getText()));
            }
        }
    }

    public int getCostCenterId(String charId){
        ensureCostCenters();
        for (SelectItem item : _costCenters){
            if (item.getLabel().startsWith(charId.toLowerCase() + " ")){
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
            for (CostType costType : costTypes){
                _costTypes.add(new SelectItem(costType.getId(), costType.getCharId() + " " + costType.getText()));
            }
        }
    }

    public int getCostTypeId(String charId){
        ensureCostTypes();
        for (SelectItem item : _costTypes){
            if (item.getLabel().startsWith(charId.toLowerCase() + " ")){
                return (int) item.getValue();
            }
        }
        return -1;
    }
}
