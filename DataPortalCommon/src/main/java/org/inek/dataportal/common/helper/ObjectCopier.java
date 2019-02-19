package org.inek.dataportal.common.helper;

import java.io.*;
import java.lang.reflect.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@SuppressWarnings("unchecked")
public final class ObjectCopier {

    private static final Logger LOGGER = Logger.getLogger("ObjectUtils");
    private Map<Object, Object> knownObjects = new HashMap<>();

    /**
     * Performs a deep copy of object original.
     *
     * @param <T>
     * @param original
     * @return
     */
    public static <T> T copy(T original) {
        ObjectCopier copier = new ObjectCopier();
        return copier.performCopy(original);
    }

    public static <T> void copyFieldValue(Field field, Object source, T target) {
        ObjectCopier copier = new ObjectCopier();
        copier.performCopyFieldValue(field, source, target);
    }

    private ObjectCopier() {
    }

    private <T> T performCopy(T original) {
        boolean isSerializable = false;
        // checks, whether class implements interface Serializable
        // todo: check if contained objects are serializable too
        for (Class c : original.getClass().getInterfaces()) {
            if (c == Serializable.class) {
                isSerializable = true;
                break;
            }
        }
        if (isSerializable) {
            return streamCopy(original);
        }
        return copyObject(original);

    }

    private <T> T copyObject(T source) {
        if (source == null) {
            return null;
        }
        Class<?> type = source.getClass();
        if (type == Date.class) {
            Date target = new Date(((Date) source).getTime());
            return (T) target;
        }
        if (type.isPrimitive() || type == String.class || type == Boolean.class || type.getSuperclass() == Number.class
                || type.getSuperclass() == Enum.class || type.getSimpleName().startsWith("XMLGregorianCalendar")) {
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

    private <T> T streamCopy(T original) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(original);
                out.flush();
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                @SuppressWarnings("unchecked") T clone = (T) ois.readObject();
                return clone;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("error during copy", e);
        }
    }

    private <T> void performCopyFieldValue(Field field, Object source, T target) {
        try {
            String name = field.getName();
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                return;
            }
            if (Modifier.isTransient(field.getModifiers())) {
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
            LOGGER.log(Level.SEVERE, "error during setField: " + field.getName());
        }
    }


    /**
     * performs a deep copy from object source into target
     *
     * @param <T>
     * @param source
     * @param target
     * @return
     */
    private <T> T copyObject(T source, T target) {
        if (source == null || target == null) {
            return null;
        }
        if (source instanceof List) {
            return copyList(source, target);
        }
        if (source instanceof Map) {
            return copyMap(source, target);
        }

        return copyFields(source, target);
    }

    private <T> T copyFields(T source, T target) {
        if (knownObjects.containsKey(source)) {
            return (T) knownObjects.get(source);
        }
        knownObjects.put(source, target);
        try {
            for (Class clazz = source.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
                for (Field field : clazz.getDeclaredFields()) {
                    performCopyFieldValue(field, source, target);
                }
            }
            return target;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error during copyObject", e);
            throw e;
        }
    }

    private <T> T copyList(T source, T target) {
        for (Object o : (List) source) {
            Object item = copyObject(o);
            ((List) target).add(item);
        }
        return target;
    }

    private <T> T copyMap(T source, T target) {
        for (Object item : ((Map) source).entrySet()) {
            Entry entry = (Entry) item;
            Object key = copyObject(entry.getKey());
            Object value = copyObject(entry.getValue());
            ((Map) target).put(key, value);
        }
        return target;
    }

    private <T> T createTarget(T source) {
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
            if (constructor == null) {
                LOGGER.log(Level.INFO, "Constructor is null: {0}", targetClass.getName());
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

    private Object[] getDummyParameters(Class[] types) throws UnsupportedOperationException {
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

}
