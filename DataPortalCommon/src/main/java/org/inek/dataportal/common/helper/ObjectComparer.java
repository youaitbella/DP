package org.inek.dataportal.common.helper;

import org.inek.dataportal.common.helper.structures.FieldValues;
import org.inek.dataportal.common.utils.IgnoreOnCompare;

import javax.persistence.Transient;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@SuppressWarnings("unchecked")
public class ObjectComparer {

    private final Logger LOGGER = Logger.getLogger("ObjectUtils");

    public static <T> Map<String, FieldValues> getDifferences(T obj1, T obj2) {
        return getDifferences(obj1, obj2, Collections.emptyList());
    }

    public static <T> Map<String, FieldValues> getDifferences(T obj1, T obj2, List<Class> excludedTypes) {
        ObjectComparer comparer = new ObjectComparer();
        return comparer.obtainDifferences(obj1, obj2, excludedTypes);
    }

    private ObjectComparer() {
    }

    private <T> Map<String, FieldValues> obtainDifferences(T obj1, T obj2, List<Class> excludedTypes) {
        Map<String, FieldValues> differences = new HashMap<>();

        if (obj1 == null || obj2 == null) {
            throw new IllegalArgumentException("Objects must not be null");
        }

        Class<?> type = obj1.getClass();
        if (type.isPrimitive()) {
            throw new IllegalArgumentException();
        } else if (type == String.class || type == Boolean.class || type.getSuperclass() == Number.class
                || type.getSuperclass() == Enum.class || type.getSimpleName().startsWith("XMLGregorianCalendar")) {
            throw new IllegalArgumentException();
        }

        if (type.isArray()) {
            throw new IllegalArgumentException();
        }

        if (obj1 instanceof List) {
            throw new IllegalArgumentException();
        }
        if (obj1 instanceof Map) {
            throw new IllegalArgumentException();
        }

        getDifferencesFromSuperClasses(obj1, obj2, excludedTypes, differences);
        return differences;
    }

    private <T> boolean areEqualObjects(T obj1, T obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }

        Class<?> type = obj1.getClass();
        if (type.isPrimitive()) {
            return obj1 == obj2;
        }
        if (isSimpleType(type)) {
            return obj1.equals(obj2);
        }

        if (type.isArray()) {
            Class dataType = type.getComponentType();
            if (dataType.isPrimitive()) {
                return areEqualArrays(obj1, obj2);
            }
        }

        if (obj1 instanceof List) {
            return areEqualLists((List) obj1, (List) obj2);
        }
        if (obj1 instanceof Map) {
            return areEqualMaps((Map) obj1, (Map) obj2);
        }

        return haveEqualSuperclasses(obj1, obj2);
    }

    private boolean isSimpleType(Class<?> type) {
        return type == String.class || type == Boolean.class || type == Date.class
                || type.getSuperclass() == Number.class || type.getSuperclass() == Enum.class
                || type.getSimpleName().startsWith("XMLGregorianCalendar");
    }

    private <T> boolean areEqualArrays(T obj1, T obj2) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        int len1 = Array.getLength(obj1);
        int len2 = Array.getLength(obj2);
        if (len1 != len2) {
            return false;
        }
        for (int i = 0; i < len1; i++) {
            if (!areEqualObjects(Array.get(obj1, i), Array.get(obj2, i))) {
                return false;
            }
        }
        return true;
    }

    private <T> boolean haveEqualSuperclasses(T obj1, T obj2) throws SecurityException {
        for (Class clazz = obj1.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (isFieldToIgnore(field, null)) {
                    continue;
                }
                if (!areEqualFields(field, obj1, obj2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private <T> void getDifferencesFromSuperClasses(
            T obj1,
            T obj2,
            List<Class> excludedTypes,
            Map<String, FieldValues> differences) throws SecurityException {
        for (Class clazz = obj1.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (isFieldToIgnore(field, excludedTypes)) {
                    continue;
                }
                if (!areEqualFields(field, obj1, obj2)) {
                    try {
                        field.setAccessible(true);
                        FieldValues fieldValues = new FieldValues(field, field.get(obj1), field.get(obj2));
                        differences.put(field.getName(), fieldValues);
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        // won't reach this due to check within areEqualFields
                    }
                }
            }
        }
    }

    private boolean isFieldToIgnore(Field field, List<Class> excludedTypes) {
        if (field.getName().startsWith("_persistence_")) {
            // ignore fields of JPA proxy (hopefully no other start with this prefix...) 
            // alternative: remember and check for all objects in object graph
            return true;
        }
        if (excludedTypes != null && excludedTypes.contains(field.getType())) {
            return true;
        }
        if (field.getAnnotation(Transient.class) != null) {
            return true;
        }
        if (field.getAnnotation(IgnoreOnCompare.class) != null) {
            return true;
        }
        return false;
    }

    private boolean areEqualLists(List list1, List list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!areEqualObjects(list1.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean areEqualMaps(Map map1, Map map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Object item : map1.entrySet()) {
            Entry entry = (Entry) item;
            Object key = ObjectCopier.copy(entry.getKey());
            Object value = ObjectCopier.copy(entry.getValue());
            if (!map2.containsKey(key)) {
                return false;
            }
            if (!areEqualObjects(value, map2.get(key))) {
                return false;
            }
        }
        return true;
    }

    private <T> boolean areEqualFields(Field field, T obj1, T obj2) {
        try {
            field.setAccessible(true);
            Object value1 = field.get(obj1);
            Object value2 = field.get(obj2);
            if (value1 == null && value2 == null) {
                return true;
            }
            if (value1 == null || value2 == null) {
                return false;
            }

            Class<?> type1 = value1.getClass();
            Class<?> type2 = value2.getClass();
            if (type1 != type2) {
                return false;
            }
            return areEqualObjects(value1, value2);

        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LOGGER.log(Level.WARNING, "Exception during areEqualFields");
        }
        return false;
    }
}
