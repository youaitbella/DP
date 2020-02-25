package org.inek.dataportal.base.feature.approval.entities;

import org.inek.dataportal.common.utils.DateUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ItemRecipient", schema = "conf")
public class ItemRecipient {
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

    // <editor-fold defaultstate="collapsed" desc="Property ItemId">
    @Column(name = "irItemId")
    private int itemId = -1;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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
        return itemId == that.itemId &&
                accountId == that.accountId &&
                id.equals(that.id) &&
                emailAddress.equals(that.emailAddress) &&
                firstViewedDt.equals(that.firstViewedDt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, accountId, emailAddress, firstViewedDt);
    }
    //</editor-fold>
}
