package org.inek.dataportal.common.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.inek.dataportal.common.specificfunction.entity.CenterName;

@FacesConverter(value = "CenterNameConverter")
public class CenterNameConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        CenterName center = new CenterName();
        try {
            int id = Integer.parseInt(value);
            center.setId(id);
        } catch (NumberFormatException e) {
            // ignore
        }
        return center;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        CenterName center = (CenterName) value;
        return "" + center.getId();
    }

}
