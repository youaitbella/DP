package org.inek.dataportal.psy.nub.converter;

import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = false)
public class PsyNubNumberFieldsConverter implements AttributeConverter<PsyNubNumberFields, String> {

    @Override
    public String convertToDatabaseColumn(PsyNubNumberFields status) {
        return status.name();
    }

    @Override
    public PsyNubNumberFields convertToEntityAttribute(String value) {
        return PsyNubNumberFields.valueOf(value);
    }
}
