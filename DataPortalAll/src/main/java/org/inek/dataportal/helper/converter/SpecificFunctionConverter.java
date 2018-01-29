/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunction;

/**
 *
 * @author muellermi
 */
@FacesConverter(value = "SpecificFunctionConverter")
public class SpecificFunctionConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            String[] parts = value.split("[|]");
            if (parts.length == 2) {
                int id = Integer.parseInt(parts[0]);
                return new SpecificFunction(id, parts[1]);
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        return new SpecificFunction();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        SpecificFunction specificFunction = (SpecificFunction) value;
        return specificFunction.getId() + "|" + specificFunction.getText();
    }

}
