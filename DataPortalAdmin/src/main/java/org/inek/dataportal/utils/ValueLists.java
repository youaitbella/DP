package org.inek.dataportal.utils;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.inek.dataportal.common.enums.Feature;

/**
 *
 * @author muellermi
 */
@Named
@Singleton
public class ValueLists{

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
