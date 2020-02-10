package org.inek.dataportal.common.data.KhComparison.entities;

import org.inek.dataportal.common.data.account.entities.Account;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "InekComparisonJob", schema = "psy")
public class InekComparisonJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icjId")
    private int id;

    @Column(name = "icjVersion")
    @Version
    private int version;

    @Column(name = "icjCreatedAt", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));

    @Column(name = "icjStartWorking")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startWorking = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    @Column(name = "icjEndWorking")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endWorking = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    @Column(name = "icjDataYear")
    private int dataYear;

    @Column(name = "icjAebUpTo")
    private String aebUpTo;

    @ManyToOne
    @JoinColumn(name = "icjAccountId")
    private Account account;

    public InekComparisonJob() {
    }

    public InekComparisonJob(Account account, int inekDataYear, String inekAebSendDateUpToConsider) {
        this.account = account;
        dataYear = inekDataYear;
        aebUpTo = inekAebSendDateUpToConsider;
    }

    public static boolean checkAebToConsider(String inekAebSendDateUpToConsider) {
        try {
            LocalDate.parse(inekAebSendDateUpToConsider, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Expected Format (yyyy-MM-dd) not given: " + inekAebSendDateUpToConsider);
        }
        return true;
    }

    public static boolean checkDataYear(int inekDataYear) {
        if (inekDataYear < 2018 || inekDataYear > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("DataYear only allowed for range 2018 to " + LocalDate.now().getYear() + " inclusive");
        }
        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="Properties">

    public int getId() {
        return id;
    }

//    public void set_id(int _id) {
//        this._id = _id;
//    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getStartWorking() {
        return startWorking;
    }

    public void setStartWorking(Date startWorking) {
        this.startWorking = startWorking;
    }

    public Date getEndWorking() {
        return endWorking;
    }

    public void setEndWorking(Date endWorking) {
        this.endWorking = endWorking;
    }

    public int getDataYear() {
        return dataYear;
    }

    public void setDataYear(int dataYear) {
        this.dataYear = dataYear;
    }

    public String getAebUpTo() {
        return aebUpTo;
    }

    public void setAebUpTo(String aebUpTo) {
        this.aebUpTo = aebUpTo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InekComparisonJob)) return false;
        InekComparisonJob that = (InekComparisonJob) o;
        return id != 0 ? id == that.id :
                dataYear == that.dataYear &&
                createdDate.equals(that.createdDate) &&
                startWorking.equals(that.startWorking) &&
                endWorking.equals(that.endWorking) &&
                Objects.equals(aebUpTo, that.aebUpTo) &&
                Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return id != 0 ? Objects.hash(id) : Objects.hash(createdDate, startWorking, endWorking, dataYear, aebUpTo, account);
    }
}
