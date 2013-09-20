/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.maintenance;

import org.inek.dataportal.entities.AccountFeature;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class FeatureEditorDAO {

    private final AccountFeature _accFeature;
    private boolean _value;
    private final boolean _isEditable;

    FeatureEditorDAO(AccountFeature accFeature) {
        _accFeature = accFeature;
        _value = _accFeature.getFeature() == Feature.USER_MAINTENANCE || 
                accFeature.getFeatureState() != FeatureState.NEW  && accFeature.getFeatureState() != FeatureState.REJECTED;
        _isEditable = _accFeature.getFeature() != Feature.USER_MAINTENANCE && 
                (accFeature.getFeatureState() == FeatureState.NEW || accFeature.getFeatureState() == FeatureState.SIMPLE || accFeature.getFeatureState() == FeatureState.APPROVED);
    }

    public String getName() {
        return _accFeature.getFeature().name();
    }

    public String getShortText() {
        String name = _accFeature.getFeature().name();
        return Utils.getMessageOrEmpty("name" + name);
    }

    public String getDescription() {
        String name = _accFeature.getFeature().name();
        return Utils.getMessageOrEmpty("description" + name);
    }

    public boolean isValue() {
        return _value;
    }

    public void setValue(boolean value) {
        this._value = value;
    }
    
    public boolean isEditable(){
        return _isEditable;
    }

    public AccountFeature getAccFeature() {
        return _accFeature;
    }
    
}
