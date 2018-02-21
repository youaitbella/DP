/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.inek.dataportal.common.enums.Right;

/**
 *
 * @author muellermi
 */
@Converter(autoApply = true)
public class RightConverter implements AttributeConverter<Right, String> {


    @Override
    public String convertToDatabaseColumn(Right right) {
        return right.getKey();
    }

    @Override
    public Right convertToEntityAttribute(String key) {
        return Right.getRightFromKey(key);
    }
}
