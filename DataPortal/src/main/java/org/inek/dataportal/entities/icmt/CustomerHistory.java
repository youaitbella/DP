/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.icmt;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "CustomerHistory", catalog = "CallCenterDB", schema = "dbo")
public class CustomerHistory implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CustomerId">
    @Column(name = "chCustomerId")
    private int _customerId;

    public int getCustomerId() {
        return _customerId;
    }

    public void setCustomerId(int customerId) {
        _customerId = customerId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property reviousCustomerId">
    @Column(name = "chPreviousCustomerId")
    private int _previousCustomerId;

    public int getPreviousCustomerId() {
        return _previousCustomerId;
    }

    public void setPreviousCustomerId(int previousCustomerId) {
        _previousCustomerId = previousCustomerId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ChangeDate">
    @Column(name = "chChangeDate")
    @Temporal(TemporalType.TIMESTAMP)
    private DateTime _changeDate;

    public DateTime getChangeDate() {
        return _changeDate;
    }

    public void setChangeDate(DateTime changeDate) {
        _changeDate = changeDate;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Comment">
    @Column(name = "chComment")
    private String _comment;

    public String getComment() {
        return _comment;
    }

    public void setComment(String comment) {
        _comment = comment;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode + equals">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this._id;
        return hash;
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
        final CustomerHistory other = (CustomerHistory) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }
    //</editor-fold>
    
}
