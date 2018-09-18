package org.inek.dataportal.common.data.adm;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "ChangeLog", schema = "adm")
public class ChangeLog implements Serializable {

    public ChangeLog(int accountId,
            Feature feature,
            String form,
            int entryId,
            String field,
            String oldValue,
            String newValue) {
        this._accountId = accountId;
        this._feature = feature;
        this._form = form;
        this._entryId = entryId;
        this._field = field;
        this._oldValue = oldValue;
        this._newValue = newValue;
    }

    public ChangeLog() {
    }

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property TimeStamp">
    @Column(name = "clTimeStamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _timeStamp = new Date();

    public Date getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this._timeStamp = timeStamp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "clAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "clFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        this._feature = feature;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Form">
    @Column(name = "clForm")
    private String _form = "";

    public String getForm() {
        return _form;
    }

    public void setForm(String form) {
        this._form = form;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Entity">
    @Column(name = "clEntity")
    private String _entity = "";

    public String getEntity() {
        return _entity;
    }

    public void setEntity(String entity) {
        this._entity = entity;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property EntryId">
    @Column(name = "clEntryId")
    private int _entryId;

    public int getEntryId() {
        return _entryId;
    }

    public void setEntryId(int entryId) {
        this._entryId = entryId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Field">
    @Column(name = "clField")
    private String _field = "";

    public String getField() {
        return _field;
    }

    public void setField(String field) {
        this._field = field;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property OldValue">
    @Column(name = "clOldValue")
    private String _oldValue = "";

    public String getOldValue() {
        return _oldValue;
    }

    public void setOldValue(String oldValue) {
        this._oldValue = oldValue;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property NewValue">
    @Column(name = "clNewValue")
    private String _newValue = "";

    public String getNewValue() {
        return _newValue;
    }

    public void setNewValue(String newValue) {
        this._newValue = newValue;
    }
    //</editor-fold>

}
