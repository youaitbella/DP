/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.inek.dataportal.feature.psychstaff.entity.ExclusionFact;

/**
 *
 * @author muellermi
 */
@FacesConverter(value = "ExclusionFactConverter")
public class ExclusionFactConverter implements Converter {

    private static Logger LOGGER = Logger.getLogger(ExclusionFactConverter.class.getName());

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            ExclusionFact exclusionFact = mapper.readValue(value, ExclusionFact.class);
            return exclusionFact;  // sadly this object is new and without any JPA context - will be stored as additional entry
            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value == null) {
                return "";
            }
            ExclusionFact exclusionFact = (ExclusionFact) value;
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(exclusionFact);
        } catch (JsonProcessingException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return "";
        }
    }

}
