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

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
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

    public int getFunctionId() {
        return _functionId;
    }

    public void setFunctionId(int functionId) {
        this._functionId = functionId;
    }
    //</editor-fold>

    //<editor-fold desc="Property DataId">
    @Column(name="coDataId")
    private int _dataId;

    public int getDataId() {
        return _dataId;
    }

    public void setDataId(int dataId) {
        this._dataId = dataId;
    }
    //</editor-fold>

    //<editor-fold desc="Property CreatedAt">
    @Column(name="coCreatedAt")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _date;

    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        this._date = date;
    }
    //</editor-fold>

    //<editor-fold desc="Property Message">
    @Column(name="coMessage")
    private String _message;

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        this._message = message;
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
                _dataId == that._dataId &&
                Objects.equals(_date, that._date) &&
                Objects.equals(_message, that._message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _accountId, _inek, _functionId, _dataId, _date, _message);
    }
}
