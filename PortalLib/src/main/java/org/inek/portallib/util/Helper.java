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
            collector.append(NEW_LINE)
                    .append(NEW_LINE)
                    .append("--------------------------------")
                    .append(NEW_LINE);
        }
        collector.append("Level: ").append(level).append(NEW_LINE).append(NEW_LINE);
        collector.append(exception.getMessage()).append(NEW_LINE).append(NEW_LINE);
        for (StackTraceElement element : exception.getStackTrace()) {
            collector.append(element.toString()).append(NEW_LINE);
        }
        Throwable cause = exception.getCause();
        if (cause != null && level < 9) {
            collector.append(collectException(cause, level + 1));
        }
        return collector.toString();
    }
    private static final String NEW_LINE = "\r\n";

    public static String getContentType(String name) {
        String ext = name.substring(1 + name.lastIndexOf(".")).toLowerCase();
        switch (ext) {
            case "jar":
                return "application/java-archive";
            case "zip":
                return "application/zip";
            case "7z":
                return "application/x-7z-compressed";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/powerpoint";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "pdf":
                return "application/pdf";
            case "exe":
                return "application/octet-stream";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpeg";
            case "xml":
                return "text/xml";
            case "csv":
                return "text/csv";
            case "gpg":
                return "pgp/mime";
            default:
                return "text/plain";
        }
    }
    
}
