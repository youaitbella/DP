/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.calc.backingbean.CalcBasicsStaticData;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLListEndoscopyAmbulant", schema = "calc")
@XmlRootElement
public class KGLListEndoscopyAmbulant implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaId", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    @Column(name = "leaBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    
    @Column(name = "leaAmbulantService")
    @Documentation(name = "Ambulante Leistungen im endoskopischen Bereich", rank = 5)
    private String _ambulantService = "";

    @Size(max = 255)
    public String getAmbulantService() {
        return _ambulantService;
    }

    public void setAmbulantService(String ambulantService) {
        this._ambulantService = ambulantService;
    }
    
    @Column(name = "leaServiceKey")
    //@Documentation(name = "Leistungsschlüssel", rank = 10)
    private int _serviceKey = -1;

    public int getServiceKey() {
        return _serviceKey;
    }
    
    @Documentation(name = "Leistungsschlüssel", rank = 10)
    @JsonIgnore
    public String getServiceKeyText(){
        return CalcBasicsStaticData.staticGetServiceKeyItems()
                .stream()
                .filter(i -> (int)i.getValue() == _serviceKey)
                .findAny().orElse(new SelectItem(-1, ""))
                .getLabel();
    }

    public void setServiceKey(int serviceKey) {
        this._serviceKey = serviceKey;
    }
    
    @Column(name = "leaNumServices")
    @Documentation(name = "Anzahl erbrachter amb. Leistungen", rank = 20)
    private int _numServices = 0;

    public int getNumServices() {
        return _numServices;
    }

    public void setNumServices(int numServices) {
        this._numServices = numServices;
    }
    
    @Column(name = "leaCostVolumeMedical")
    @Documentation(name = "abgegr. Kostenvolumen ÄD", rank = 30)
    private double _costVolumeMedical = 0.0;

    public double getCostVolumeMedical() {
        return _costVolumeMedical;
    }

    public void setCostVolumeMedical(double costVolumeMedical) {
        this._costVolumeMedical = costVolumeMedical;
    }
    
    @Column(name = "leaCostVolumeFunction")
    @Documentation(name = "abgegr. Kostenvolumen FD", rank = 30)
    private double _costVolumeFunction = 0.0;

    public double getCostVolumeFunction() {
        return _costVolumeFunction;
    }

    public void setCostVolumeFunction(double costVolumeFunction) {
        this._costVolumeFunction = costVolumeFunction;
    }
    
    @Column(name = "leaCostVolumeMedInfra")
    @Documentation(name = "abgegr. Kostenvolumen  med. Infrastruktur", rank = 40)
    private double _costVolumeMedInfra = 0.0;

    public double getCostVolumeMedInfra() {
        return _costVolumeMedInfra;
    }

    public void setCostVolumeMedInfra(double costVolumeMedInfra) {
        this._costVolumeMedInfra = costVolumeMedInfra;
    }
    
    @Column(name = "leaCostVolumeNonMedInfra")
    @Documentation(name = "abgegr. Kostenvolumen  nicht med. Infrastruktur", rank = 40)
    private double _costVolumeNonMedInfra = 0.0;

    public double getCostVolumeNonMedInfra() {
        return _costVolumeNonMedInfra;
    }

    public void setCostVolumeNonMedInfra(double costVolumeNonMedInfra) {
        this._costVolumeNonMedInfra = costVolumeNonMedInfra;
    }
    
    @Column(name = "leaMiscText")
    @Documentation(name = "Beschreibung", rank = 40)
    private String _miscText = "";

    @Size(max = 255)
    public String getMiscText() {
        return _miscText;
    }

    public void setMiscText(String miscText) {
        this._miscText = miscText;
    }
}
