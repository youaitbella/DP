package org.inek.dataportal.base.feature.approval.entities;

import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "ItemBlock", schema = "conf")
public class ItemBlock {
    public ItemBlock() {
    }

    public ItemBlock(Item item) {
        this.item = item;
    }

    private static final long serialVersionUID = 1L;

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

    // <editor-fold defaultstate="collapsed" desc="Property Item">
    @ManyToOne
    @JoinColumn(name = "ibItemId")
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item itemId) {
        this.item = itemId;
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
    @ManyToOne
    @JoinColumn(name = "ibConfStateId")
    private ConfState confState;

    public ConfState getConfState() {
        return confState;
    }

    public void setConfState(ConfState confStateId) {
        this.confState = confStateId;
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

    //<editor-fold desc="Property Data">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idItemBlockId", referencedColumnName = "ibId")
    private List<ItemBlock> data = new ArrayList<>();

    public List<ItemBlock> getData() {
        return Collections.unmodifiableList(data);
    }
    //</editor-fold>


    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemBlock itemBlock = (ItemBlock) o;
        return confAccountId == itemBlock.confAccountId &&
                id.equals(itemBlock.id) &&
                item.equals(itemBlock.item) &&
                header.equals(itemBlock.header) &&
                confState.equals(itemBlock.confState) &&
                confDt.equals(itemBlock.confDt) &&
                data.equals(itemBlock.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, header, confState, confDt, confAccountId, data);
    }
//</editor-fold>
}
