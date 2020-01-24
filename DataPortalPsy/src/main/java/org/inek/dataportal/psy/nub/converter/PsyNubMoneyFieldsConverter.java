package org.inek.dataportal.psy.nub.converter;

import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = false)
public class PsyNubMoneyFieldsConverter implements AttributeConverter<PsyNubMoneyFields, String> {


    @Override
    public String convertToDatabaseColumn(PsyNubMoneyFields status) {
        return status.name();
    }

    @Override
    public PsyNubMoneyFields convertToEntityAttribute(String value) {
        return PsyNubMoneyFields.valueOf(value);
    }
}
