package org.inek.dataportal.care.entities.StructuralChanges;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
