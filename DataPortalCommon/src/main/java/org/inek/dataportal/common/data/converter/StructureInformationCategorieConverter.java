package org.inek.dataportal.common.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.inek.dataportal.common.enums.StructureInformationCategorie;

/**
 *
 * @author muellermi
 */
@Converter(autoApply = false)
public class StructureInformationCategorieConverter implements AttributeConverter<StructureInformationCategorie, String> {

    @Override
    public String convertToDatabaseColumn(StructureInformationCategorie structureCategorie) {
        return structureCategorie.toString();
    }

    @Override
    public StructureInformationCategorie convertToEntityAttribute(String name) {
        return StructureInformationCategorie.valueOf(name);
    }
}
