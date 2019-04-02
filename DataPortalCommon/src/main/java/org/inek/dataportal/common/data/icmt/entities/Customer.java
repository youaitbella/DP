/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.data.icmt.entities;

import org.inek.dataportal.common.data.icmt.enums.State;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "ccCustomer", catalog = "CallCenterDB", schema = "dbo")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Contacts">
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "coCustomerId", referencedColumnName = "cuId")
    private List<Contact> _contacts = Collections.emptyList();

    public List<Contact> getContacts() {
        return _contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this._contacts = contacts;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CustomerId">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuId")
    private Integer _customerId;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "cuIK")
    private Integer _ik = -1;

    public int getIK() {
        return _ik;
    }

    public void setIK(int IK) {
        _ik = IK;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "cuName")
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Street">
    @Column(name = "cuStreet")
    private String _street;

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        _street = street;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PostCode">
    @Column(name = "cuPostCode")
    private String _postCode;

    public String getPostCode() {
        return _postCode;
    }

    public void setPostCode(String postCode) {
        this._postCode = postCode;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Town">
    @Column(name = "cuCity")
    private String _town;

    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        _town = town;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CustomerTypeId">
    @Column(name = "cuCustomerTypeId")
    private Integer _customerTypeId;

    public int getCustomerTypeId() {
        return _customerTypeId;
    }

    public void setCustomerTypeId(int customerTypeId) {
        _customerTypeId = customerTypeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property State">
    @Column(name = "cuStateId")
    private Integer _state = State.Unknown.getId();

    public State getState() {
        return State.fromValue(_state);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PsyState">
    @Column(name = "cuPsyStateId")
    private Integer _psyState = State.Unknown.getId();

    public State getPsyState() {
        return State.fromValue(_psyState);
    }
    // </editor-fold>


    @PrePersist
    @PreUpdate
    private void preventUpdate() {
        throw new IllegalStateException("Attempt to write customer");
    }

}
