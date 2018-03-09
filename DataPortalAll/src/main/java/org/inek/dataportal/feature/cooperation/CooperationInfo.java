/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.cooperation;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.SelectItem;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.enums.Feature;

/**
 *
 * @author muellermi
 */
public class CooperationInfo implements Serializable {

    private final Feature _feature;
    private final List<SelectItem> _selectItems;
    private final CooperationRight _right;

    public CooperationInfo(Feature feature, List<SelectItem> selectItems, CooperationRight right) {
        _feature = feature;
        _selectItems = selectItems;
        _right = right;
    }

    public Feature getFeature() {
        return _feature;
    }

    public List<SelectItem> getSelectItems() {
        return _selectItems;
    }

    public CooperationRight getRight() {
        return _right;
    }
}
