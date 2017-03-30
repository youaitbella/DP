/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.icmt;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "ccContact", catalog="CallCenterDB", schema="dbo")
public class Contact implements Serializable {
    private static final Logger _logger = Logger.getLogger("Contact");
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property CustomerId">
    @Column(name = "coCustomerId")
    private int _customerId;

    public int getCustomerId() {
        return _customerId;
    }

    public void setCustomerId(int customerId) {
        _customerId = customerId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property SexId">
    @Column(name = "coSexId")
    private String _sexId;

    public String getSexId() {
        return _sexId;
    }

    public void setSexId(String sexId) {
        _sexId = sexId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "coTitle")
    private String _title;

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "coFirstName")
    private String _firstName;

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "coLastName")
    private String _lastName;

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Main">
    @Column(name = "coIsMain")
    private boolean _main;

    public boolean isMain() {
        return _main;
    }

    public void setMain(boolean main) {
        _main = main;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Active">
    @Column(name = "coIsActive")
    private boolean _active;

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Info">
    @Column(name = "coInfo")
    private String _info;

    public String getInfo() {
        return _info;
    }

    public void setInfo(String info) {
        this._info = info;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Re">
    @Column(name = "coRe")
    private boolean _re;

    public boolean isRe() {
        return _re;
    }

    public void setRe(boolean re) {
        _re = re;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Prio">
    @Column(name = "coPrio")
    private int _prio;

    public int getPrio() {
        return _prio;
    }

    public void setPrio(int prio) {
        _prio = prio;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property ReportViaPortal">
    @Column(name = "coDPReceiver")
    private boolean _reportViaPortal;

    @Deprecated
    public boolean isReportViaPortal() {
        return _reportViaPortal;
    }

    @Deprecated
    public void setReportViaPortal(boolean reportViaPortal) {
        _reportViaPortal = reportViaPortal;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property ContactDetails">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "cdContactId", referencedColumnName = "coId")
    private List<ContactDetail> _contactDetails;

    public List<ContactDetail> getContactDetails() {
        return _contactDetails;
    }

    public void setContactDetails(List<ContactDetail> contactDetails) {
        this._contactDetails = contactDetails;
    }
    // </editor-fold>
    
    @PrePersist
    @PreUpdate
    private void preventUpdate(){
        _logger.warning("Attempt to write Contact");
        throw new IllegalStateException("Attempt to write Contact");
    }
}
