/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.persistence.Id;

/**
 *
 * @author muellermi
 */
public class ObjectUtil {

    public static <T extends Object> T copyObject(Class<T> targetClass, Object source) {
        T target;
        try {
            target = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }

        for (Field field : source.getClass().getDeclaredFields()) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (field.getAnnotation(Id.class) != null) {
                continue;
            }
            String name = field.getName();
            if (name.startsWith("_persistence_")) {
                continue;
            }
            try {
                if (name.equals("serialVersionUID")) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(source);
                Field targetField = targetClass.getDeclaredField(name);
                targetField.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isPrimitive() || type == String.class || type == Boolean.class || type.getSuperclass() == Number.class) {
                    targetField.set(target, value);

                } else {
                    targetField.set(target, copyObject(field.getType(), value));
                }

            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                ex.getMessage();
            }
        }
        return target;
    }
}
