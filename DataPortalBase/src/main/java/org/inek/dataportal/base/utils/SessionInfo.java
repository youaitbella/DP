/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.base.utils;

/**
 *
 * @author muellermi
 */
public class SessionInfo {
    private String _sessionToken;
    private String _ip;
    private String _userAgent;
    private String _os;

    public String getSessionToken() {
        return _sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this._sessionToken = sessionToken;
    }

    public String getIp() {
        return _ip;
    }

    public void setIp(String ip) {
        this._ip = ip;
    }

    public String getUserAgent() {
        return _userAgent;
    }

    public void setUserAgent(String userAgent) {
        this._userAgent = userAgent;
    }

    public String getOs() {
        return _os;
    }

    public void setOs(String os) {
        this._os = os;
    }
}
