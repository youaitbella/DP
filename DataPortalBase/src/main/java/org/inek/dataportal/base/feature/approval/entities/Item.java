package org.inek.dataportal.base.feature.approval.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Item", schema = "conf")
public class Item {
    private static final long serialVersionUID = 1L;

    public Item() {
    }

    public Item(Action action) {
        this.action = action;
    }

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

    // <editor-fold defaultstate="collapsed" desc="Property Action">
    @ManyToOne
    @JoinColumn(name = "itActionId")
    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
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

    //<editor-fold desc="Property Recipients">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "irItemId", referencedColumnName = "itId")
    private List<ItemRecipient> recipients = new ArrayList<>();

    public List<ItemRecipient> getRecipients() {
        return Collections.unmodifiableList(recipients);
    }
    //</editor-fold>

    //<editor-fold desc="Property Blocks">
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ibItemId", referencedColumnName = "itId")
    private List<ItemBlock> blocks = new ArrayList<>();

    public List<ItemBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }
    //</editor-fold>

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return ik == item.ik &&
                id.equals(item.id) &&
                action.equals(item.action) &&
                recipients.equals(item.recipients) &&
                blocks.equals(item.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, ik, recipients, blocks);
    }
    //</editor-fold>
}
