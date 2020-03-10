package org.inek.dataportal.base.feature.approval.entities;

import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ItemRecipient", schema = "conf")
public class ItemRecipient {
    public ItemRecipient() {
    }

    public ItemRecipient(Item item) {
        this.item = item;
    }

    private static final long serialVersionUID = 1L;

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "irId")
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
    @JoinColumn(name = "irItemId")
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item itemId) {
        this.item = itemId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "irAccountId")
    private int accountId = -1;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property EmailAddress">
    @Column(name = "irEmailAddress")
    private String emailAddress = "";

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FirstViewedDt">
    @Column(name = "irFirstViewedDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstViewedDt = DateUtils.MIN_DATE;

    public Date getFirstViewedDt() {
        return firstViewedDt;
    }

    public void setFirstViewedDt(Date firstViewedDt) {
        this.firstViewedDt = firstViewedDt;
    }
    // </editor-fold>

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRecipient that = (ItemRecipient) o;
        return accountId == that.accountId &&
                id.equals(that.id) &&
                item.equals(that.item) &&
                emailAddress.equals(that.emailAddress) &&
                firstViewedDt.equals(that.firstViewedDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, accountId, emailAddress, firstViewedDt);
    }
    //</editor-fold>
}
