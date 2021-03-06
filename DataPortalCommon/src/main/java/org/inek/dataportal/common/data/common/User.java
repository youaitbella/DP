package org.inek.dataportal.common.data.common;

import org.inek.dataportal.common.data.account.iface.Person;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.inek.dataportal.common.data.account.entities.Account;

/**
 * An excerpt of Account for fast info within IkAdmin
 *
 * @author muellermi
 */
@Entity
@Table(name = "Account")
@NamedQueries({
    @NamedQuery(name = "User.findIkAdmins",
            query = "select u "
            + "from IkAdmin adm "
            + "join IkAdminFeature f on adm = f._ikAdmin "
            + "join User u on adm._accountId = u._id "
            + "where adm._ik = :ik and f._feature = :feature"),
    @NamedQuery(name = "User.findUsersWithRights",
            query = "select u "
            + "from AccessRight a "
            + "join User u on a._accountId = u._id "
            + "where a._ik = :ik and a._feature = :feature and a._right in :rights")
})
public class User implements Serializable, Person {

    private static final long serialVersionUID = 1L;

    public User() {
    }

    public User(int id, int gender, String title, String firstName, String lastName, String email, String company) {
        _id = id;
        _gender = gender;
        _title = title;
        _firstName = firstName;
        _lastName = lastName;
        _email = email;
        _company = company;
    }

    public User(Account account) {
        this(account.getId(),
                account.getGender(),
                account.getTitle(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.getCompany());
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column(name = "acId")
    private int _id;

    @Override
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Gender">
    @Column(name = "acGender")
    private int _gender = 0;

    @Override
    public int getGender() {
        return _gender;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "acTitle")
    private String _title = "";

    @Override
    public String getTitle() {
        return _title;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "acFirstName")
    private String _firstName = "";

    @Override
    public String getFirstName() {
        return _firstName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "acLastName")
    private String _lastName = "";

    @Override
    public String getLastName() {
        return _lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Email">
    @Column(name = "acMail")
    private String _email;

    @Override
    public String getEmail() {
        return _email;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Company">
    @Column(name = "acCompany")
    private String _company = "";

    @Override
    public String getCompany() {
        return _company;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this._id;
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
        final User other = (User) obj;
        return this._id == other._id;
    }
    // </editor-fold>

}
