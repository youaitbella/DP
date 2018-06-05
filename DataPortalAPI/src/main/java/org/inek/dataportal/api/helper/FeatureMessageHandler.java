package org.inek.dataportal.api.helper;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author muellermi
 */
public class FeatureMessageHandler {

    private static String obtainMessage(String key) {
        ResourceBundle messageBundle;
        messageBundle = ResourceBundle.getBundle("org.inek.dataportal.feature");
        return messageBundle.getString(key);
    }

    public static String getMessage(String key) {
        try {
            return obtainMessage(key);
        } catch (MissingResourceException e) {
            return "Unbekannter Text: " + key;
        }
    }

    public static String getMessageOrEmpty(String key) {
        try {
            return obtainMessage(key);
        } catch (MissingResourceException e) {
            return "";
        }
    }

}
