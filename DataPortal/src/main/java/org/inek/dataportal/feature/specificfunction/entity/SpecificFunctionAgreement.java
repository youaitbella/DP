package org.inek.dataportal.feature.specificfunction.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.inek.dataportal.entities.iface.StatusEntity;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "AgreementMaster", schema = "spf")
public class SpecificFunctionAgreement implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amId")
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "amDataYear")
    @Documentation(key = "lblYearData")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        this._dataYear = dataYear;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Code">
    @Column(name = "amCode")
    @Documentation(key = "lblContractKey")
    private String _code = "";

    @Size(max = 10)
    public String getCode() {
        return _code;
    }

    public void setCode(String code) {
        _code = code == null ? "" : code;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property InsuranceIk">
    @Column(name = "amInsuranceIK")
    @Documentation(key = "lblInsuranceIk")
    private int _insuranceIk;

    public int getInsuranceIk() {
        return _insuranceIk;
    }

    public void setInsuranceIk(int insuranceIk) {
        this._insuranceIk = insuranceIk;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property InsuranceName">
    @Column(name = "amInsuranceName")
    @Documentation(key = "lblInsuranceName")
    private String _insuranceName = "";

    @Size(max = 250)
    public String getInsuranceName() {
        return _insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        _insuranceName = insuranceName;
    }
    // </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Property InsuranceStreet">
    @Column(name = "amInsuranceStreet")
    @Documentation(key = "lblStreet")
    private String _insuranceStreet = "";

    @Size(max = 100)
    public String getInsuranceStreet() {
        return _insuranceStreet;
    }

    public void setInsuranceStreet(String insuranceStreet) {
        _insuranceStreet = insuranceStreet;
    }
    // </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Property InsurancePostCode">
    @Column(name = "amInsurancePostCode")
    @Documentation(key = "lblPostalCode")
    private String _insurancePostCode = "";

    @Size(max = 10)
    public String getInsurancePostCode() {
        return _insurancePostCode;
    }

    public void setInsurancePostCode(String insurancePostCode) {
        _insurancePostCode = insurancePostCode;
    }
    // </editor-fold>
   
    // <editor-fold defaultstate="collapsed" desc="Property InsuranceTown">
    @Column(name = "amInsuranceTown")
    @Documentation(key = "lblTown")
    private String _insuranceTown = "";

    @Size(max = 100)
    public String getInsuranceTown() {
        return _insuranceTown;
    }

    public void setInsuranceTown(String insuranceTown) {
        _insuranceTown = insuranceTown;
    }
    // </editor-fold>
   
    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "amAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "amLastChanged")
    @Documentation(name = "Stand")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged;

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this._lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accountIdLastChange">
    @Column(name = "amLastChangedBy")
    private int _accountIdLastChange;

    public int getAccountIdLastChange() {
        return _accountIdLastChange;
    }

    public void setAccountIdLastChange(int accountId) {
        this._accountIdLastChange = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "amSealed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _sealed = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getSealed() {
        return _sealed;
    }

    public void setSealed(Date sealed) {
        this._sealed = sealed;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "amStatusId")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        this._statusId = statusId;
    }

    @Documentation(key = "lblWorkstate", rank = 10)
    @Override
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    @Override
    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Gender">
    @Column(name = "amGender")
    
    @Documentation(key = "lblSalutation", omitOnValues = "0", translateValue = "1=salutationFemale;2=salutationMale")
    private int _gender = 0;

    public int getGender() {
        return _gender;
    }

    public void setGender(int gender) {
        _gender = (gender < 0 || gender > 2) ? 0 : gender;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Title">
    @Column(name = "amTitle")
    @Documentation(key = "lblTitle", omitOnEmpty = true)
    private String _title = "";

    @Size(max = 50)
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        // When loading from ICMT a string might be null. Thus we check every string value here and replace null by an empty string.
        _title = title;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstName">
    @Column(name = "amFirstName")
    @Documentation(key = "lblFirstName")
    private String _firstName = "";

    @Size(max = 50)
    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LastName">
    @Column(name = "amLastName")
    @Documentation(key = "lblLastName")
    private String _lastName = "";

    @Size(max = 50)
    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Phone">
    @Column(name = "amPhone")
    @Documentation(key = "lblPhone")
    private String _phone = "";

    @Size(max = 50)
    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Mail">
    @Column(name = "amMail")
    @Documentation(key = "lblMail")
    private String _mail = "";

    @Size(max = 100)
    public String getMail() {
        return _mail;
    }

    public void setMail(String mail) {
        _mail = mail;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "amIK")
    @Documentation(key = "lblIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property SpecificInstitution">
    @Column(name = "amSpecificInstitution")
    private boolean _specificInstitution = true;

    public boolean isSpecificInstitution() {
        return _specificInstitution;
    }

    public void setSpecificInstitution(boolean specificInstitution) {
        _specificInstitution = specificInstitution;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property IsReported">
    @Column(name = "amIsReported")
    private boolean _reported;

    public boolean isReported() {
        return _reported;
    }

    public void setReported(boolean reported) {
        _reported = reported;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AgreedRemunerationkeys">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "arkSpecificFunctionAgreementId", referencedColumnName = "amId")
    private List<AgreedRemunerationKeys> _remunerationKeys = new ArrayList<>();

    public List<AgreedRemunerationKeys> getRemunerationKeys() {
        return _remunerationKeys;
    }

    public void setRemunerationKeys(List<AgreedRemunerationKeys> remunerationKeys) {
        this._remunerationKeys = remunerationKeys;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property AgreedCenter">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "acAgreedMasterId", referencedColumnName = "amId")
    @Documentation(name = "vereinbarte Kostenstellen", omitOnEmpty = true)
    private List<AgreedCenter> _agreedCenters = new Vector<>();

    public List<AgreedCenter> getAgreedCenters() {
        return _agreedCenters;
    }

    public void setAgreedCenters(List<AgreedCenter> agreedCenters) {
        this._agreedCenters = agreedCenters;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NoteInek">
    @Column(name = "amNoteInek")
    @Documentation(name = "Bemerkung InEK", rank = 175, omitOnEmpty = true)
    private String _noteInek = "";

    public String getNoteInek() {
        return _noteInek;
    }

    public void setNoteInek(String noteInek) {
        _noteInek = noteInek;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property BudgetYear">
    @Column(name = "amBudgetYear")
    @Documentation(name = "Budgetjahr", rank = 180, omitOnEmpty = true)
    private int _budgetYear;

    public int getBudgetYear() {
        return _budgetYear;
    }

    public void setBudgetYear(int budgetYear) {
        this._budgetYear = budgetYear;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property BudgetDate">
    @Column(name = "amBudgetDate")
    @Documentation(name = "Genehmigung Budget", rank = 185, omitOnEmpty = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date _budgetDate = new Date(0);

    public Date getBudgetDate() {
        if(_budgetDate.getTime() == 0)
            return null;
        return _budgetDate;
    }

    public void setBudgetDate(Date budgetDate) {
        this._budgetDate = budgetDate;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SpecificFunctionAgreement)) {
            return false;
        }
        SpecificFunctionAgreement other = (SpecificFunctionAgreement) object;

        return _id == other._id;
    }

    @Override
    public String toString() {
        return "AgreedMaster[id=" + _id + "]";
    }
    // </editor-fold>
    
    public void addAgreedCenterRemunerationKey() {
        AgreedRemunerationKeys key = new AgreedRemunerationKeys();
        key.setSpecificFunctionAgreementId(this.getId());
        getRemunerationKeys().add(key);
    }
    
    public void removeAgreedCenterRemunerationKey(AgreedRemunerationKeys key) {
        getRemunerationKeys().remove(key);
    }
}
