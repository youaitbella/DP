package org.inek.dataportal.common.data.adm;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.converter.FeatureConverter;
import org.inek.dataportal.common.enums.WorkflowStatus;

/**
 *
 * @author lautenti
 */
@Entity
@Table(name = "ActionLog", schema = "adm")
public class ActionLog implements Serializable {

    public ActionLog(Account account,
            Feature feature,
            int entryId,
            WorkflowStatus workflowStatus) {
        this._accountId = account.getId();
        this._feature = feature;
        this._entryId = entryId;
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

    //<editor-fold defaultstate="collapsed" desc="Property WorkflowStatus">
    @Column(name = "alWorkflowStatusId")
    private WorkflowStatus _workflowStatus ;

    public WorkflowStatus getWorkflowStatus() {
        return _workflowStatus;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property EntryId">
    @Column(name = "alEntryId")
    private int _entryId;

    public int getEntryId() {
        return _entryId;
    }
    //</editor-fold>

}
