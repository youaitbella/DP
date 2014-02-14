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
                String name = getName(doc, field.getName());
                field.setAccessible(true);
                Object rawValue = field.get(o);
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
                String name = getName(doc, method.getName());
                method.setAccessible(true);
                Object rawValue = method.invoke(o);
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
        if (rawValue instanceof Boolean){
            rawValue = (boolean) rawValue ? "Ja" : "Nein"; // todo: replace by localized message
        }
        if (rawValue instanceof Date){
            rawValue = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(((Date) rawValue)); // todo: replace by localized message
        }
        String value = rawValue == null ? "" : rawValue.toString().replace((char) 7, '*');
        Long sorterKey = 1000L * doc.rank() + i;
        i++;
        if (value.length() > 0 || !doc.omitOnEmpty()) {
            sorter.put(sorterKey, new KeyValue<>(name, value));
        }
        return i;
    }

    
    public static String getMessage(String key) {
        FacesContext ctxt = FacesContext.getCurrentInstance();
        ResourceBundle messageBundle = ctxt.getApplication().getResourceBundle(ctxt, "msg");
        try {
            return messageBundle.getString(key);
        } catch (Exception e) {
            return "";
        }
    }

    private static String getName(Documentation doc, String defaultName) {
        String name = "";
        if (doc.key().length() > 0) {
            name = getMessage(doc.key());
        }
        if (name.isEmpty()) {
            if (doc.name().length() > 0) {
                name = doc.name();
            } else {
                name = defaultName;
                if (name.startsWith("_")) {
                    name = name.substring(1, 2).toUpperCase() + name.substring(2);
                }
            }
        }
        return name;
    }
}
