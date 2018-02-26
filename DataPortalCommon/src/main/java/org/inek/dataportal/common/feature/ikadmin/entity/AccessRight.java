/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.feature.ikadmin.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.data.converter.FeatureConverter;
import org.inek.dataportal.common.data.converter.RightConverter;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AccessRight", schema = "ikadm")
public class AccessRight implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public AccessRight(){}

    public AccessRight(int accountId, int ik, Feature feature, Right right){
        _accountId = accountId;
        _ik = ik;
        _feature = feature;
        _right = right;
    }
    
    public AccessRight(User user, int ik, Feature feature, Right right){
        _user = user;
        _accountId = user.getId();
        _ik = ik;
        _feature = feature;
        _right = right;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "arAccountId")
    private int _accountId = -1;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int id) {
        _accountId = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property User">
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "arAccountId", referencedColumnName = "acId", insertable = false, updatable = false)
    private User _user;
   
    public User getUser() {
        return _user;
    }

    public void setUser(User user) {
        _user = user;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "arIk")
    private int _ik = -1;

    public int getIk() {
        return _ik;
    }

    public void setIk(int id) {
        _ik = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "arFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Right">
    @Column(name = "arRight")
    @Convert(converter = RightConverter.class)
    private Right _right;

    public Right getRight() {
        return _right;
    }

    public void setRight(Right right) {
        _right = right;
    }

    public void setRight(String key) {
        _right = Right.getRightFromKey(key);
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals ">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this._id;
        return hash;
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
        final AccessRight other = (AccessRight) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }
    // </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Delegates to Right">
    public boolean canRead(){
        return _right.canRead();
    }

    public boolean canWrite(){
        return _right.canWrite();
    }

    public boolean canCreate(){
        return _right.canCreate();
    }

    public boolean canSeal(){
        return _right.canSeal();
    }
    // </editor-fold>
    
}
