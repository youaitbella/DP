/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muellermi
 */
public class ObjectLogger {

    private static final Logger LOGGER = Logger.getLogger("ObjectLogger");

    public static void logObject(Object o) {
        Level oldLevel = LOGGER.getLevel();
        LOGGER.setLevel(Level.INFO);
        for (Field field : o.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String value = field.get(o) == null ? "" : field.get(o).toString().replace((char) 7, '*');
                LOGGER.log(Level.INFO, "{0} ^ Key: {1} ^ Length: {2} ^ Value: {3}", new Object[]{o.getClass().getSimpleName(), field.getName(), value.toString().length(), value});

            } catch (IllegalArgumentException | IllegalAccessException ex) {
            }
            LOGGER.setLevel(oldLevel);

        }

    }
}