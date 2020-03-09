package org.inek.dataportal.base.feature.approval.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ItemData", schema = "conf")
public class ItemData {
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idId")
    private Integer id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ItemBlockId">
    @Column(name = "idItemBlockId")
    private int itemBlockId;

    public int getItemBlockId() {
        return itemBlockId;
    }

    public void setItemBlockId(int itemBlockId) {
        this.itemBlockId = itemBlockId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Key">
    @Column(name = "idKey")
    private String key = "";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Value">
    @Column(name = "idValue")
    private String value = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    // </editor-fold>

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemData itemData = (ItemData) o;
        return itemBlockId == itemData.itemBlockId &&
                id.equals(itemData.id) &&
                key.equals(itemData.key) &&
                value.equals(itemData.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemBlockId, key, value);
    }
    //</editor-fold>
}
