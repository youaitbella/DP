/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.nub;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "NubFormerRequest")
public class NubFormerRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nfrId")
    private int _id = -1;
    
    @Column(name = "nfrExternalId")
    @Size(max = 10, message = "Die NUB-Nummer darf maximal 10 Zeichen lang sein.")
    private String _externalId = "";
    
    @Column(name = "nfrIK")
    @Pattern(regexp="/^\\d{9}$/", message = "Die angegebene IK entspricht nicht dem korrekten Format.")
    private int _ik;
    
    @Column(name = "nfrNubName")
    private String _name = "";
    
    @Column(name = "nfrInekMethod")
    private int _inekMethod;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getExternalId() {
        return _externalId;
    }

    public void setExternalId(String _externalId) {
        this._externalId = _externalId;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int _ik) {
        this._ik = _ik;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public int getInekMethod() {
        return _inekMethod;
    }

    public void setInekMethod(int _inekMethod) {
        this._inekMethod = _inekMethod;
    }
}
