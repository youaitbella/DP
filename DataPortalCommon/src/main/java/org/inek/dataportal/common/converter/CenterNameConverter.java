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
        try {
            String[] parts = value.split("[|]");
            if (parts.length == 2) {
                int id = Integer.parseInt(parts[0]);
                return new CenterName(id, parts[1]);
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        return new CenterName();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        CenterName center = (CenterName) value;
        return center.getId() + "|" + center.getName();
    }

}
