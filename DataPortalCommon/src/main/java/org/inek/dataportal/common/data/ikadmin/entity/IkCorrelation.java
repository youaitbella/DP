/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.ikadmin.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "IkCorrelation", schema = "ikadm")
public class IkCorrelation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public IkCorrelation(){}

    public IkCorrelation(Feature feature, int userIk, int dataIk){
        _feature = feature;
        _userIk = userIk;
        _dataIk = dataIk;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "icFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property UserIk">
    @Column(name = "icUserIk")
    private int _userIk = -1;

    public int getUserIk() {
        return _userIk;
    }

    public void setUserIk(int id) {
        _userIk = id;
    }
    // </editor-fold>
  
    // <editor-fold defaultstate="collapsed" desc="Property DateIk">
    @Column(name = "icDataIk")
    private int _dataIk = -1;

    public int getDataIk() {
        return _dataIk;
    }

    public void setDataIk(int id) {
        _dataIk = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals ">
    @Override
    public int hashCode() {
        return 97;
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
        final IkCorrelation other = (IkCorrelation) obj;
        if (this._userIk != other._userIk) {
            return false;
        }
        if (this._dataIk != other._dataIk) {
            return false;
        }
        if (!Objects.equals(this._id, other._id)) {
            return false;
        }
        if (this._feature != other._feature) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    
}
