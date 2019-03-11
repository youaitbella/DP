package org.inek.dataportal.common.helper.structures;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int _accountDocumentId;
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

    @SuppressWarnings("ParameterNumber")
    public DocInfo(
            int accountDocumentId,
            String name,
            String domain,
            Date created,
            Date validUntil,
            boolean isRead,
            int accountId,
            int agentId,
            int senderIk,
            String agentName,
            String tag
    ) {
        _accountDocumentId = accountDocumentId;
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
    }

    public int getAccountDocumentId() {
        return _accountDocumentId;
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

}
