package org.inek.dataportal.common.helper;

import org.inek.dataportal.common.helper.structures.FieldValues;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author muellermi
 */
@SuppressWarnings("unchecked")
public class ObjectUtils {

    /**
     * @deprecated replace by call to ObjectCopier
     */
    @Deprecated
    public static <T> T copy(T original) {
        return ObjectCopier.copy(original);
    }

    /**
     * @deprecated replace by call to ObjectCopier
     */
    @Deprecated
    public static <T> void copyFieldValue(Field field, Object source, T target) {
        ObjectCopier.copyFieldValue(field, source, target);
    }

    /**
     * @deprecated replace by call to ObjectComparer
     */
    @Deprecated
    public static <T> Map<String, FieldValues> getDifferences(T obj1, T obj2) {
        return ObjectComparer.getDifferences(obj1, obj2, Collections.emptyList());
    }

    /**
     * @deprecated replace by call to ObjectComparer
     */
    @Deprecated
    public static <T> Map<String, FieldValues> getDifferences(T obj1, T obj2, List<Class> excludedTypes) {
        return ObjectComparer.getDifferences(obj1, obj2, excludedTypes);
    }

}
