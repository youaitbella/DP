package org.inek.dataportal.enums;

import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public enum InsuranceAffiliation {
    
    AllInsurances(0, "enmNoAttribute"),
    NamedInsurances(1, "enmInsuranceAffiliationNamed");

    private final int _id;
    private final String _textId;

    private InsuranceAffiliation(int id, String textId) {
        _id = id;
        _textId = textId;
    }

    public int id() {
        return _id;
    }

    public String type() {
        return Utils.getMessage(_textId);
    }
    
}
