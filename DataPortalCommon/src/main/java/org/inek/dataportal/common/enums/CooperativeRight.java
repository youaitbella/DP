package org.inek.dataportal.common.enums;

import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
public enum CooperativeRight {

    None("0000", true), // no access granted
    ReadOnly("3000", true), // partner may read
    ReadWrite("3300", true), // partner may read, write
    ReadWriteSeal("3301", true), // partner may read, write, seal
    ReadWriteTakeSeal("3311", true); // partner may read, write, take ownership, seal
    
    /**
     * rights are defined as a four character string 
     * read: 0 = none; 1 = when sealed; 2 = when at least completed; 3 = always 
     * write: 0 = none; 1 = when sealed; 2 = when at least completed; 3 = always 
     * take: 0 = none; 1 = yes
     * seal: 0 = none; 1 = yes; 2 = yes as supervisor
     */
    private final String _rights;

    /**
     * this property declares rights, which are displayed to the public, e.g. to
     * comboBoxes the user might choose from Other rights migt be used after
     * merging public rights
     */
    private final boolean _isPublic;

    CooperativeRight(String rights, boolean isPublic) {
        _rights = rights;
        _isPublic = isPublic;
    }

    public boolean canRead() {
        return _rights.matches("3...");
    }

    public boolean canWrite() {
        return _rights.matches(".3..");
    }

    public boolean canTake() {
        return _rights.matches("..1.");
    }

    public boolean canSeal() {
        return _rights.matches("...1");
    }

    public CooperativeRight fromRightsAsString(String rights) {
        for (CooperativeRight right : CooperativeRight.values()) {
            if (rights.equalsIgnoreCase(right._rights)) {
                return right;
            }
        }
        return CooperativeRight.None;
    }

    public String getRightsAsString() {
        return _rights;
    }

    public String Description() {
        return Utils.getMessage("cor" + name());
    }

    public CooperativeRight mergeRights(CooperativeRight other) {
        return mergeRightFromStrings(other.getRightsAsString());
    }

    public CooperativeRight mergeRightFromStrings(String rights) {
        String merged = "";
        try {
            for (int i = 0; i < 4; i++) {
                int val = Math.max(Integer.parseInt(_rights.substring(i, i + 1)), Integer.parseInt(rights.substring(i, i + 1)));
                merged = merged + val;
            }
            return fromRightsAsString(merged);
        } catch (NumberFormatException e) {
            return CooperativeRight.None;
        }
    }

}
