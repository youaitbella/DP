package org.inek.dataportal.common.data.common;

import org.inek.dataportal.common.helper.EnvironmentInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Synchronizer", schema = "dbo")
public class Synchronizer {

    public Synchronizer() {
    }

    public Synchronizer(String key) {
        this.key = key;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Key">
    @Id
    @Column(name = "syncKey")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Server">
    @Column(name = "syncServer")
    private String server = EnvironmentInfo.getLocalServerName();

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DateTime">
    @Column(name = "syncDateTime")
    private Date dateTime = new Date();

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    // </editor-fold>


}
