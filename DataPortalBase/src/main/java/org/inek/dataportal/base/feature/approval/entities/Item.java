package org.inek.dataportal.base.feature.approval.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Item", schema = "conf")
public class Item {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itId")
    private Integer id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ActionId">
    @Column(name = "itActionId")
    private int actionId = -1;

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "itIk")
    private int ik = -1;

    public int getIk() {
        return ik;
    }

    public void setIk(int ik) {
        this.ik = ik;
    }
    // </editor-fold>

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return actionId == item.actionId &&
                ik == item.ik &&
                id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actionId, ik);
    }
    //</editor-fold>
}
