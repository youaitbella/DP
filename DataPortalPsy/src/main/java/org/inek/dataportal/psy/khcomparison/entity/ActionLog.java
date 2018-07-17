package org.inek.dataportal.psy.khcomparison.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "ActionLog", schema = "adm")
public class ActionLog implements Serializable {

    public ActionLog(int accountId,
            String area,
            String form,
            String field,
            String oldValue,
            String newValue) {
        this._accountId = accountId;
        this._area = area;
        this._form = form;
        this._field = field;
        this._oldValue = oldValue;
        this._newValue = newValue;
    }

    public ActionLog() {
    }

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "alAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Area">
    @Column(name = "alArea")
    private String _area = "";

    public String getArea() {
        return _area;
    }

    public void setArea(String area) {
        this._area = area;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Area">
    @Column(name = "alForm")
    private String _form = "";

    public String getForm() {
        return _form;
    }

    public void setForm(String form) {
        this._form = form;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Field">
    @Column(name = "alField")
    private String _field = "";

    public String getField() {
        return _field;
    }

    public void setField(String field) {
        this._field = field;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property OldValue">
    @Column(name = "alOldValue")
    private String _oldValue = "";

    public String getOldValue() {
        return _oldValue;
    }

    public void setOldValue(String oldValue) {
        this._oldValue = oldValue;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property NewValue">
    @Column(name = "alNewValue")
    private String _newValue = "";

    public String getNewValue() {
        return _newValue;
    }

    public void setNewValue(String newValue) {
        this._newValue = newValue;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ChangedDate">
    @Column(name = "alChangedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _changeDate = new Date();

    public Date getChangeDate() {
        return _changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this._changeDate = changeDate;
    }
    //</editor-fold>
}
