package org.inek.dataportal.enums;

import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public enum CooperativeRight {

    None("000"), // no access granted
    ReadOnly("300"), // partner may read
    ReadSealed("100"), // partner may read sealed
    ReadCompleted("200"), // partner may read completed (approval requested)
    ReadWriteCompleted("220"), // partner may read or write completed (approval requested)
    ReadWrite("330"), // partner may read, write
    ReadWriteSeal("331"), // partner may read, write, seal
    ReadCompletedSealSupervisor("202"), // partner may read completed. To be sealed by partner only.
    ReadWriteCompletedSealSupervisor("222"), // partner may read, write completed. To be sealed by partner only.
    ReadSealSupervisor("302"), // partner may read always. To be sealed by partner only.
    ReadWriteSealSupervisor("332");    // partner may read, write incompleted. To be sealed by partner only.

    /**
     * rights are defined as a three letter string 
     * read: 0 = none; 1 = when sealed; 2 = when at least completed; 3 = always
     * write: 0 = none; 1 = when sealed; 2 = when at least completed; 3 = always
     * seal: 0 = none; 1 = yes; 2 = yes as supervisor
     */
    private final String _rights;

    private CooperativeRight(String rights) {
        _rights = rights;
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
            if (merged.startsWith("32")){
                // slightly upgrade from non existent combination
                merged="33" + merged.substring(2, 3);} 
            return fromRightsAsString(merged);
        } catch (NumberFormatException e) {
            return CooperativeRight.None;
        }
    }
}
