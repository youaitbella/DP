/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 *
 * @author muellermi
 */
public class MessageUtils {

    /**
     * Retrieves a set of supported languages inclusive default
     *
     * @return
     */
    public static Set<String> getSupportedLanguages() {
        Application app = FacesContext.getCurrentInstance().getApplication();
        Set<String> languageCodes = new HashSet<>();
        for (Iterator<Locale> itr = app.getSupportedLocales(); itr.hasNext();) {
            Locale locale = itr.next();
            languageCodes.add(locale.getLanguage());
        }

        String defaultLang = app.getDefaultLocale().getLanguage();
        // We need to include the default language
        // cause it may or may not defined as a supported language.
        languageCodes.add(defaultLang);
        return languageCodes;
    }

    public static String getMessage(String key) {
        return getMessage(key, null);
    }

    public static String getMessage(String key, String languageCode) {
        ResourceBundle messageBundle;
        if (languageCode == null || languageCode.length() == 0) {
            messageBundle = ResourceBundle.getBundle("messages");
        } else {
            Locale locale = new Locale(languageCode);
            messageBundle = ResourceBundle.getBundle("messages", locale);
        }
        return messageBundle.getString(key);
    }
}
