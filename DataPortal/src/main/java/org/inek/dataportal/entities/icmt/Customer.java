/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.icmt;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "ccCustomer", catalog="CallCenterDB", schema="dbo")
public class Customer implements Serializable {
    private static final Logger _logger = Logger.getLogger("Customer");

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuId")
    private Integer _customerId;
    @Column(name = "cuIK")
    private Integer _ik = -1;
    @Column(name = "cuName")
    private String _name;

    // <editor-fold defaultstate="collapsed" desc="Property Contacts">
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "coCustomerId", referencedColumnName = "cuId")
    private List<Contact> _contacts = Collections.EMPTY_LIST;

    public List<Contact> getContacts() {
        return _contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this._contacts = contacts;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Integer getCustomerId() {
        return _customerId;
    }

    public void setCustomerId(Integer customerId) {
        this._customerId = customerId;
    }

    public Integer getIK() {
        return _ik;
    }

    public void setIK(Integer IK) {
        this._ik = IK;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    private void preventUpdate(){
        _logger.warning("Attempt to write customer");
        throw new IllegalStateException("Attempt to write customer");
    }
}
