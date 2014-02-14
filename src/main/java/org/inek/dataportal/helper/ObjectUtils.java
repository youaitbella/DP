package org.inek.dataportal.helper;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muellermi
 */
public class ObjectUtils {

    private static final Logger logger = Logger.getLogger("ObjectUtils");

    public static <T> T copy(T original) {
        boolean isSerializable = false;
        for (Class c : original.getClass().getInterfaces()) {
            if (c == Serializable.class) {
                isSerializable = true;
                break;
            }
        }
        if (!isSerializable) {
            return copyObject(original);
        }

        T clone = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(original);
                out.flush();
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                clone = (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("error during copy", e);
        }
        return clone;
    }

    /**
     * performs a deep copy of object source
     *
     * @param <T>
     * @param source
     * @return
     */
    public static <T> T copyObject(T source) {
        if (source == null) {
            return null;
        }
        Class<?> type = source.getClass();
        if (type == Date.class) {
            Date target =  new Date(((Date)source).getTime());
            return (T) target;
        }
        if (type.isPrimitive() || type == String.class || type == Boolean.class || type.getSuperclass() == Number.class || 
                type.getSuperclass() == Enum.class || type.getSimpleName().startsWith("XMLGregorianCalendar")) {
            return source;
        }
        if (type.isArray()) {
            Class dataType = type.getComponentType();
            if (dataType.isPrimitive()) {
                int len = Array.getLength(source);
                Object target = Array.newInstance(dataType, len);
                System.arraycopy(source, 0, target, 0, len);
                return (T) target;
            }
        }
        return copyObject(source, createTarget(source));
    }

    /**
     * performs a deep copy from object source into target
     *
     * @param <T>
     * @param source
     * @param target
     * @return
     */
    public static <T> T copyObject(T source, T target) {
        try {
            if (source == null || target == null) {
                return null;
            }
            if (source instanceof List) {
                return copyList(source, target);
            }
            if (source instanceof Map) {
                return copyMap(source, target);
            }

            for (Class clazz = source.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
                for (Field field : clazz.getDeclaredFields()) {
                    setField(field, source, target);
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error during copyObject", e);
            throw e;
        }
        return target;
    }

    private static <T> T copyList(T source, T target) {
        for (Object o : (List) source) {
            Object item = copyObject(o);
            ((List) target).add(item);
        }
        return target;
    }

    private static <T> T copyMap(T source, T target) {
        for (Object item : ((Map) source).entrySet()) {
            Entry entry = (Entry) item;
            Object key = copyObject(entry.getKey());
            Object value = copyObject(entry.getValue());
            ((Map) target).put(key, value);
        }
        return target;
    }

    private static <T> T createTarget(T source) {
        T target = null;
        Class<T> targetClass = (Class<T>) source.getClass();
        try {
            target = targetClass.newInstance();
        } catch (IllegalAccessException ex) {
            return null;
        } catch (InstantiationException ex) {
            // no default ctor
        }

        if (target == null) {
            Constructor<?>[] ctors = targetClass.getDeclaredConstructors();
            int minArgs = Integer.MAX_VALUE;
            Constructor constructor = null;
            for (Constructor ctor : ctors) {
                Class[] types = ctor.getParameterTypes();
                if (types.length < minArgs) {
                    constructor = ctor;
                    minArgs = types.length;
                }
            }
            if (constructor == null){
                System.out.println(targetClass.getName());
            }
            constructor.setAccessible(true);
            Class[] types = constructor.getParameterTypes();
            Object[] params = getDummyParameters(types);
            try {
                target = (T) constructor.newInstance(params);
            } catch (InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
                return null;
            }
        }
        return target;
    }

    private static <T> void setField(Field field, Object source, T target) {
        try {
            String name = field.getName();
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())){
                return;
            }
            Object value = field.get(source);
            Field targetField = target.getClass().getDeclaredField(name);
            targetField.setAccessible(true);
            if (value == null) {
                targetField.set(target, null);
                return;
            }
            targetField.set(target, copyObject(value));

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            logger.log(Level.SEVERE, "error during setField");
        }
    }

    private static Object[] getDummyParameters(Class[] types) throws UnsupportedOperationException {
        Object[] params = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i].isPrimitive()) {
                switch (types[i].getSimpleName()) {
                    case "int":
                    case "long":
                    case "float":
                    case "double":
                        params[i] = 0;
                        break;
                    case "boolean":
                        params[0] = false;
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            } else {
                params[i] = null;
            }
        }
        return params;
    }

    public static <T> boolean areEqualObjects(T obj1, T obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }

        Class<?> type = obj1.getClass();
        if (type.isPrimitive()) {
            return obj1 == obj2;
        } else if (type == String.class || type == Boolean.class || type.getSuperclass() == Number.class || 
                type.getSuperclass() == Enum.class || type.getSimpleName().startsWith("XMLGregorianCalendar")) {
            return obj1.equals(obj2);
        }

        if (type.isArray()) {
            Class dataType = type.getComponentType();
            if (dataType.isPrimitive()) {
                int len1 = Array.getLength(obj1);
                int len2 = Array.getLength(obj2);
                if (len1 != len2){return false;}
                for (int i = 0; i < len1; i++){
                    if (!areEqualObjects(Array.get(obj1, i), Array.get(obj2, i))){
                        return false;
                    }
                    
                }
                return true;
            }
        }
        
        if (obj1 instanceof List) {
            return areEqualLists((List) obj1, (List) obj2);
        }
        if (obj1 instanceof Map) {
            return areEqualMaps((Map) obj1, (Map) obj2);
        }

        for (Class clazz = obj1.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!areEqualFields(field, obj1, obj2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean areEqualLists(List list1, List list2) {
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

    private static boolean areEqualMaps(Map map1, Map map2) {
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Object item : map1.entrySet()) {
            Entry entry = (Entry) item;
            Object key = copyObject(entry.getKey());
            Object value = copyObject(entry.getValue());
            if (!map2.containsKey(key)) {
                return false;
            }
            if (!areEqualObjects(value, map2.get(key))) {
                return false;
            }
        }
        return true;
    }

    private static <T> boolean areEqualFields(Field field, T obj1, T obj2) {
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
            logger.log(Level.WARNING, "Exception during areEqualFields");
        }
        return false;
    }
}
