/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital.entities.sop;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;

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
    @Column(name = "coId", updatable = false, nullable = false)
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
    @Documentation(key = "lblSalutation", omitOnValues = "0", translateValue = "1=salutationFemale;2=salutationMale")
    private int _gender = 0;

    public int getGender() {
        return  _gender; 
    }

    public void setGender(int gender) {
        _gender = (gender < 0 || gender > 2) ? 0 : gender;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "coTitle")
    @Documentation(key = "lblTitle", omitOnEmpty = true)
    private String _title = "";

    @Size(max = 50)
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        // When loading from ICMT a string might be null. Thus we check every string value here and replace null by an empty string.
        _title = title == null ? "" : title;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "coFirstName")
    @Documentation(key = "lblFirstName")
    private String _firstName = "";

    @Size(max = 50)
    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName == null ? "" : firstName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "coLastName")
    @Documentation(key = "lblLastName")
    private String _lastName = "";

    @Size(max = 50)
    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName == null ? "" : lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Phone">
    @Column(name = "coPhone")
    @Documentation(key = "lblPhone")
    private String _phone = "";

    @Size(max = 50)
    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone == null ? "" : phone;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Mail">
    @Column(name = "coMail")
    @Documentation(key = "lblMail")
    private String _mail = "";

    @Size(max = 100)
    public String getMail() {
        return _mail;
    }

    public void setMail(String mail) {
        _mail = mail == null ? "" : mail;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsDrg">
    @Column(name = "coIsDrg")
    @Documentation(key = "lblDrg", omitOnValues = "false")
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
    @Documentation(key = "lblPsy", omitOnValues = "false")
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
    @Documentation(key = "lblInv", omitOnValues = "false")
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
    @Documentation(key = "lblTpg", omitOnValues = "false")
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
    @Documentation(key = "lblObd", omitOnValues = "false")
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
    @Documentation(name = "Externer Berater", omitOnValues = "false")
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
        hash = 71 * hash + (this._obd ? 1 : 0);
        hash = 71 * hash + (this._consultant ? 1 : 0);
        return hash;
    }
    
    @Override
    @SuppressWarnings("CyclomaticComplexity")
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
        if (this._obd != other._obd) {
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
        return _id < 0 && getGender() == 0 
                && (_title + _firstName + _lastName + _phone + _mail).length() == 0 
                && !_drg && !_psy && !_inv && ! _tpg && ! _obd; 
    }
    
}
