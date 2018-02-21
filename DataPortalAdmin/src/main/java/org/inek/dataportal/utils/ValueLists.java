package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.enums.Region;
import org.inek.dataportal.enums.RemunSystem;

/**
 *
 * @author muellermi
 */
@Named
@Singleton
public class ValueLists{

    public List getRemunerationDomains() {
        List<SelectItem> list = new ArrayList<>();
        for (RemunSystem val : RemunSystem.values()) {
            list.add(new SelectItem(val.getId(), val.getName()));
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
