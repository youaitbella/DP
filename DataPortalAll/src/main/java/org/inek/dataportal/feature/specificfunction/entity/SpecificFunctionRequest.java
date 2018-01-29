package org.inek.dataportal.feature.specificfunction.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
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
@Table(name = "RequestMaster", schema = "spf")
public class SpecificFunctionRequest implements Serializable, StatusEntity {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rmId")
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
    @Column(name = "rmDataYear")
    @Documentation(key = "lblYearData")
    private int _dataYear;

    public int getDataYear() {
        return _dataYear;
    }

    public void setDataYear(int dataYear) {
        _dataYear = dataYear;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Code">
    @Column(name = "rmCode")
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

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "rmIK")
    @Documentation(key = "lblIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "rmAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "rmLastChanged")
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
    @Column(name = "rmLastChangedBy")
    private int _accountIdLastChange;

    public int getAccountIdLastChange() {
        return _accountIdLastChange;
    }

    public void setAccountIdLastChange(int accountId) {
        this._accountIdLastChange = accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sealed">
    @Column(name = "rmSealed")
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
    @Column(name = "rmStatusId")
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
    @Column(name = "rmGender")
    
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
    @Column(name = "rmTitle")
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
    @Column(name = "rmFirstName")
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
    @Column(name = "rmLastName")
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
    @Column(name = "rmPhone")
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
    @Column(name = "rmMail")
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

    // <editor-fold defaultstate="collapsed" desc="Property WillNegotiate">
    @Column(name = "rmWillNegotiate")
    private boolean _willNegotiate = true;

    public boolean isWillNegotiate() {
        return _willNegotiate;
    }

    public void setWillNegotiate(boolean willNegotiate) {
        _willNegotiate = willNegotiate;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property HasAgreement">
    @Column(name = "rmHasAgreement")
    private boolean _hasAgreement = true;

    public boolean isHasAgreement() {
        return _hasAgreement;
    }

    public void setHasAgreement(boolean hasAgreement) {
        _hasAgreement = hasAgreement;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property RequestProjectedCenter">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rpcRequestMasterId", referencedColumnName = "rmId")
    @Documentation(name = "geplante Vereinbarung", omitOnEmpty = true)
    private List<RequestProjectedCenter> _requestProjectedCenters = new Vector<>();

    public List<RequestProjectedCenter> getRequestProjectedCenters() {
        return _requestProjectedCenters;
    }

    public void setRequestProjectedCenters(List<RequestProjectedCenter> requestProjectedCenters) {
        this._requestProjectedCenters = requestProjectedCenters;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property RequestAgreedCenter">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "racRequestMasterId", referencedColumnName = "rmId")
    @Documentation(name = "vorliegende Vereinbarung", omitOnEmpty = true)
    private List<RequestAgreedCenter> _requestAgreedCenters = new Vector<>();

    public List<RequestAgreedCenter> getRequestAgreedCenters() {
        return _requestAgreedCenters;
    }

    public void setRequestAgreedCenters(List<RequestAgreedCenter> requestAgreedCenters) {
        this._requestAgreedCenters = requestAgreedCenters;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NoteInek">
    @Column(name = "rmNoteInek")
    @Documentation(name = "Bemerkung InEK", rank = 175, omitOnEmpty = true)
    private String _noteInek = "";

    public String getNoteInek() {
        return _noteInek;
    }

    public void setNoteInek(String noteInek) {
        _noteInek = noteInek;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SpecificFunctionRequest)) {
            return false;
        }
        SpecificFunctionRequest other = (SpecificFunctionRequest) object;

        return _id == other._id;
    }

    @Override
    public String toString() {
        return "RequestMaster[id=" + _id + "]";
    }
    // </editor-fold>

}
