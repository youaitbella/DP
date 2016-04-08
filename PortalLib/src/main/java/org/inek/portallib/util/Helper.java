/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.util;

/**
 *
 * @author muellermi
 */
public class Helper {

    public static String collectException(Throwable exception, int level) {
        StringBuilder collector = new StringBuilder();
        if (collector.length() > 0) {
            collector.append("\r\n\r\n--------------------------------\r\n");
        }
        collector.append("Level: ").append(level).append("\r\n\r\n");
        collector.append(exception.getMessage()).append("\r\n\r\n");
        for (StackTraceElement element : exception.getStackTrace()) {
            collector.append(element.toString()).append("\r\n");
        }
        Throwable cause = exception.getCause();
        if (cause != null && level < 9) {
            collector.append(collectException(cause, level + 1));
        }
        return collector.toString();
    }
    
}
