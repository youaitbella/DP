/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "KGLListContentTextOps", schema = "calc")
public class KGLListContentTextOps implements Serializable {
    
    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ctoID", updatable = false, nullable = false)
    private int _id = -1;
    
    public int getId() {
        return _id;
    }
    
    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>
    
    @Column(name = "ctoContentTextID")
    private int _contentTextId;

    public int getContentTextId() {
        return _contentTextId;
    }

    public void setContentTextId(int contentTextId) {
        this._contentTextId = contentTextId;
    }
    
    @Column(name = "ctoOpsId")
    private int _opsId;

    public int getOpsId() {
        return _opsId;
    }

    public void setOpsId(int opsId) {
        this._opsId = opsId;
    }
    
    @Column(name = "ctoOpsCode")
    private String _opsCode;

    public String getOpsCode() {
        return _opsCode;
    }

    public void setOpsCode(String opsCode) {
        this._opsCode = opsCode;
    }
}
