/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Contact", schema = "calc")
public class CalcContact implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property StatementOfParticipanceId">
    @Column(name = "coStatementOfParticipanceId")
    private int _statementOfParticipanceId;
    public int getStatementOfParticipanceId() {
        return _statementOfParticipanceId;
    }

    public void setStatementOfParticipanceId(int statementOfParticipanceId) {
        _statementOfParticipanceId = statementOfParticipanceId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Salutation">
    @Column(name = "coSalutation")
    private String _salutation = "";
    public String getSalutation() {
        return _salutation;
    }

    public void setSalutation(String salutation) {
        this._salutation = salutation;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "coTitle")
    private String _title = "";
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "coFirstName")
    private String _firstName = "";
    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "coLastName")
    private String _lastName = "";
    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Phone">
    @Column(name = "coPhone")
    private String _phone = "";
    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Mail">
    @Column(name = "coMail")
    private String _mail = "";
    public String getMail() {
        return _mail;
    }

    public void setMail(String mail) {
        _mail = mail;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsDrg">
    @Column(name = "coIsDrg")
    private boolean _drg;
    public boolean isDrg() {
        return _drg;
    }

    public void setDrg(boolean drg) {
        _drg = drg;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsPsy">
    @Column(name = "coIsPsy")
    private boolean _psy;
    public boolean isPsy() {
        return _psy;
    }

    public void setPsy(boolean psy) {
        _psy = psy;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsInv">
    @Column(name = "coIsInv")
    private boolean _inv;
    public boolean isInv() {
        return _inv;
    }

    public void setInv(boolean inv) {
        _inv = inv;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsTpg">
    @Column(name = "coIsTpg")
    private boolean _tpg;
    public boolean isTpg() {
        return _tpg;
    }

    public void setTpg(boolean tpg) {
        _tpg = tpg;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsConsultant">
    @Column(name = "coIsConsultant")
    private boolean _consultant;
    public boolean isConsultant() {
        return _consultant;
    }

    public void setConsultant(boolean consultant) {
        _consultant = consultant;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsMailDistribution">
    @Column(name = "coIsMailDistribution")
    private boolean _mailDistribution;
    public boolean isMailDistribution() {
        return _mailDistribution;
    }

    public void setMailDistribution(boolean mailDistribution) {
        _mailDistribution = mailDistribution;
    }
    // </editor-fold>
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this._id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CalcContact other = (CalcContact) obj;
        return _id == other.getId();
    }
    
    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.CalcContact[ id=" + _id + " ]";
    }
    
    // </editor-fold>
    
}
