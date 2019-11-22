package org.inek.dataportal.common.data.common;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="conversation")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "coId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    //<editor-fold desc="Property AccountId">
    @Column(name = "coAccountId")
    private int _accountId;

    public int get_accountId() {
        return _accountId;
    }

    public void set_accountId(int _accountId) {
        this._accountId = _accountId;
    }
    //</editor-fold>

    //<editor-fold desc="Property isInek">
    @Column(name= "coIsInek")
    private boolean _inek;

    public boolean isInek() {
        return _inek;
    }

    public void setInek(boolean inek) {
        this._inek = inek;
    }
    //</editor-fold>

    //<editor-fold desc="Property FunctionId">
    @Column(name="coFunctionId")
    private int _functionId;

    public int get_functionId() {
        return _functionId;
    }

    public void set_functionId(int _functionId) {
        this._functionId = _functionId;
    }
    //</editor-fold>

    //<editor-fold desc="Property DataId">
    @Column(name="coDataId")
    private int _DataId;

    public int get_DataId() {
        return _DataId;
    }

    public void set_DataId(int _DataId) {
        this._DataId = _DataId;
    }
    //</editor-fold>

    //<editor-fold desc="Property CreatedAt">
    @Column(name="coCreatedAt")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _date;

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }
    //</editor-fold>

    //<editor-fold desc="Property Message">
    @Column(name="coMessage")
    private String _message;

    public String get_message() {
        return _message;
    }

    public void set_message(String _message) {
        this._message = _message;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return _id == that._id &&
                _accountId == that._accountId &&
                _inek == that._inek &&
                _functionId == that._functionId &&
                _DataId == that._DataId &&
                Objects.equals(_date, that._date) &&
                Objects.equals(_message, that._message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _accountId, _inek, _functionId, _DataId, _date, _message);
    }
}
