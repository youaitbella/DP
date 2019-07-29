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
 *
 * @author lautenti
 */
@Entity
@Table(name = "HosptalComparisonJob", schema = "psy")
public class HosptalComparisonJob implements Serializable {

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hcsId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property version">
    @Column(name = "hcsVersion")
    @Version
    private int _version;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hceEvaluationTypeId">
    @OneToOne
    @JoinColumn(name = "hcsHosptalComparisonInfoId")
    private HosptalComparisonInfo _hosptalComparisonInfo;

    public HosptalComparisonInfo getHosptalComparisonInfo() {
        return _hosptalComparisonInfo;
    }

    public void setHosptalComparisonInfo(HosptalComparisonInfo hosptalComparisonInfo) {
        this._hosptalComparisonInfo = hosptalComparisonInfo;
    }
    //</editor-fold>

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

    //<editor-fold defaultstate="collapsed" desc="Property hcsStartWorking">
    @Column(name = "hcsStartWorking")
    @Temporal(TemporalType.DATE)
    private Date _startWorking = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getStartWorking() {
        return _startWorking;
    }

    public void setStartWorking(Date startWorking) {
        this._startWorking = startWorking;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property hcsEndWorking">
    @Column(name = "hcsEndWorking")
    @Temporal(TemporalType.DATE)
    private Date _endWorking = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    public Date getEndWorking() {
        return _endWorking;
    }

    public void setEndWorking(Date endWorking) {
        this._endWorking = endWorking;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HosptalComparisonJob that = (HosptalComparisonJob) o;
        return _id == that._id &&
                _version == that._version &&
                Objects.equals(_hosptalComparisonInfo, that._hosptalComparisonInfo) &&
                Objects.equals(_status, that._status) &&
                Objects.equals(_startWorking, that._startWorking) &&
                Objects.equals(_endWorking, that._endWorking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _version, _hosptalComparisonInfo, _status, _startWorking, _endWorking);
    }
}
