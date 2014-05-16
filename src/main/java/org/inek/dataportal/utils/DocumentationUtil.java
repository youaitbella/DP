package org.inek.dataportal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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

    public static List<KeyValueLevel> getDocumentation(Object o) {
        DocumentationUtil docUtil = new DocumentationUtil();
        docUtil.documentObject(o, 0);
        return docUtil.getFieldValues();
    }

    private DocumentationUtil() {
    }

    private final Map<Long, KeyValueLevel> _sorter = new TreeMap<>();
    private int _position = 0;

    public List<KeyValueLevel> getFieldValues() {
        List<KeyValueLevel> fieldValues = new ArrayList<>();
        for (KeyValueLevel kv : _sorter.values()) {
            fieldValues.add(kv);
        }
        return fieldValues;
    }

    public void documentObject(Object o, int level) {
        for (Field field : o.getClass().getDeclaredFields()) {
            Documentation doc = field.getAnnotation(Documentation.class);
            if (doc == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object rawValue = field.get(o);
                docElement(doc, field.getName(), rawValue, level);
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
                docElement(doc, method.getName(), rawValue, level);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            }

        }
    }

    private void docElement(Documentation doc, String fieldName, Object rawValue, int level) {
        String name = getName(doc, fieldName);
        if (rawValue instanceof Collection) {
            addDoc("", doc, name, level);
            documentCollection(doc, (Collection) rawValue, level + 1);
        } else {
            String value = translate(rawValue, doc);
            addDoc(value, doc, name, level);
        }
    }

    private void documentCollection(Documentation doc, Collection collection, int level) {
        for (Object entry : collection) {
//            documentObject(entry, level);
            addDoc(flatDocumentObject(entry), doc, "", level);
        }
    }

    private String flatDocumentObject(Object o) {
        String line = "";
        for (Field field : o.getClass().getDeclaredFields()) {
            Documentation doc = field.getAnnotation(Documentation.class);
            if (doc == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object rawValue = field.get(o);
                line = addField(line, getName(doc, field.getName()), rawValue);
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
                line = addField(line, getName(doc, method.getName()), rawValue);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            }
        }
        return line;
    }

    private String addField(String line, String fieldName, Object rawValue) {
        if (line.length() > 0) {
            line += "; ";
        }
        line += fieldName + " = " + rawValue;
        return line;
    }

    private void addDoc(String value, Documentation doc, String name, int level) {
        Long sorterKey = 1000L * doc.rank() + _position;
        _position++;
        if (value.length() > 0 || !doc.omitOnEmpty()) {
            _sorter.put(sorterKey, new KeyValueLevel<>(name, value, level));
        }
    }

    private String translate(Object rawValue, Documentation doc) {
        if (rawValue instanceof Boolean) {
            return (boolean) rawValue ? "Ja" : "Nein"; // todo: replace by localized message
        }
        if (rawValue instanceof Date) {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(((Date) rawValue)); // todo: replace by localized message
        }

        String value = rawValue == null ? "" : rawValue.toString().replace((char) 7, '*');
        if (doc.translateValue().length() == 0) {
            return value;
        }
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
        return value;
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
