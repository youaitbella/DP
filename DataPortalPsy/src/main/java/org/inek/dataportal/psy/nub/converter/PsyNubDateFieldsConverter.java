package org.inek.dataportal.psy.nub.converter;

import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = false)
public class PsyNubDateFieldsConverter implements AttributeConverter<PsyNubDateFields, String> {

    @Override
    public String convertToDatabaseColumn(PsyNubDateFields status) {
        return status.name();
    }

    @Override
    public PsyNubDateFields convertToEntityAttribute(String value) {
        return PsyNubDateFields.valueOf(value);
    }
}
