/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

import org.inek.dataportal.helper.Utils;

/**
 *
 * @author schlappajo
 */
public enum QualityUsage {

    None(0, "enmNone"),
    WithoutIndicator(1, "enmQualityWithoutIndicator"),
    WithIndicator(2, "enmQualityWithIndicator");

    private final int _id;
    private final String _textId;

    QualityUsage(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }

    public String type() {
        try {
            return Utils.getMessage(_textId);
        } catch (Exception ex) {
            return "unknown text id: " + _textId;
        }

    }
}
