/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.idservice;

import java.util.Date;

/**
 *
 * @author muellermi
 */
public class IdInfo {

    private final String _id;
    private final long _ts = new Date().getTime();

    public IdInfo(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }

    public long getTs() {
        return _ts;
    }
}
