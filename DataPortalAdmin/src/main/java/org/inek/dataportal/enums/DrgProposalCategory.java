/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum DrgProposalCategory {
    UNKNOWN,
    SYSTEM, // Vorschlag zur Systementwicklung PEPP
    SUPPLEMENTARY, // Vorschlag zum Zusatzentgelt
    POLICY, // Vorschlag zu den Kodierrichtlinien Psychiatrie und Psychosomatik
    CODES, // Weiterentwicklung Leistungsbezeichner
    CCL,
    CALCULATION, // Vorschlag zur Kalkulation
    OTHER;          // Sonstiger Vorschlag
}
