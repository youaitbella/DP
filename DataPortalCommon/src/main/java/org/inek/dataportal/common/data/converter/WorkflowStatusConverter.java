package org.inek.dataportal.common.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Converter(autoApply = false)
public class WorkflowStatusConverter implements AttributeConverter<WorkflowStatus, Integer> {


    @Override
    public Integer convertToDatabaseColumn(WorkflowStatus status) {
        return status.getId();
    }

    @Override
    public WorkflowStatus convertToEntityAttribute(Integer id) {
        return WorkflowStatus.fromValue(id);
    }
}
