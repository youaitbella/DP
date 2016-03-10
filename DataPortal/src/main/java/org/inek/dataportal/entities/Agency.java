package org.inek.dataportal.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.inek.dataportal.entities.account.Account;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "listAgency")
public class Agency implements Serializable {
    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agID")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "agName")
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="List Accounts">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "mapAccountAgency",
            joinColumns = @JoinColumn(name = "maaAgencyId"),
            inverseJoinColumns = @JoinColumn(name = "maaAccountId"))
    private List<Account> _accounts;    

    public List<Account> getAccounts() {
        return _accounts;
    }

    public void setAccounts(List<Account> accounts) {
        _accounts = accounts;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Agency)) {
            return false;
        }
        Agency other = (Agency) object;
        
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "Agency[id=" + _id + "; name=" + _name + "]";
    }
    // </editor-fold>
    
}
