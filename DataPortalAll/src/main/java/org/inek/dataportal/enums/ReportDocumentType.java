/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum ReportDocumentType {
    PDF("PDF-Dokument", "00000000-0000-0000-0000-000000000000"),
    XLSX("Excel-Datei", "B3CA7A14-59B3-4BE9-AF5F-85D8D2BCBA76");

    ReportDocumentType(String description, String uuid) {
        _description = description;
        _uuid = uuid;
    }
    private final String _uuid;
    private final String _description;

    public String getDescription() {
        return _description;
    }

    public String getUuid() {
        return _uuid;
    }

    public String getExtension() {
        return "." + name().toLowerCase();
    }

}
