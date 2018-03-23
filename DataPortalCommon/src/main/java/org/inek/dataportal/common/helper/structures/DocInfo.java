package org.inek.dataportal.common.helper.structures;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author muellermi
 */
public class DocInfo implements Serializable{

    private final int _id;
    private final String _name;
    private final String _domain;
    private final Date _created;
    private final Date _validUntil;
    private final boolean _isRead;
    private final int _accountId;
    private final int _agentId;
    private final int _senderIk;
    private final String _agentName;
    private final String _tag;
    private final boolean _sendToProcess;
    private boolean _isSelected;

    @SuppressWarnings("ParameterNumber")
    public DocInfo(
            int id, 
            String name, 
            String domain, 
            Date created, 
            Date validUntil, 
            boolean isRead, 
            int accountId, 
            int agentId, 
            int senderIk, 
            String agentName, 
            String tag,
            boolean sendToProcess
    ) {
        _id = id;
        _name = name;
        _domain = domain;
        _created = created;
        _validUntil = validUntil;
        _isRead = isRead;
        _accountId = accountId;
        _agentId = agentId;
        _senderIk = senderIk;
        _agentName = agentName;
        _tag = tag;
        _sendToProcess = sendToProcess;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getDomain() {
        return _domain;
    }

    public String getTag() {
        return _tag;
    }

    public Date getCreated() {
        return _created;
    }

    public String getCreatedString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(_created);
    }

    public String getLongCreatedString() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        char nbsp = 160;
        return df.format(_created).replace(' ', nbsp);
    }

    public Date getValidUntil() {
        return _validUntil;
    }

    public String getValidUntilString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(_validUntil);
    }

    public boolean isRead() {
        return _isRead;
    }

    public int getAccountId() {
        return _accountId;
    }

    public int getAgentId() {
        return _agentId;
    }
    public int getSenderIk() {
        return _senderIk;
    }

    public boolean isSendToProcess() {
        return _sendToProcess;
    }

    public boolean isSelected() {
        return _isSelected;
    }

    public void setSelected(boolean isSelected) {
        _isSelected = isSelected;
    }
}