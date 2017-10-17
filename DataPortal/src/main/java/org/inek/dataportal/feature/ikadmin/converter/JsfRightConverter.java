/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.ikadmin.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.inek.dataportal.feature.ikadmin.enums.Right;

/**
 *
 * @author muellermi
 */
@FacesConverter(value = "JsfRightConverter")
public class JsfRightConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return Right.getRightFromKey(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof String) {
            String key = (String) value;
            Right.getRightFromKey(key);
            return key;
        }
        return ((Right) value).getKey();
    }

}
