/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.account.entities;

import org.inek.dataportal.common.data.account.iface.Person;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.FeatureState;
import org.inek.dataportal.common.data.adm.InekRole;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.AccountResponsibility;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Account")
public class Account implements Serializable, Person {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private int _id = -1;

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

    @Override
    public void setGender(int isFemale) {
        _gender = isFemale;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "acTitle")
    private String _title = "";

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public void setTitle(String title) {
        _title = title;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "acFirstName")
    private String _firstName = "";

    @Override
    public String getFirstName() {
        return _firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        _firstName = firstName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "acLastName")
    private String _lastName = "";

    @Override
    public String getLastName() {
        return _lastName;
    }

    @Override
    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CombinedName">
    public String getCombinedName() {
        // this property is a kind of dummy to prevent from "illegal setter syntax" when using like
        //filterValue="#{sessionController.account.lastName}, #{sessionController.account.firstName}"
        // such usage may be replaced now by
        //filterValue="#{sessionController.account.combinedName}"
        return _lastName + ", " + _firstName;
    }

    public void setCombinedName(String combinedName) {
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Mail">
    @Column(name = "acMail")
    private String _email = "";

    @Override
    public String getEmail() {
        return _email;
    }

    @Override
    public void setEmail(String email) {
        _email = email;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Company">
    @Column(name = "acCompany")
    private String _company = "";

    public String getCompany() {
        return _company;
    }

    public void setCompany(String company) {
        _company = company;
    }
    // </editor-fold>

    @Column(name = "acLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;
    @Column(name = "acIsDeactivated")
    private boolean _isDeactivated = false;
    @Column(name = "acUser")
    private String _user;
    @Column(name = "acInitials")
    private String _initials = "";
    @Column(name = "acPhone")
    private String _phone = "";
    @Column(name = "acRoleId")
    private int _roleId = -1;
    @Column(name = "acCustomerTypeId")
    private int _customerTypeId = -1;
    @Column(name = "acStreet")
    private String _street = "";
    @Column(name = "acPostalCode")
    private String _postalCode = "";
    @Column(name = "acTown")
    private String _town = "";
    @Column(name = "acCustomerPhone")
    private String _customerPhone = "";
    @Column(name = "acCustomerFax")
    private String _customerFax = "";

    @Column(name = "acNubConfirmation")
    private boolean _nubConfirmation = true;

    @Column(name = "acMessageCopy")
    private boolean _messageCopy = true;

    @Column(name = "acNubInformationMail")
    private boolean _nubInformationMail = true;

    //<editor-fold defaultstate="collapsed" desc="Property IkAdminDisclaimer">
    @Column(name = "acIkAdminDisclaimer")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date _ikAdminDisclaimer = getDefalutDate();

    public Date getIkAdminDisclaimer() {
        return _ikAdminDisclaimer;
    }

    public void setIkAdminDisclaimer(Date ikAdminDisclaimer) {
        this._ikAdminDisclaimer = ikAdminDisclaimer;
    }

    public boolean isDisclaimerConfirmed() {
        return _ikAdminDisclaimer.after(getDefalutDate());
    }
    //</editor-fold>

    private Date getDefalutDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date defaultDate = calendar.getTime();
        return defaultDate;
    }

    public boolean isNubInformationMail() {
        return _nubInformationMail;
    }

    public void setNubInformationMail(boolean nubInformationMail) {
        _nubInformationMail = nubInformationMail;
    }

    // <editor-fold defaultstate="collapsed" desc="Property DropBoxHoldTime">
    @Column(name = "acDropBoxHoldTime")
    private int _dropBoxHoldTime = 100;

    @Min(30)
    @Max(1500)
    public int getDropBoxHoldTime() {
        return _dropBoxHoldTime;
    }

    public void setDropBoxHoldTime(int dropBoxHoldTime) {
        _dropBoxHoldTime = dropBoxHoldTime;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Features">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "afAccountId", referencedColumnName = "acId")
    @OrderBy("_sequence")
    private List<AccountFeature> _features;

    public void setFeatures(List<AccountFeature> features) {
        _features = features;
    }

    public List<AccountFeature> getFeatures() {
        if (_features == null) {
            _features = new ArrayList<>();
        }
        return _features;
    }

    public void removeAccountFeature(AccountFeature feature) {
        _features.remove(feature);
    }

    public void addFeature(Feature feature, boolean isApproved) {
        FeatureState state = feature.getNeedsApproval()
                ? (isApproved ? FeatureState.APPROVED : FeatureState.NEW)
                : FeatureState.SIMPLE;
        _features.add(new AccountFeature(_features.size(), state, feature));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AdditionalIks">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aaiAccountId", referencedColumnName = "acId")
    @OrderBy("_ik")
    private List<AccountIk> _additionalIKs = new ArrayList<>();

    public void addIk(int ik) {
        _additionalIKs.add(new AccountIk(_id, ik));
    }

    public void removeIk(int ik) {
        _additionalIKs.removeIf(a -> a.getIK() == ik);
    }

    public List<AccountIk> getAccountIks() {
        return new CopyOnWriteArrayList<>(_additionalIKs);
    }

    private void removeDuplicateIks() {
        List<AccountIk> accountIks = new ArrayList<>();
        for (AccountIk additionalIK : _additionalIKs) {
            if (accountIks.stream().noneMatch(a -> a.getIK() == additionalIK.getIK())) {
                accountIks.add(additionalIK);
            }
        }
        _additionalIKs = accountIks;
    }

    public Set<Integer> getFullIkSet() {
        Set<Integer> iks = new HashSet<>();
        for (AccountIk addIk : _additionalIKs) {
            iks.add(addIk.getIK());
        }
        return iks;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AdminIks">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "iaAccountId", referencedColumnName = "acId")
    @OrderBy("_ik")
    private List<IkAdmin> _adminIks = new ArrayList<>();

    public List<IkAdmin> getAdminIks() {
        return _adminIks;
    }

    public void setAdminIks(List<IkAdmin> adminIks) {
        this._adminIks = adminIks;
    }

    public void removeIkAdmin(int ik) {
        _adminIks.removeIf(ai -> ai.getIk() == ik);
    }

    /**
     * Adds an ik and returns true, if ik could be added, false otherwise (ik existed)
     *
     * @param ik
     * @param mailDomain
     * @param features
     *
     * @return
     */
    public boolean addIkAdmin(int ik, String mailDomain, List<Feature> features) {
        Optional<IkAdmin> admin = _adminIks.stream().filter(ai -> ai.getIk() == ik).findAny();
        if (admin.isPresent()) {
            admin.get().setMailDomain(mailDomain);
            for (Feature fe : features) {
                admin.get().addIkAdminFeature(fe);
            }
            admin.get().removeIkAdminFeaturesIfNotInList(features);
            return false;
        }
        _adminIks.add(new IkAdmin(_id, ik, mailDomain, features));
        return true;
    }
    // </editor-fold>


    public boolean addIkAdmin(int ik, String mailDomain, Feature feature) {
        Optional<IkAdmin> admin = _adminIks.stream().filter(ai -> ai.getIk() == ik).findAny();
        if (admin.isPresent()) {
            admin.get().setMailDomain(mailDomain);
            if (!admin.get().getIkAdminFeatures().stream().anyMatch(c -> c.getFeature() == feature)) {
                admin.get().addIkAdminFeature(feature);
            }
            return false;
        }

        List<Feature> features = new ArrayList<>();
        features.add(feature);

        _adminIks.add(new IkAdmin(_id, ik, mailDomain, features));
        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Responsibilities">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "arAccountId", referencedColumnName = "acId")
    private List<AccountResponsibility> _responsibleForIks;

    public Set<Integer> obtainResponsibleForIks(Feature feature, int userIk) {
        Collection<Integer> userIks = new ArrayList<>();
        userIks.add(userIk);
        return obtainResponsibleForIks(feature, userIks);
    }

    public Set<Integer> obtainResponsibleForIks(Feature feature, Collection<Integer> userIks) {
        return _responsibleForIks
                .stream()
                .filter(r -> r.getFeature() == feature && userIks.contains(r.getUserIk()))
                .map(r -> r.getDataIk())
                .collect(Collectors.toSet());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InekRoles">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "mapAccountInekRole", schema = "adm",
            joinColumns = @JoinColumn(name = "aiAccountId"),
            inverseJoinColumns = @JoinColumn(name = "aiInekRoleId"))
    private List<InekRole> _inekRoles;

    public List<InekRole> getInekRoles() {
        return _inekRoles;
    }

    public void setInekRoles(List<InekRole> inekRoles) {
        _inekRoles = inekRoles;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter">
    public Date getLastModified() {
        return _lastModified;
    }

    public boolean isDeactivated() {
        return _isDeactivated;
    }

    public void setDeactivated(boolean isDeactivated) {
        _isDeactivated = isDeactivated;
    }

    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        _user = user;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public int getRoleId() {
        return _roleId;
    }

    public void setRoleId(int roleId) {
        _roleId = roleId;
    }

    public int getCustomerTypeId() {
        return _customerTypeId;
    }

    public void setCustomerTypeId(int customerTypeId) {
        _customerTypeId = customerTypeId;
    }

    public String getCustomerPhone() {
        return _customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        _customerPhone = customerPhone;
    }

    public String getCustomerFax() {
        return _customerFax;
    }

    public void setCustomerFax(String customerFax) {
        _customerFax = customerFax;
    }

    public String getInitials() {
        return _initials;
    }

    public void setInitials(String initials) {
        _initials = initials;
    }

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        _street = street;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public void setPostalCode(String postalCode) {
        _postalCode = postalCode;
    }

    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        _town = town;
    }

    public boolean isNubConfirmation() {
        return _nubConfirmation;
    }

    public void setNubConfirmation(boolean nubConfirmation) {
        _nubConfirmation = nubConfirmation;
    }

    public boolean isMessageCopy() {
        return _messageCopy;
    }

    public void setMessageCopy(boolean messageCopy) {
        _messageCopy = messageCopy;
    }

    // </editor-fold>
    @PrePersist
    @PreUpdate
    public void tagModifiedDate() {
        _lastModified = Calendar.getInstance().getTime();
        removeDuplicateIks();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this._id);
        return hash;
    }

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
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
        final Account other = (Account) obj;
        return Objects.equals(this._id, other._id);
    }

    @Override
    public String toString() {
        return "org.inek.entities.Account[id=" + _id + "]";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Transient Property Selected">
    @Transient
    private boolean _selected;

    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean selected) {
        _selected = selected;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Transient Property CalcRoles">
    @Transient
    private String _calcRoles;

    public String getCalcRoles() {
        return _calcRoles;
    }

    public void setCalcRoles(String calcRoles) {
        _calcRoles = calcRoles;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccessRight">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "arAccountId", referencedColumnName = "acId")
    private List<AccessRight> _accessRights = new ArrayList<>();

    public List<AccessRight> getAccessRights() {
        return _accessRights;
    }

    public void setAccessRights(List<AccessRight> accessRights) {
        this._accessRights = accessRights;
    }
    // </editor-fold>

}
