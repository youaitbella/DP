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

    private static final Logger _logger = Logger.getLogger("ObjectLogger");

    public static void logObject(Object o) {
        Level oldLevel = _logger.getLevel();
        _logger.setLevel(Level.INFO);
        for (Field field : o.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String value = field.get(o) == null ? "" : field.get(o).toString().replace((char) 7, '*');
                _logger.log(Level.INFO, "{0} ^ Key: {1} ^ Length: {2} ^ Value: {3}", new Object[]{o.getClass().getSimpleName(), field.getName(), value.toString().length(), value});

            } catch (IllegalArgumentException | IllegalAccessException ex) {
            }
            _logger.setLevel(oldLevel);

        }

    }
}