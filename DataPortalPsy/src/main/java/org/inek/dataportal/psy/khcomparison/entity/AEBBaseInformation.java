package org.inek.dataportal.psy.khcomparison.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "AEBBaseInformation", schema = "psy")
public class AEBBaseInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "biId")
    private Integer _id;

    public int getId() {
        return _id == null ? -1 : _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "biVersion")
    @Version
    private int _version;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dataYear">
    @Column(name = "biDataYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "biIK")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatedFrom">
    @Column(name = "biCreatedFrom")
    private int _createdFrom;

    public int getCreatedFrom() {
        return _createdFrom;
    }

    public void setCreatedFrom(int createdFrom) {
        _createdFrom = createdFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CreatedFrom">
    @Column(name = "biLastChangeFrom")
    private int _lastChangeFrom;

    public int getLastChangeFrom() {
        return _lastChangeFrom;
    }

    public void setLastChangeFrom(int lastChangeFrom) {
        _lastChangeFrom = lastChangeFrom;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "biCreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _created = new Date();

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        _created = created;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property LastChanged">
    @Column(name = "biLastChanged")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastChanged = new Date();

    public Date getLastChanged() {
        return _lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        _lastChanged = lastChanged;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Send">
    @Column(name = "biSend")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _send = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getSend() {
        return _send;
    }

    public void setSend(Date send) {
        _send = send;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StatusId">
    @Column(name = "biStatusId")
    private int _statusId;

    public int getStatusId() {
        return _statusId;
    }

    public void setStatusId(int statusId) {
        _statusId = statusId;
    }

    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_statusId);
    }

    public void setStatus(WorkflowStatus status) {
        _statusId = status.getId();
    }
    //</editor-fold>

    @OneToOne(mappedBy = "_baseInformation", cascade = CascadeType.ALL)
    private StructureInformation _structureInformation;

    public StructureInformation getStructureInformation() {
        return _structureInformation;
    }

    public void setStructureInformation(StructureInformation structureInformation) {
        _structureInformation = structureInformation;
    }

    @OneToOne(mappedBy = "_baseInformation", cascade = CascadeType.ALL)
    private AEBPageB1 _aebPageB1;

    public AEBPageB1 getAebPageB1() {
        return _aebPageB1;
    }

    public void setAebPageB1(AEBPageB1 aebPageB1) {
        this._aebPageB1 = aebPageB1;
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peBaseInformationId")
    private List<AEBPageE1_1> _aebPageE1_1 = new ArrayList<>();

    public List<AEBPageE1_1> getAebPageE1_1() {
        return _aebPageE1_1;
    }

    public void setAebPageE1_1(List<AEBPageE1_1> aebPageE1_1) {
        _aebPageE1_1 = aebPageE1_1;
    }

    public void addAebPageE1_1() {
        AEBPageE1_1 page = new AEBPageE1_1();
        page.setBaseInformation(this);
        _aebPageE1_1.add(page);
    }

    public void removeAebPageE1_1(AEBPageE1_1 page) {
        _aebPageE1_1.remove(page);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peBaseInformationId")
    private List<AEBPageE1_2> _aebPageE1_2 = new ArrayList<>();

    public List<AEBPageE1_2> getAebPageE1_2() {
        return _aebPageE1_2;
    }

    public void setAebPageE1_2(List<AEBPageE1_2> aebPageE1_2) {
        _aebPageE1_2 = aebPageE1_2;
    }

    public void addAebPageE1_2() {
        AEBPageE1_2 page = new AEBPageE1_2();
        page.setBaseInformation(this);
        _aebPageE1_2.add(page);
    }

    public void removeAebPageE1_2(AEBPageE1_2 page) {
        _aebPageE1_2.remove(page);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peBaseInformationId")
    private List<AEBPageE2> _aebPageE2 = new ArrayList<>();

    public List<AEBPageE2> getAebPageE2() {
        return _aebPageE2;
    }

    public void setAebPageE2(List<AEBPageE2> aebPageE2) {
        _aebPageE2 = aebPageE2;
    }

    public void addAebPageE2() {
        AEBPageE2 page = new AEBPageE2();
        page.setBaseInformation(this);
        _aebPageE2.add(page);
    }

    public void removeAebPageE2(AEBPageE2 page) {
        _aebPageE2.remove(page);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peBaseInformationId")
    private List<AEBPageE3_1> _aebPageE3_1 = new ArrayList<>();

    public List<AEBPageE3_1> getAebPageE3_1() {
        return _aebPageE3_1;
    }

    public void setAebPageE3_1(List<AEBPageE3_1> aebPageE3_1) {
        _aebPageE3_1 = aebPageE3_1;
    }

    public void addAebPageE3_1() {
        AEBPageE3_1 page = new AEBPageE3_1();
        page.setBaseInformation(this);
        _aebPageE3_1.add(page);
    }

    public void removeAebPageE3_1(AEBPageE3_1 page) {
        _aebPageE3_1.remove(page);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peBaseInformationId")
    private List<AEBPageE3_2> _aebPageE3_2 = new ArrayList<>();

    public List<AEBPageE3_2> getAebPageE3_2() {
        return _aebPageE3_2;
    }

    public void setAebPageE3_2(List<AEBPageE3_2> aebPageE3_2) {
        _aebPageE3_2 = aebPageE3_2;
    }

    public void addAebPageE3_2() {
        AEBPageE3_2 page = new AEBPageE3_2();
        page.setBaseInformation(this);
        _aebPageE3_2.add(page);
    }

    public void removeAebPageE3_2(AEBPageE3_2 page) {
        _aebPageE3_2.remove(page);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "peBaseInformationId")
    private List<AEBPageE3_3> _aebPageE3_3 = new ArrayList<>();

    public List<AEBPageE3_3> getAebPageE3_3() {
        return _aebPageE3_3;
    }

    public void setAebPageE3_3(List<AEBPageE3_3> aebPageE3_3) {
        _aebPageE3_3 = aebPageE3_3;
    }

    public void addAebPageE3_3() {
        AEBPageE3_3 page = new AEBPageE3_3();
        page.setBaseInformation(this);
        _aebPageE3_3.add(page);
    }

    public void removeAebPageE3_3(AEBPageE3_3 page) {
        _aebPageE3_3.remove(page);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doBaseInformationId")
    private List<PsyDocument> _psyDocument = new ArrayList<>();

    public List<PsyDocument> getPsyDocument() {
        return _psyDocument;
    }

    public void setPsyDocument(List<PsyDocument> psyDocument) {
        _psyDocument = psyDocument;
    }

    public void addPsyDocument(PsyDocument doc) {
        doc.setBaseInformation(this);
        _psyDocument.add(doc);
    }

    public void removePsyDocument(PsyDocument doc) {
        _psyDocument.remove(doc);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paBaseInformationId")
    private List<PersonalAgreed> _personalAgreed = new ArrayList<>();

    public List<PersonalAgreed> getPersonalAgreed() {
        return _personalAgreed;
    }

    public void setPersonalAgreed(List<PersonalAgreed> psyDocument) {
        _personalAgreed = psyDocument;
    }

    public void addPersonalAgreed(PersonalAgreed agreed) {
        agreed.setBaseInformation(this);
        _personalAgreed.add(agreed);
    }

    public void removePersonalAgreed(PersonalAgreed agreed) {
        _personalAgreed.remove(agreed);
    }

    @OneToMany(mappedBy = "_baseInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rspBaseInformationId")
    private List<RegionStructurParticularities> _regionStructurParticularities = new ArrayList<>();

    public List<RegionStructurParticularities> getRegionStructurParticularities() {
        return _regionStructurParticularities;
    }

    public void setRegionStructurParticularities(List<RegionStructurParticularities> regionStructurParticularities) {
        this._regionStructurParticularities = regionStructurParticularities;
    }

    public void addNewRegionStructurParticularities() {
        RegionStructurParticularities value = new RegionStructurParticularities();
        value.setBaseInformation(this);
        _regionStructurParticularities.add(value);
    }

    public void removeRegionStructurParticularities(RegionStructurParticularities value) {
        _regionStructurParticularities.remove(value);
    }

    public void addAebPageE1_1(AEBPageE1_1 page) {
        page.setBaseInformation(this);
        _aebPageE1_1.add(page);
    }

    public void addAebPageE1_2(AEBPageE1_2 page) {
        page.setBaseInformation(this);
        _aebPageE1_2.add(page);
    }

    public void addAebPageE2(AEBPageE2 page) {
        page.setBaseInformation(this);
        _aebPageE2.add(page);
    }

    public void addAebPageE3_1(AEBPageE3_1 page) {
        page.setBaseInformation(this);
        _aebPageE3_1.add(page);
    }

    public void addAebPageE3_2(AEBPageE3_2 page) {
        page.setBaseInformation(this);
        _aebPageE3_2.add(page);
    }

    public void addAebPageE3_3(AEBPageE3_3 page) {
        page.setBaseInformation(this);
        _aebPageE3_3.add(page);
    }

    public void clearAebPages() {
        _aebPageE1_1.clear();
        _aebPageE1_2.clear();
        _aebPageE2.clear();
        _aebPageE3_1.clear();
        _aebPageE3_2.clear();
        _aebPageE3_3.clear();
    }
}
