/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.drg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.inek.dataportal.enums.DrgProposalCategory;
import org.inek.dataportal.enums.DrgProposalChangeMethod;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author schwarzst
 */
@Entity
@Table(name = "DrgProposal")
public class DrgProposal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prId")
    private Integer _id;

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "prVersion")
    @Version
    private int _version;
    // </editor-fold>

    @Column(name = "prAccountId")
    private Integer _accountId;

    @Column(name = "prStatus")
    private int _status;

    @Documentation(key = "lblAppellation")
    @Column(name = "prName")
    private String _name = "";

    @Documentation(key = "lblCategory")
    @Column(name = "prCategory")
    @Enumerated(EnumType.STRING)
    private DrgProposalCategory _category = DrgProposalCategory.UNKNOWN;

    @Documentation(name = "Aktion Diagnose")
    @Column(name = "prChangeMethodDiag")
    @Enumerated(EnumType.STRING)
    private DrgProposalChangeMethod _changeMethodDiag = DrgProposalChangeMethod.UNKNOWN;

    @Documentation(name = "Aktion Procedure")
    @Column(name = "prChangeMethodProc")
    @Enumerated(EnumType.STRING)
    private DrgProposalChangeMethod _changeMethodProc = DrgProposalChangeMethod.UNKNOWN;

    @Column(name = "prCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = Calendar.getInstance().getTime();

    @Column(name = "prDateSealed")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateSealed = null;

    @Column(name = "prLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;

    @Column(name = "prCreatedBy")
    private int _createdBy;

    @Column(name = "prLastChangedBy")
    private int _lastChangedBy;

    @Column(name = "prSealedBy")
    private int _sealedBy;

    @Documentation(key = "lblProposalRequestor")
    @Column(name = "prInstitute")
    private String _institute = "";

    @Column(name = "prGender")
    private int _gender;

    @Documentation(key = "lblTitle")
    @Column(name = "prTitle")
    private String _title = "";

    @Documentation(key = "lblFirstName")
    @Column(name = "prFirstName")
    private String _firstName = "";

    @Documentation(key = "lblLastName")
    @Column(name = "prLastName")
    private String _lastName = "";

    @Documentation(key = "lblDivision")
    @Column(name = "prDivision")
    private String _division = "";

    @Documentation(key = "lblContactRole")
    @Column(name = "prRoleId")
    private Integer _roleId = -1;

    @Documentation(key = "lblStreet")
    @Column(name = "prStreet")
    private String _street = "";

    @Documentation(key = "lblPostalCode")
    @Column(name = "prPostalCode")
    private String _postalCode = "";

    @Documentation(key = "lblTown")
    @Column(name = "prTown")
    private String _town = "";

    @Documentation(key = "lblPhone")
    @Column(name = "prPhone")
    private String _phone = "";

    @Documentation(key = "lblFax")
    @Column(name = "prFax")
    private String _fax = "";

    @Documentation(key = "lblEMail")
    @Column(name = "prEmail")
    private String _email = "";

    @Documentation(key = "lblProblem")
    @Column(name = "prProblem")
    private String _problem = "";

    @Documentation(key = "lblSolution")
    @Column(name = "prSolution")
    private String _solution = "";

    @Documentation(name = "DRG")
    @Column(name = "prDrg")
    private String _drg = "";

    @Documentation(name = "Diagnos(en), Text")
    @Column(name = "prDiags")
    private String _diags = "";

    @Documentation(name = "Diagnos(en), Kode(s)")
    @Column(name = "prDiagCodes")
    private String _diagCodes = "";

    @Documentation(name = "Prozeduren")
    @Column(name = "prProcs")
    private String _procs = "";

    @Documentation(name = "Prozeduren Beschreibung")
    @Column(name = "prProcsText")
    private String _procsText = "";

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "mapPeppProposalProcedure",
//            joinColumns = @JoinColumn(name = "pppPeppProposalId"),
//            inverseJoinColumns = @JoinColumn(name = "pppOpsId"))
//    private List<ProcedureInfo> _procedures = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "drgdDrgProposalId", referencedColumnName = "prId")
    private List<DrgProposalDocument> _documents = new ArrayList<>();

    @Documentation(name = "Dokumente, Post")
    @Column(name = "prDocumentationOffline")
    private String _documentsOffline = "";

    @Column(name = "prAnonymousData")
    private Boolean _anonymousData = false;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "drgcDrgProposalId", referencedColumnName = "prId")
    private List<DrgProposalComment> _comments = new ArrayList<>();

    // <editor-fold defaultstate="collapsed" desc="Property Note">
    @Documentation(name = "Bemerkungen")
    @Column(name = "prNote")
    private String _note = "";
    public String getNote() {
        return _note;
    }

    public void setNote(String note) {
        this._note = note;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    @Documentation(name = "Verfahrensnummer", rank = 1)
    public String getExternalId() {
        if (_id == null || _id < 0 || _status < 1) {
            return "";
        }
        return "V" + _id;
    }

    /**
     * Dummy setter, cause JSF needs on
     *
     *
     * @param dummy
     */
    public void setExternalId(String dummy) {
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer id) {
        _accountId = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public DrgProposalCategory getCategory() {
        return _category;
    }

    public DrgProposalChangeMethod getChangeMethodDiag() {
        return _changeMethodDiag;
    }

    public DrgProposalChangeMethod getChangeMethodProc() {
        return _changeMethodProc;
    }

    public void setCategory(DrgProposalCategory category) {
        _category = category;
    }

    public void setChangeMethodDiag(DrgProposalChangeMethod changeMethodDiag) {
        _changeMethodDiag = changeMethodDiag;
    }

    public void setChangeMethodProc(DrgProposalChangeMethod changeMethodProc) {
        _changeMethodProc = changeMethodProc;
    }

    public Boolean isAnonymousData() {
        return _anonymousData;
    }

    public void setAnonymousData(Boolean anonymousData) {
        _anonymousData = anonymousData;
    }

    @Documentation(name = "Bearbeitungsstatus", rank = 10)
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_status);
    }

    public void setStatus(int value) {
        _status = value;
    }

    public void setStatus(WorkflowStatus status) {
        _status = status.getValue();
    }

    public String getInstitute() {
        return _institute;
    }

    public void setInstitute(String institute) {
        _institute = institute;
    }

    public int getGender() {
        return _gender;
    }

    public void setGender(int gender) {
        _gender = gender;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        _division = division;
    }

    public Integer getRoleId() {
        return _roleId;
    }

    public void setRoleId(Integer roleId) {
        _roleId = roleId;
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

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getFax() {
        return _fax;
    }

    public void setFax(String fax) {
        _fax = fax;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getProblem() {
        return _problem.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");  // to replace false characters stored by former char conversion
    }

    public void setProblem(String problem) {
        _problem = problem;
    }

    public String getSolution() {
        return _solution.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setSolution(String solution) {
        _solution = solution;
    }

    public String getDrg() {
        return _drg;
    }

    public void setDrg(String drg) {
        _drg = drg;
    }

    public String getDiags() {
        return _diags.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setDiags(String diags) {
        _diags = diags;
    }

    public String getDiagCodes() {
        return _diagCodes;
    }

    public void setDiagCodes(String diagCodes) {
        _diagCodes = diagCodes;
    }

//    public String getOpsText() {
//        return _opsText;
//    }
//
//    public void setOpsText(String opsText) {
//        _opsText = opsText;
//    }
    public String getProcs() {
        return _procs.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setProcs(String procs) {
        _procs = procs;
    }

    public String getProcsText() {
        return _procsText;
    }

    public void setProcsText(String procsText) {
        _procsText = procsText;
    }

    public List<DrgProposalDocument> getDocuments() {
        return _documents;
    }

    public void setDocuments(List<DrgProposalDocument> documents) {
        _documents = documents;
    }

    public String getDocumentsOffline() {
        return _documentsOffline.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setDocumentsOffline(String documentsOffline) {
        _documentsOffline = documentsOffline;
    }

    public List<DrgProposalComment> getComments() {
        return _comments;
    }

    public void setComments(List<DrgProposalComment> comments) {
        _comments = comments;
    }

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date date) {
        _creationDate = date;
    }

    public Date getDateSealed() {
        return _dateSealed;
    }

    public void setDateSealed(Date dateSealed) {
        _dateSealed = dateSealed;
    }

    public Date getLastModified() {
        return _lastModified;
    }

    public void setLastModified(Date lastModified) {
        _lastModified = lastModified;
    }

    public int getCreatedBy() {
        return _createdBy;
    }

    public void setCreatedBy(int createdBy) {
        _createdBy = createdBy;
    }

    public int getLastChangedBy() {
        return _lastChangedBy;
    }

    public void setLastChangedBy(int lastChangedBy) {
        _lastChangedBy = lastChangedBy;
    }

    public int getSealedBy() {
        return _sealedBy;
    }

    public void setSealedBy(int sealedBy) {
        _sealedBy = sealedBy;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DrgProposal)) {
            return false;
        }
        DrgProposal other = (DrgProposal) object;
        if ((_id == null && other.getId() != null) || (_id != null && !_id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.DrgProposal[id=" + _id + "]";
    }

    // </editor-fold>
}
