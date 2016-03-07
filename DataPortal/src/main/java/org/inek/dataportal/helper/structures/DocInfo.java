package org.inek.dataportal.helper.structures;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author muellermi
 */
public class DocInfo {

    private final int _id;
    private final String _name;
    private final String _domain;
    private final Date _created;
    private final Date _validUntil;
    private final boolean _isRead;

    public DocInfo(int id, String name, String domain, Date created, Date validUntil, boolean isRead) {
        _id = id;
        _name = name;
        _domain = domain;
        _created = created;
        _validUntil = validUntil;
        _isRead = isRead;
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

    public Date getCreated() {
        return _created;
    }

    public String getCreatedString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(_created);
    }

    public Date getValidUntil() {
        return _validUntil;
    }

    public String getValidUntilString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return df.format(_validUntil);
    }

    public boolean isRead(){
        return _isRead;
    }
}
