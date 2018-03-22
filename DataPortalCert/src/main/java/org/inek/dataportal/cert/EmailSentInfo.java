/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert;

import java.io.Serializable;

/**
 *
 * @author muellermi
 */
public class EmailSentInfo implements Serializable{

    private final String _receipient;
    private final String _bcc;
    private final String _result;

    public EmailSentInfo(String receipient, String bcc, String result) {
        _receipient = receipient;
        _bcc = bcc;
        _result = result;
    }

    public String getReceipient() {
        return _receipient;
    }

    public String getBcc() {
        return _bcc;
    }

    public String getResult() {
        return _result;
    }

}
