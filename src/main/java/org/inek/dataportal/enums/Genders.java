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
public enum Genders {
    
        NoAttribute(0, "enmNoAttribute"),
        Male(1, "enmGenderMale"),
        Female(2, "enmGenderFemale");

        private final int _id;
        private final String _textId;

        private Genders(int id, String textId) {
            _id = id;
            _textId = textId;
        }

        public int id() {
            return _id;
        }

        public String gender() {
            return Utils.getMessage(_textId);
        }
}
