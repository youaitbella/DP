package org.inek.dataportal.common.data.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Synchronizer", schema = "dbo")
public class Synchronizer {

    public Synchronizer() {
    }

    public Synchronizer(String key) {
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
    private int server;

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property DateTime">
    @Column(name = "syncDateTime")
    LocalDateTime dateTime = LocalDateTime.now();

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    // </editor-fold>


}
