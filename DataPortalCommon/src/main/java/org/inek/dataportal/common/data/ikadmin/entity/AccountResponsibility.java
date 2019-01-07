package org.inek.dataportal.common.data.ikadmin.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;

@Entity
@Table(name = "AccountResponsibility", schema = "ikadm")
@NamedQueries({
    @NamedQuery(name="AccountResponsibility.findByAccountId+Feature+UserIk",
                query="select ar from AccountResponsibility ar "
                        + "where ar._accountId = :accountId and ar._feature = :feature and ar._userIk = :userIk")
})
public class AccountResponsibility implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public AccountResponsibility(){}

    public AccountResponsibility(int accountId, Feature feature, int userIk, int dataIk){
        _accountId = accountId;
        _feature = feature;
        _userIk = userIk;
        _dataIk = dataIk;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "arAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int id) {
        _accountId = id;
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

    // <editor-fold defaultstate="collapsed" desc="Property UserIk">
    @Column(name = "arUserIk")
    private int _userIk;

    public int getUserIk() {
        return _userIk;
    }

    public void setUserIk(int id) {
        _userIk = id;
    }
    // </editor-fold>
  
    // <editor-fold defaultstate="collapsed" desc="Property DataIk">
    @Column(name = "arDataIk")
    private int _dataIk;

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
        final AccountResponsibility other = (AccountResponsibility) obj;
        if (this._accountId != other._accountId) {
            return false;
        }
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
