package org.inek.dataportal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.faces.context.FacesContext;

/**
 *
 * @author muellermi
 */
public class DocumentationUtil {

    private final static String KeyNotFound = "### key not found";

    public static List<KeyValue> getDocumentation(Object o) {
        Map<Long, KeyValue> sorter = new TreeMap<>();
        List<KeyValue> fieldValues = new ArrayList<>();
        int i = 0;
        for (Field field : o.getClass().getDeclaredFields()) {
            Documentation doc = field.getAnnotation(Documentation.class);
            if (doc == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object rawValue = field.get(o);
                String name = getName(doc, field.getName());
                i = addDoc(rawValue, doc, i, sorter, name);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
            }
        }
        for (Method method : o.getClass().getMethods()) {
            Documentation doc = method.getAnnotation(Documentation.class);
            if (doc == null) {
                continue;
            }
            try {
                method.setAccessible(true);
                Object rawValue = method.invoke(o);
                String name = getName(doc, method.getName());
                i = addDoc(rawValue, doc, i, sorter, name);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            }

        }
        for (KeyValue kv : sorter.values()) {
            fieldValues.add(kv);
        }
        return fieldValues;
    }

    private static int addDoc(Object rawValue, Documentation doc, int i, Map<Long, KeyValue> sorter, String name) {
        String value;
        if (rawValue instanceof Boolean) {
            value = (boolean) rawValue ? "Ja" : "Nein"; // todo: replace by localized message
        } else if (rawValue instanceof Date) {
            value = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(((Date) rawValue)); // todo: replace by localized message
        } else {
            value = rawValue == null ? "" : rawValue.toString().replace((char) 7, '*');
            value = translateValue(doc, value);
        }
        Long sorterKey = 1000L * doc.rank() + i;
        i++;
        if (value.length() > 0 || !doc.omitOnEmpty()) {
            sorter.put(sorterKey, new KeyValue<>(name, value));
        }
        return i;
    }

    private static String getMessage(String key) {
        FacesContext ctxt = FacesContext.getCurrentInstance();
        ResourceBundle messageBundle = ctxt.getApplication().getResourceBundle(ctxt, "msg");
        try {
            return messageBundle.getString(key);
        } catch (Exception e) {
            return KeyNotFound + " " + key;
        }
    }

    private static String translateValue(Documentation doc, String value) {
        if (doc.translateValue().length() > 0) {
            String[] pairs = doc.translateValue().split(";");
            for (String pair : pairs) {
                int pos = pair.indexOf("=");
                if (pos > 0 && pair.length() > pos + 1) {
                    String val = pair.substring(0, pos).trim();
                    String key = pair.substring(pos + 1).trim();
                    if (val.equals(value)) {
                        String translatedValue = getMessage(key);
                        if (!translatedValue.startsWith(KeyNotFound)) {
                            return translatedValue;
                        }
                        break;
                    }
                }
            }
        }
        return value;
    }

    private static String getName(Documentation doc, String defaultName) {
        if (doc.key().length() > 0) {
            return getMessage(doc.key());
        }
        if (doc.name().length() > 0) {
            return doc.name();
        }
        if (defaultName.startsWith("_")) {
            return defaultName.substring(1, 2).toUpperCase() + defaultName.substring(2);
        }
        return defaultName;
    }

}
