package org.inek.dataportal.common.data.adm;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
            String entity,
            String field,
            String oldValue,
            String newValue) {
        _accountId = accountId;
        _feature = feature;
        _form = form;
        _entryId = entryId;
        _entity = entity;
        _field = field;
        _oldValue = oldValue;
        _newValue = newValue;
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "clAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "clFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Form">
    @Column(name = "clForm")
    private String _form = "";

    public String getForm() {
        return _form;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Entity">
    @Column(name = "clEntity")
    private String _entity = "";

    public String getEntity() {
        return _entity;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property EntryId">
    @Column(name = "clEntryId")
    private int _entryId;

    public int getEntryId() {
        return _entryId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Field">
    @Column(name = "clField")
    private String _field = "";

    public String getField() {
        return _field;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property OldValue">
    @Column(name = "clOldValue")
    private String _oldValue = "";

    public String getOldValue() {
        return _oldValue;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property NewValue">
    @Column(name = "clNewValue")
    private String _newValue = "";

    public String getNewValue() {
        return _newValue;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals">
    @Override
    public int hashCode() {
        return 89;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChangeLog other = (ChangeLog) obj;
        if (_id != other._id) {
            return false;
        }
        if (_accountId != other._accountId) {
            return false;
        }
        if (_entryId != other._entryId) {
            return false;
        }
        if (!Objects.equals(_form, other._form)) {
            return false;
        }
        if (!Objects.equals(_entity, other._entity)) {
            return false;
        }
        if (!Objects.equals(_field, other._field)) {
            return false;
        }
        if (!Objects.equals(_timeStamp, other._timeStamp)) {
            return false;
        }
        if (_feature != other._feature) {
            return false;
        }
        return true;
    }
    //</editor-fold>
    
}
