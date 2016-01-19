/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.cooperation;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "CooperationRight", schema = "usr")
public class CooperationRight implements Serializable {
/*
    	[corId] [int] IDENTITY(1,1) NOT NULL,
	[corOwnerId] [int] NOT NULL,
	[corPartnerId] [int] NOT NULL,
	[corFeature] [varchar](50) NOT NULL,
	[corIk] [int] NOT NULL,
	[corRight] [varchar](50) NOT NULL,
*/
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corId")
    private int _id = -1;
    
    @Column(name = "corOwnerId")
    private int _ownerId;
    
    @Column(name = "corPartnerId")
    private int _partnerId;
    
    @Column(name = "corIk")
    private int _ik;
    
    @Column(name = "corFeature")
    @Enumerated(EnumType.STRING)
    private Feature _feature;

    @Column(name = "corCooperativeRight")
    @Enumerated(EnumType.STRING)
    private CooperativeRight _cooperativeRight = CooperativeRight.None;

    public CooperationRight(){}
    public CooperationRight(int ownerId, int partnerId, int ik, Feature feature){
        _ownerId=ownerId;
        _partnerId=partnerId;
        _ik=ik;
        _feature=feature;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public int getId() {
        return _id;
    }

    public void setId(int Id) {
        _id = Id;
    }

    public int getOwnerId() {
        return _ownerId;
    }

    public void setOwnerId(int ownerId) {
        _ownerId = ownerId;
    }

    public int getPartnerId() {
        return _partnerId;
    }

    public void setPartnerId(int partnerId) {
        _partnerId = partnerId;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }

    public CooperativeRight getCooperativeRight() {
        return _cooperativeRight;
    }

    public void setCooperativeRight(CooperativeRight cooperativeRight) {
        _cooperativeRight = cooperativeRight;
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        if (_id >= 0){
            return _id;
        }
        int hash = 3;
        hash = 53 * hash + _ownerId;
        hash = 53 * hash + _partnerId;
        hash = 53 * hash + _ik;
        hash = 53 * hash + Objects.hashCode(_feature);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CooperationRight)) {
            return false;
        }
        CooperationRight other = (CooperationRight) object;
        if (_id >= 0){
            return _id == other._id;
        }
        return _ownerId == other._ownerId
                && _partnerId == other._partnerId
                && _ik == other._ik
                && _feature == other._feature;
    }

    @Override
    public String toString() {
        return "AccountFeature[id=" + _id + ", Feature=" + _feature.getDescription() + "]";
    }
    // </editor-fold>
}
