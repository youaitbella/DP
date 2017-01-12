/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.utils.Documentation;

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
    private int _statementOfParticipanceId = -1;
    public int getStatementOfParticipanceId() {
        return _statementOfParticipanceId;
    }

    public void setStatementOfParticipanceId(int statementOfParticipanceId) {
        _statementOfParticipanceId = statementOfParticipanceId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Gender">
    @Column(name = "coGender")
    private Integer _gender = -1;

    public Integer getGender() {
        return (_gender == null || _gender < 0 || _gender > 2) ? null : _gender; 
    }

    public void setGender(Integer gender) {
        _gender = (gender == null || gender < 0 || gender > 2) ? -1 : gender;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "coTitle")
    private String _title = "";

    @Size(max = 50)
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

    @Size(max = 50)
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

    @Size(max = 50)
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

    @Size(max = 50)
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

    @Size(max = 100)
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

    // <editor-fold defaultstate="collapsed" desc="Property Obd">
    @Column(name = "coIsObd")
    @Documentation(name = "OBD")
    private boolean _obd;

    public boolean isObd() {
        return _obd;
    }

    public void setObd(boolean obd) {
        _obd = obd;
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

    // <editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this._id;
        if (this._id != -1) return hash;
        
        hash = 71 * hash + this._statementOfParticipanceId;
        hash = 71 * hash + Objects.hashCode(this._gender);
        hash = 71 * hash + Objects.hashCode(this._title);
        hash = 71 * hash + Objects.hashCode(this._firstName);
        hash = 71 * hash + Objects.hashCode(this._lastName);
        hash = 71 * hash + Objects.hashCode(this._phone);
        hash = 71 * hash + Objects.hashCode(this._mail);
        hash = 71 * hash + (this._drg ? 1 : 0);
        hash = 71 * hash + (this._psy ? 1 : 0);
        hash = 71 * hash + (this._inv ? 1 : 0);
        hash = 71 * hash + (this._tpg ? 1 : 0);
        hash = 71 * hash + (this._consultant ? 1 : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CalcContact)) {
            return false;
        }
        final CalcContact other = (CalcContact) obj;
        
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        
        if (this._id != other._id) {
            return false;
        }        
        if (this._statementOfParticipanceId != other._statementOfParticipanceId) {
            return false;
        }
        if (this._drg != other._drg) {
            return false;
        }
        if (this._psy != other._psy) {
            return false;
        }
        if (this._inv != other._inv) {
            return false;
        }
        if (this._tpg != other._tpg) {
            return false;
        }
        if (this._consultant != other._consultant) {
            return false;
        }
        if (!Objects.equals(this._title, other._title)) {
            return false;
        }
        if (!Objects.equals(this._firstName, other._firstName)) {
            return false;
        }
        if (!Objects.equals(this._lastName, other._lastName)) {
            return false;
        }
        if (!Objects.equals(this._phone, other._phone)) {
            return false;
        }
        if (!Objects.equals(this._mail, other._mail)) {
            return false;
        }
        if (!Objects.equals(this._gender, other._gender)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.CalcContact[ id=" + _id + " ]";
    }
    // </editor-fold>

    public boolean isEmpty() {
        return _id < 0 && getGender() == null 
                && (_title + _firstName + _lastName + _phone + _mail).length() == 0 
                && !_drg && !_psy && !_inv && ! _tpg && ! _obd; 
    }
    
}
