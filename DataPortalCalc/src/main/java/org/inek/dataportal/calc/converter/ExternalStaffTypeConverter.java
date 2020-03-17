package org.inek.dataportal.calc.converter;

import org.inek.dataportal.calc.enums.ExternalStaffType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class ExternalStaffTypeConverter implements AttributeConverter<ExternalStaffType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ExternalStaffType staffType) {
        return staffType.getId();
    }

    @Override
    public ExternalStaffType convertToEntityAttribute(Integer id) {
        return ExternalStaffType.fromId(id);
    }
}

/*
@Converter(autoApply = false)
public class StructuralChangesMarkerConverter implements AttributeConverter<StructuralChangesMarker, Integer> {
    @Override
    public Integer convertToDatabaseColumn(StructuralChangesMarker structuralChangesMarker) {
        return structuralChangesMarker.getId();
    }

    @Override
    public StructuralChangesMarker convertToEntityAttribute(Integer id) {
        return StructuralChangesMarker.fromId(id);
    }
}

 */
