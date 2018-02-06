/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.cooperation;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.helper.converter.FeatureConverter;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Message", schema = "usr")
public class PortalMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "mCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _created = Calendar.getInstance().getTime();

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        _created = created;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Status">
    @Column(name = "mStatus")
    private int _status = 0;

    public int getStatus() {
        return _status;
    }

    public void setStatus(int status) {
        _status = status;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FromAccountId">
    @Column(name = "mFromAccountId")
    private int _fromAccountId = -1;

    public int getFromAccountId() {
        return _fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        _fromAccountId = fromAccountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ToAccountId">
    @Column(name = "mToAccountId")
    private int _toAccountId = -1;

    public int getToAccountId() {
        return _toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        _toAccountId = toAccountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "mFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property KeyId">
    @Column(name = "mKeyId")
    private int _keyId = -1;

    public int getKeyId() {
        return _keyId;
    }

    public void setKeyId(int keyId) {
        _keyId = keyId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Subject">
    @Column(name = "mSubject")
    private String _subject = "";

    public String getSubject() {
        return _subject;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PortalMessage">
    @Column(name = "mMessage")
    private String _message = "";

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Visible">
    @Transient
    private boolean _visible = false;

    public boolean isVisible() {
        return _visible;
    }

    public void setVisible(boolean visible) {
        _visible = visible;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hash & equals">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PortalMessage other = (PortalMessage) obj;
        return _id == other._id;
    }

    // </editor-fold>
}
