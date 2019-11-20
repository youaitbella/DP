package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.time.*;
import java.util.Date;
import java.util.Objects;

@NamedQuery(name = "Extension.findByCoordinates",
        query = "select e from Extension e where e._ik = :ik and e._year = :year and e._quarter = :quarter")
@Entity
@Table(name = "ProofDeadlineForIk", schema = "care")
@IdClass(ExtensionId.class)
public class Extension {

    public Extension() {

    }

    public Extension(int ik, int year, int quarter, int accountId) {
        this._ik = ik;
        this._year = year;
        this._quarter = quarter;
        this._accountId = accountId;
        calculateDeadline(year, quarter);
    }

    private void calculateDeadline(int year, int quarter) {
        switch (quarter) {
            case 1:
                _deadline = createDate(29,Month.APRIL, year);
                break;
            case 2:
                _deadline = createDate(29,Month.JULY, year);
                break;
            case 3:
                _deadline = createDate(29,Month.OCTOBER, year);
                break;
            case 4:
                _deadline = createDate(29,Month.JANUARY, year + 1);
                break;
            default:
                throw new IllegalArgumentException("Unknows quarder: " + quarter);
        }
    }

    private Date createDate(int day, Month month, int year) {
        LocalDateTime datetime = LocalDateTime.of(year, month, day, 23, 59, 59);

        return java.util.Date.from(datetime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    //<editor-fold desc="Property Id">
    @Id
    @Column(name = "pdIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }
    //</editor-fold>

    //<editor-fold desc="Property Year">
    @Id
    @Column(name = "pdYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        this._year = year;
    }
    //</editor-fold>

    //<editor-fold desc="Property Quarter">
    @Id
    @Column(name = "pdQuarter")
    private int _quarter;

    public int getQuarter() {
        return _quarter;
    }

    public void setQuarter(int quarter) {
        this._quarter = quarter;
    }
    //</editor-fold>

    //<editor-fold desc="Property Date">
    @Column(name = "pdDeadline")
    private Date _deadline = new Date();

    public Date getDeadline() {
        return _deadline;
    }

    public void setDeadline(Date deadline) {
        this._deadline = deadline;
    }
    //</editor-fold>

    //<editor-fold desc="Property Account Id">
    @Column(name = "pdAccountId")
    private int _accountId = 0;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        this._accountId = accountId;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extension extension = (Extension) o;
        return _ik == extension._ik &&
                _year == extension._year &&
                _quarter == extension._quarter &&
                _accountId == extension._accountId &&
                Objects.equals(_deadline, extension._deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_ik, _year, _quarter, _deadline, _accountId);
    }
}
