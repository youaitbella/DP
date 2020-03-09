package org.inek.dataportal.base.feature.approval.entities;

import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Action", schema = "conf")
public class Action {
    private static final long serialVersionUID = 1L;

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

    // <editor-fold defaultstate="collapsed" desc="Property ConfType">
    @ManyToOne
    @JoinColumn(name = "acConfTypeId")
    private ConfType confType;

    public ConfType getConfType() {
        return confType;
    }

    public void setConfType(ConfType confTypeId) {
        this.confType = confTypeId;
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

    //<editor-fold desc="Property Items">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "itActionId", referencedColumnName = "acId")
    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
    //</editor-fold>

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return id.equals(action.id) &&
                confType.equals(action.confType) &&
                creationDt.equals(action.creationDt) &&
                infoMailHeader.equals(action.infoMailHeader) &&
                infoMailBody.equals(action.infoMailBody) &&
                items.equals(action.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, confType, creationDt, infoMailHeader, infoMailBody, items);
    }
    //</editor-fold>

}
