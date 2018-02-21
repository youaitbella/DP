package org.inek.dataportal.common.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.inek.dataportal.common.enums.Feature;

/**
 *
 * @author muellermi
 */
@Converter(autoApply = false)
public class FeatureConverter implements AttributeConverter<Feature, Integer> {


    @Override
    public Integer convertToDatabaseColumn(Feature feature) {
        return feature.getId();
    }

    @Override
    public Feature convertToEntityAttribute(Integer id) {
        return Feature.getFeatureFromId(id);
    }
}
