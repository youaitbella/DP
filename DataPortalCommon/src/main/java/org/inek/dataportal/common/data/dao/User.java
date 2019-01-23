package org.inek.dataportal.common.data.ikadmin.entity;

import org.inek.dataportal.common.data.account.iface.Person;
import java.io.Serializable;
import org.inek.dataportal.common.data.account.entities.Account;

/**
 * An excerpt of Account for fast info within IkAdmin
 *
 * @author muellermi
 */
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
    private int _gender = 0;

    @Override
    public int getGender() {
        return _gender;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    private String _title = "";

    @Override
    public String getTitle() {
        return _title;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    private String _firstName = "";

    @Override
    public String getFirstName() {
        return _firstName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    private String _lastName = "";

    @Override
    public String getLastName() {
        return _lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Email">
    private String _email;

    @Override
    public String getEmail() {
        return _email;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Company">
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
