/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.modelintention;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author schlappajo
 */
@Entity
@Table(name = "Quality", schema = "mvh")
public class ModelIntentionQuality implements Serializable {/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "qyId")
    private Integer _id;
    
    @Column (name = "qyModelIntentionId")
    private Integer _modelIntentionId;
    
    @Column(name = "qyType")
    private Integer _type;
    
    @Column(name = "qyIndicator")
    private String _indicator;
        
    @Column(name = "qyDescription")
    private String _description;
    
    
    
    // <editor-fold defaultstate="collapsed" desc=" Getter / Setter">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getType() {
        return _type;
    }

    public void setType(Integer type) {
        type = type;
    }

    public String getIndicator() {
        return _indicator;
    }

    public void setIndicator(String indicator) {
        _indicator = indicator;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public Integer getModelIntentionId() {
        return _modelIntentionId;
    }

    public void setModelIntentionId(Integer modelIntentionId) {
        _modelIntentionId = modelIntentionId;
    }
    
    
    
    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModelIntentionQuality)) {
            return false;
        }
        ModelIntentionQuality other = (ModelIntentionQuality) object;
        if ((_id == null && other.getId()!= null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Quality[id=" + _id + "]";
    }

    // </editor-fold>
    
    
}
