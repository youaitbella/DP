/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.ikadmin.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "IkAdmin", schema = "ikadm")
public class AccountIkAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountIkAdmin(){}
    public AccountIkAdmin(int AccountId, int ik, String mailDomain){
        _accountId = AccountId;
        _ik = ik;
        _mailDomain = mailDomain;
    }
    
 
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iaId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>
       
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Id
    @Column(name = "iaAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int id) {
        _accountId = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Ik">
    @Id
    @Column(name = "iaIk")
    private int _ik = -1;

    public int getIk() {
        return _ik;
    }

    public void setIk(int id) {
        _ik = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MailDomain">
    @Column(name = "iaMailDomain")
    private String _mailDomain;
    public String getMailDomain() {
        return _mailDomain;
    }

    public void setMailDomain(String mailDomain) {
        _mailDomain = mailDomain;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode & equals">
    @Override
    public int hashCode() {
        return 47;
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
        final AccountIkAdmin other = (AccountIkAdmin) obj;
        if (this._accountId != other._accountId) {
            return false;
        }
        if (this._ik != other._ik) {
            return false;
        }
        if (!Objects.equals(this._mailDomain, other._mailDomain)) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        return true;
    }
    // </editor-fold>


}
