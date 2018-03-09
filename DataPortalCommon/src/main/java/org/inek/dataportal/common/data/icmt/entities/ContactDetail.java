/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.icmt.entities;

import java.io.Serializable;
import java.util.logging.Logger;
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
@Table(name = "ccContactDetails", catalog="CallCenterDB", schema="dbo")
public class ContactDetail implements Serializable {
    private static final Logger LOGGER = Logger.getLogger("ContactDetails");
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cdId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property ContactId">
    @Column(name = "cdContactId")
    private int _contactId;

    public int getContactId() {
        return _contactId;
    }

    public void setContactId(int contactId) {
        this._contactId = contactId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Details">
    @Column(name = "cdDetails")
    private String _details;

    public String getDetails() {
        return _details;
    }

    public void setDetails(String details) {
        this._details = details;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property ContactDetailTypeId">
    @Column(name = "cdContactDetailTypeId")
    private String _contactDetailTypeId;

    public String getContactDetailTypeId() {
        return _contactDetailTypeId;
    }

    public void setContactDetailTypeId(String contactDetailTypeId) {
        this._contactDetailTypeId = contactDetailTypeId;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property DetailsSearch">
    @Column(name = "CdDetailsSearch", updatable = false, insertable = false)
    private String _detailsSearch;

    public String getDetailsSearch() {
        return _detailsSearch;
    }

    public void setDetailsSearch(String detailsSearch) {
        this._detailsSearch = detailsSearch;
    }
    // </editor-fold>

    
}
