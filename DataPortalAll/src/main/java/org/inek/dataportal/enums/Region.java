/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author schlappajo
 */
public enum Region {

    NoAttribute(-1, "enmNoAttribute"),
    Germany(0, "enmRegionGer"),
    State(1, "enmRegionState"),
    Misc(2, "enmRegionMisc");

    private final int _id;
    private final String _textId;

    Region(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }

    public String region() {
        return Utils.getMessage(_textId);
    }

}
