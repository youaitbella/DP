package org.inek.dataportal.common.data.converter;

import org.inek.dataportal.api.enums.Function;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class FunctionConverter implements AttributeConverter<Function, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Function function) {
        return function.getId();
    }

    @Override
    public Function convertToEntityAttribute(Integer id) {
        return Function.fromId(id);
    }
}
