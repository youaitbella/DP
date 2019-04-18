package org.inek.dataportal.common.enums;

import org.inek.dataportal.common.helper.Utils;

/**
 * CooperativeRight is the old enum to manage cooperative rights.
 * Since April 2019 it simply delegates to Right
 * todo:
 * 1. Update Database (store Right in favor of CooperativeRight)
 * 2. Remove this compatibility enum (CooperativeRight)
 */
public enum CooperativeRight {

    None(Right.Deny), // no access granted
    ReadOnly(Right.Read), // partner may read
    ReadWrite(Right.Write), // partner may read, write
    ReadWriteSeal(Right.All), // partner may read, write, seal
    ReadWriteTakeSeal(Right.Take); // partner may read, write, take ownership, seal

    private final Right _right;


    CooperativeRight(Right right) {
        _right = right;
    }

    public boolean canRead() {
        return _right.canRead();
    }

    public boolean canWrite() {
        return _right.canWrite();
    }

    public boolean canTake() {
        return _right == Right.Take;
    }

    public boolean canSeal() {
        return _right.canSeal();
    }

    public String Description() {
        return Utils.getMessage("cor" + name());
    }

}
