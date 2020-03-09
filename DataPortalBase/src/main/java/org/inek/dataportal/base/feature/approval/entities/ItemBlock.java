package org.inek.dataportal.base.feature.approval.entities;

import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ItemBlock", schema = "conf")
public class ItemBlock {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ibId")
    private Integer id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ItemId">
    @Column(name = "ibItemId")
    private int itemId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Header">
    @Column(name = "ibHeader")
    private String header = "";

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ConfStateId">
    @Column(name = "ibConfStateId")
    private String confStateId = "";

    public String getConfStateId() {
        return confStateId;
    }

    public void setConfStateId(String confStateId) {
        this.confStateId = confStateId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ConfDt">
    @Column(name = "ibConfDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confDt = DateUtils.MIN_DATE;

    public Date getConfDt() {
        return confDt;
    }

    public void setConfDt(Date confDt) {
        this.confDt = confDt;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ConfAccountId">
    @Column(name = "ibConfAccountId")
    private int confAccountId;

    public int getConfAccountId() {
        return confAccountId;
    }

    public void setConfAccountId(int confAccountId) {
        this.confAccountId = confAccountId;
    }
    // </editor-fold>


    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemBlock itemBlock = (ItemBlock) o;
        return itemId == itemBlock.itemId &&
                confAccountId == itemBlock.confAccountId &&
                id.equals(itemBlock.id) &&
                header.equals(itemBlock.header) &&
                confStateId.equals(itemBlock.confStateId) &&
                confDt.equals(itemBlock.confDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, header, confStateId, confDt, confAccountId);
    }
    //</editor-fold>
}
