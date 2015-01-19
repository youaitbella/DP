package org.inek.dataportal.enums;

import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public enum CooperativeRight {

    None("000", true), // no access granted
    ReadOnly("300", true), // partner may read
    ReadSealed("100", true), // partner may read sealed
    ReadCompleted("200", false), // partner may read completed (approval requested)
    ReadWriteCompleted("220", false), // partner may read or write completed (approval requested)
    ReadWrite("330", true), // partner may read, write
    ReadWriteSeal("331", true), // partner may read, write, seal
    ReadCompletedSealSupervisor("202", true), // partner may read completed. To be sealed by partner only.
    ReadWriteCompletedSealSupervisor("222", true), // partner may read, write completed. To be sealed by partner only.
    ReadSealSupervisor("302", false), // partner may read always. To be sealed by partner only.
    ReadAllWriteCompletedSealSupervisor("322", false), // partner may read always, but write ohnly completed. To be sealed by partner only.
    ReadWriteSealSupervisor("332", true);    // partner may read, write incompleted. To be sealed by partner only.

    /**
     * rights are defined as a three letter string 
     * read: 0 = none; 1 = when sealed; 2 = when at least completed; 3 = always
     * write: 0 = none; 1 = when sealed; 2 = when at least completed; 3 = always
     * seal: 0 = none; 1 = yes; 2 = yes as supervisor
     */
    private final String _rights;
    
    /**
     * this property declares rights, which are displayed to the public,
     * e.g. to comboBoxes the user might choose from
     * Other rights migt be used after merging public rights
     */
    private final boolean _isPublic;

    private CooperativeRight(String rights, boolean isPublic) {
        _rights = rights;
        _isPublic = isPublic;
    }

    public boolean canReadAlways() {
        return _rights.matches("3..");
    }

    public boolean canReadCompleted() {
        return _rights.matches("[23]..");
    }

    public boolean canReadSealed() {
        return _rights.matches("[123].*");
    }

    public boolean canWriteAlways() {
        return _rights.matches(".3.");
    }

    public boolean canWriteCompleted() {
        return _rights.matches(".[23].");
    }

    public boolean canSeal() {
        return _rights.matches("..[12]");
    }

    public boolean isSupervisor() {
        return _rights.matches("..[2]");
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

    public boolean isPublic() {
        return _isPublic;
    }

    public String Description() {
        return Utils.getMessage("cor" + name());
    }

    public CooperativeRight mergeRights (CooperativeRight other){
        return mergeRightFromStrings(other.getRightsAsString());
    }
    
    public CooperativeRight mergeRightFromStrings(String rights) {
        String merged = "";
        try {
            for (int i = 0; i < 3; i++) {
                int val = Math.max(Integer.parseInt(_rights.substring(i, i+1)), Integer.parseInt(rights.substring(i, i+1)));
                merged = merged + val;
            }
            return fromRightsAsString(merged);
        } catch (NumberFormatException e) {
            return CooperativeRight.None;
        }
    }
}
