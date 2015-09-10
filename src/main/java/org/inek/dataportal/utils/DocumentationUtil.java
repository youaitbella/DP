package org.inek.dataportal.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class DocumentationUtil {

    private final static String KeyNotFound = "### key not found";

    public static List<KeyValueLevel> getDocumentation(Object o) {
        DocumentationUtil docUtil = new DocumentationUtil();
        docUtil.documentObject(o);
        return docUtil.getFieldValues();
    }

    public static Map<String, String> getFieldTranslationMap(Object o) {
        DocumentationUtil docUtil = new DocumentationUtil();
        return docUtil.getFieldTranslations(o);
    }

    private DocumentationUtil() {
    }

    private final Map<Long, KeyValueLevel> _sorter = new TreeMap<>();
    private int _position = 0;

    private Map<String, String> getFieldTranslations(Object o) {
        Map<String, String> translations = new HashMap<>();
        for (Field field : o.getClass().getDeclaredFields()) {
            Documentation doc = field.getAnnotation(Documentation.class);
            if (doc == null) {
                translations.put(field.getName(), field.getName());
                continue;
            }
            translations.put(field.getName(), getName(doc, field.getName()));
        }
        return translations;
    }

    private List<KeyValueLevel> getFieldValues() {
        List<KeyValueLevel> fieldValues = new ArrayList<>();
        for (KeyValueLevel kv : _sorter.values()) {
            fieldValues.add(kv);
        }
        return fieldValues;
    }

    private void documentObject(Object o) {
        for (Field field : o.getClass().getDeclaredFields()) {
            Documentation doc = field.getAnnotation(Documentation.class);
            if (doc == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object rawValue = field.get(o);
                docElement(doc, field.getName(), rawValue);
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
                docElement(doc, method.getName(), rawValue);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            }
        }
    }

    private void docElement(Documentation doc, String fieldName, Object rawValue) {
        String name = getName(doc, fieldName);
        if (rawValue instanceof Collection) {
            //addDoc(name, "", doc, 0);
            documentCollection(doc, name, (Collection) rawValue);
        } else {
            String value = translate(rawValue, doc);
            addDoc(name, value, doc, 0);
        }
    }

    private void documentCollection(Documentation doc, String name, Collection collection) {
        int counter = 0;
        for (Object entry : collection) {
            counter++;
            addDoc(name + " (" + counter + ")", getDocForSubObject(entry), doc, 1);
        }
    }

    private List<KeyValueLevel> getDocForSubObject(Object o) {
        List<KeyValueLevel> subList = new ArrayList<>();
        for (Field field : o.getClass().getDeclaredFields()) {
            Documentation doc = field.getAnnotation(Documentation.class);
            if (doc == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object rawValue = field.get(o);
                subList.add(new KeyValueLevel(getName(doc, field.getName()), translate(rawValue, doc), 1));
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
                subList.add(new KeyValueLevel(getName(doc, method.getName()), translate(rawValue, doc), 1));
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            }
        }
        return subList;
    }

    private void addDoc(String name, Object value, Documentation doc, int level) {
        Long sorterKey = 1000L * doc.rank() + _position;
        _position++;
        if ((value.toString().length() > 0 || !doc.omitOnEmpty()) && !doc.omitAlways()) {
            _sorter.put(sorterKey, new KeyValueLevel<>(name, value, level));
        }
    }

    private String translate(Object rawValue, Documentation doc) {
        if (rawValue instanceof Boolean) {
            return (boolean) rawValue ? "Ja" : "Nein"; // todo: replace by localized message
        }
        if (rawValue instanceof Date) {
            return new SimpleDateFormat(doc.dateFormat()).format(((Date) rawValue)); // todo: replace by localized message
        }

        if (rawValue instanceof BigDecimal) {
            if (doc.isMoneyFormat()) {
                DecimalFormat decim = new DecimalFormat("0.00");
                return decim.format(rawValue).toString() + " €";
            } else {
                return rawValue.toString();
            }
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
                    String translatedValue = Utils.getMessage(key);
                    if (!translatedValue.startsWith(KeyNotFound)) {
                        return translatedValue;
                    }
                    break;
                }
            }
        }
        return value;
    }

    private static String getName(Documentation doc, String defaultName) {
        if (doc.key().length() > 0) {
            return Utils.getMessage(doc.key());
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
