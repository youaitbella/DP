/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.entities.cooperation;

import java.io.Serializable;
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
    private Integer _id;
    
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
    public Integer getId() {
        return _id;
    }

    public void setId(Integer Id) {
        this._id = Id;
    }

    public int getOwnerId() {
        return _ownerId;
    }

    public void setOwnerId(int _ownerId) {
        this._ownerId = _ownerId;
    }

    public int getPartnerId() {
        return _partnerId;
    }

    public void setPartnerId(int _partnerId) {
        this._partnerId = _partnerId;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int _ik) {
        this._ik = _ik;
    }

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature _feature) {
        this._feature = _feature;
    }

    public CooperativeRight getCooperativeRight() {
        return _cooperativeRight;
    }

    public void setCooperativeRight(CooperativeRight _cooperativeRight) {
        this._cooperativeRight = _cooperativeRight;
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
        if (!(object instanceof CooperationRight)) {
            return false;
        }
        CooperationRight other = (CooperationRight) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.AccountFeature[id=" + _id + "]";
    }
    // </editor-fold>
}
