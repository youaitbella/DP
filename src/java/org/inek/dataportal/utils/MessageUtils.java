/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author muellermi
 */
public class MessageUtils {

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
