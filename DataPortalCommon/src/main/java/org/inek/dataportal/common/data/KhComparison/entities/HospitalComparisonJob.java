package org.inek.dataportal.common.data.KhComparison.entities;

import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "HospitalComparisonJob", schema = "psy")
public class HospitalComparisonJob implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hcsId")
    private int _id;
    //<editor-fold defaultstate="collapsed" desc="Property version">
    @Column(name = "hcsVersion")
    @Version
    private int _version;
    //<editor-fold defaultstate="collapsed" desc="Property hceEvaluationTypeId">
    @OneToOne
    @JoinColumn(name = "hcsHospitalComparisonInfoId")
    private HospitalComparisonInfo _hospitalComparisonInfo;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Status">
    @Column(name = "hcsStatus")
    private String _status;

    public PsyHosptalComparisonStatus getStatus() {
        return PsyHosptalComparisonStatus.valueOf(_status);
    }

    public void setStatus(PsyHosptalComparisonStatus status) {
        this._status = status.name();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hcsCreatedAt">
    @Column(name = "hcsCreatedAt", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date _createdDate;
    //<editor-fold defaultstate="collapsed" desc="Property hcsStartWorking">
    @Column(name = "hcsStartWorking")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _startWorking = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));
    //<editor-fold defaultstate="collapsed" desc="Property hcsEndWorking">
    @Column(name = "hcsEndWorking")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _endWorking = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));
    //</editor-fold>

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public HospitalComparisonInfo getHosptalComparisonInfo() {
        return _hospitalComparisonInfo;
    }
    //</editor-fold>

    public void setHosptalComparisonInfo(HospitalComparisonInfo hospitalComparisonInfo) {
        this._hospitalComparisonInfo = hospitalComparisonInfo;
    }
    //</editor-fold>


    public Date getStartWorking() {
        return _startWorking;
    }
    //</editor-fold>

    public void setStartWorking(Date startWorking) {
        this._startWorking = startWorking;
    }

    ;

    public Date getEndWorking() {
        return _endWorking;
    }

    public void setEndWorking(Date endWorking) {
        this._endWorking = endWorking;
    }
    //</editor-fold>

    public String getJobFolder(String baseDir) {
        return baseDir + "/" + _id;
    }

    public String getEvaluationFilePath(String baseDir) {
        return getJobFolder(baseDir) + "/" + getEvaluationFileName();
    }

    public String getEvaluationFileName() {
        String fileNamePattern = "%s_%s_Auswertungen_KH_Vergleich.zip";
        return String.format(fileNamePattern, _hospitalComparisonInfo.getHospitalComparisonId(),
                _hospitalComparisonInfo.getHospitalIk());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HospitalComparisonJob that = (HospitalComparisonJob) o;
        return _id == that._id &&
                _version == that._version &&
                Objects.equals(_hospitalComparisonInfo, that._hospitalComparisonInfo) &&
                Objects.equals(_status, that._status) &&
                Objects.equals(_startWorking, that._startWorking) &&
                Objects.equals(_endWorking, that._endWorking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _version, _hospitalComparisonInfo, _status, _startWorking, _endWorking);
    }
}
