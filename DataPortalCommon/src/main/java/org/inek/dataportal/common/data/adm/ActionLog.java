package org.inek.dataportal.common.data.adm;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;
import org.inek.dataportal.common.data.converter.WorkflowStatusConverter;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "ActionLog", schema = "adm")
public class ActionLog implements Serializable {

    public ActionLog(int accountId,
            Feature feature,
            int entryId,
            WorkflowStatus workflowStatus) {
        _accountId = accountId;
        _feature = feature;
        _entryId = entryId;
        _workflowStatus = workflowStatus;
    }

    public ActionLog() {
    }

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alId")
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property TimeStamp">
    @Column(name = "alTimeStamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _timeStamp = new Date();

    public Date getTimeStamp() {
        return _timeStamp;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "alAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "alFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;

    public Feature getFeature() {
        return _feature;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property EntryId">
    @Column(name = "alEntryId")
    private int _entryId;

    public int getEntryId() {
        return _entryId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property WorkflowStatus">
    @Column(name = "alWorkflowStatusId")
    @Convert(converter = WorkflowStatusConverter.class)
    private WorkflowStatus _workflowStatus ;

    public WorkflowStatus getWorkflowStatus() {
        return _workflowStatus;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals">
    @Override
    public int hashCode() {
        return 59;
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
        final ActionLog other = (ActionLog) obj;
        if (_id != other._id) {
            return false;
        }
        if (_accountId != other._accountId) {
            return false;
        }
        if (_entryId != other._entryId) {
            return false;
        }
        if (!Objects.equals(_timeStamp, other._timeStamp)) {
            return false;
        }
        if (_feature != other._feature) {
            return false;
        }
        return true;
    }
    //</editor-fold>
    
}
