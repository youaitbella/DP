/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities;

import java.io.Serializable;
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
    @Column(name = "cuId", updatable=false)
    private Integer _customerId;
    @Column(name = "cuIK", updatable=false)
    private Integer _ik = -1;
    @Column(name = "cuName", updatable=false)
    private String _name;

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
        for (int i = 0; i < 10; i++) {
            
        }
    }
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    private void preventUpdate(){
        _logger.warning("Attempt to write customer");
        throw new IllegalStateException("Attempt to write customer");
    }
}
