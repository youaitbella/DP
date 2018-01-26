/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;

/**
 *
 * @author muellermi
 */
@Converter(autoApply = true)
public class PsychTypeConverter implements AttributeConverter<PsychType, String> {

    @Override
    public String convertToDatabaseColumn(PsychType pschType) {
        return pschType.getKey();
    }

    @Override
    public PsychType convertToEntityAttribute(String key) {
        return PsychType.getTypeFromKey(key);
    }
}
