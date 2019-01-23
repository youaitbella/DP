package org.inek.dataportal.common.enums;

import org.inek.dataportal.common.helper.Utils;

public enum CooperativeRight {

    None("0000"), // no access granted
    ReadOnly("1000"), // partner may read
    ReadWrite("1100"), // partner may read, write
    ReadWriteSeal("1101"), // partner may read, write, seal
    ReadWriteTakeSeal("1111"); // partner may read, write, take ownership, seal
    
    /**
     * rights are defined as a four character string 
     * read, write, take, seal: 0 = no; 1 = yes;
     */
    private final String _rights;


    CooperativeRight(String rights) {
        _rights = rights;
    }

    public boolean canRead() {
        return _rights.matches("1...");
    }

    public boolean canWrite() {
        return _rights.matches(".1..");
    }

    public boolean canTake() {
        return _rights.matches("..1.");
    }

    public boolean canSeal() {
        return _rights.matches("...1");
    }

    public String Description() {
        return Utils.getMessage("cor" + name());
    }

}
