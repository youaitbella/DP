package org.inek.dataportal.base.feature.approval.entities;

import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Action", schema = "conf")
public class Action {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acId")
    private Integer id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ConfTypeId">
    @Column(name = "acConfTypeId")
    private int confTypeId = -1;

    public int getConfTypeId() {
        return confTypeId;
    }

    public void setConfTypeId(int confTypeId) {
        this.confTypeId = confTypeId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CreationDt">
    @Column(name = "acCreationDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDt = DateUtils.MIN_DATE;

    public Date getCreationDt() {
        return creationDt;
    }

    public void setCreationDt(Date creationDt) {
        this.creationDt = creationDt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InfoMailHeader">
    @Column(name = "acInfoMailHeader")
    private String infoMailHeader = "";

    public String getInfoMailHeader() {
        return infoMailHeader;
    }

    public void setInfoMailHeader(String infoMailHeader) {
        this.infoMailHeader = infoMailHeader;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property InfoMailBody">
    @Column(name = "acInfoMailBody")
    private String infoMailBody = "";

    public String getInfoMailBody() {
        return infoMailBody;
    }

    public void setInfoMailBody(String infoMailBody) {
        this.infoMailBody = infoMailBody;
    }
    // </editor-fold>

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return confTypeId == action.confTypeId &&
                id.equals(action.id) &&
                creationDt.equals(action.creationDt) &&
                infoMailHeader.equals(action.infoMailHeader) &&
                infoMailBody.equals(action.infoMailBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, confTypeId, creationDt, infoMailHeader, infoMailBody);
    }
    //</editor-fold>
}
