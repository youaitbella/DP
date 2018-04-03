/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.feature.requestsystem.dao;

/**
 *
 * @author muellermi
 */
public class RequestInfo {
    private final int _requestId;
    private final String _name;

    public RequestInfo(int requestId, String name) {
        _requestId = requestId;
        _name = name;
    }

    public int getRequestId() {
        return _requestId;
    }

    public String getName() {
        return _name;
    }
    
}
