/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author schlappajo
 */
@Entity
@Table(name = "AcademicSupervision", schema = "mvh")
public class AcademicSupervision implements Serializable {/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asId")
    private Integer _id;

    @Column(name = "asModelIntentionId")
    private int _modelIntentionId;

    @Column(name = "asRemitter")
    private String _remitter = "";

    @Column(name = "asContractor")
    private String _contractor = "";

    @Column(name = "asFrom")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _from;

    @Column(name = "asTo")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _to;

    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public String getRemitter() {
        return _remitter;
    }

    public void setRemitter(String remitter) {
        _remitter = remitter;
    }

    public String getContractor() {
        return _contractor;
    }

    public void setContractor(String contractor) {
        _contractor = contractor;
    }

    public Date getAcademicSupFrom() {
        return _from;
    }

    public void setAcademicSupFrom(Date from) {
        _from = from;
    }

    public Date getAcademicSupTo() {
        return _to;
    }

    public void setAcademicSupTo(Date to) {
        _to = to;
    }

    public int getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(int modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AcademicSupervision)) {
            return false;
        }
        AcademicSupervision other = (AcademicSupervision) object;
        return Objects.equals(_id, other._id)
                && (_id != null
                || Objects.equals(_remitter, other._remitter)
                && Objects.equals(_contractor, other._contractor)
                && Objects.equals(_from, other._from)
                && Objects.equals(_to, other._to));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this._id);
        if (_id == null) {
            hash = 79 * hash + Objects.hashCode(this._remitter);
            hash = 79 * hash + Objects.hashCode(this._contractor);
            hash = 79 * hash + Objects.hashCode(this._from);
            hash = 79 * hash + Objects.hashCode(this._to);
        }
        return hash;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AcademicSupervision[id=" + _id + "]";
    }

    // </editor-fold>
}
