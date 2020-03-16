package org.inek.dataportal.base.feature.approval.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ItemData", schema = "conf")
public class ItemData {
    public ItemData() {
    }

    public ItemData(ItemBlock itemBlock) {
        this.itemBlock = itemBlock;
    }

    private static final long serialVersionUID = 1L;

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

    // <editor-fold defaultstate="collapsed" desc="Property ItemBlock">
    @ManyToOne
    @JoinColumn(name = "idItemBlockId")
    private ItemBlock itemBlock;

    public ItemBlock getItemBlock() {
        return itemBlock;
    }

    public void setItemBlock(ItemBlock itemBlockId) {
        this.itemBlock = itemBlockId;
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
        return id.equals(itemData.id) &&
                itemBlock.equals(itemData.itemBlock) &&
                key.equals(itemData.key) &&
                value.equals(itemData.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemBlock, key, value);
    }
    //</editor-fold>
}
