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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "KGLListEndoscopyAmbulant", schema = "calc")
@XmlRootElement
public class KGLListEndoscopyAmbulant implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    @Column(name = "leaBaseInformationId")
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int _baseInformationId) {
        this._baseInformationId = _baseInformationId;
    }
    
    @Column(name = "leaAmbulantService")
    private String _ambulantService = "";

    @Size(max = 255)
    public String getAmbulantService() {
        return _ambulantService;
    }

    public void setAmbulantService(String _ambulantService) {
        this._ambulantService = _ambulantService;
    }
    
    @Column(name = "leaServiceKey")
    private int _serviceKey = -1;

    public int getServiceKey() {
        return _serviceKey;
    }

    public void setServiceKey(int _serviceKey) {
        this._serviceKey = _serviceKey;
    }
    
    @Column(name = "leaNumServices")
    private int _numServices = 0;

    public int getNumServices() {
        return _numServices;
    }

    public void setNumServices(int _numServices) {
        this._numServices = _numServices;
    }
    
    @Column(name = "leaCostVolumeMedical")
    private double _costVolumeMedical = 0.0;

    public double getCostVolumeMedical() {
        return _costVolumeMedical;
    }

    public void setCostVolumeMedical(double _costVolumeMedical) {
        this._costVolumeMedical = _costVolumeMedical;
    }
    
    @Column(name = "leaCostVolumeFunction")
    private double _costVolumeFunction = 0.0;

    public double getCostVolumeFunction() {
        return _costVolumeFunction;
    }

    public void setCostVolumeFunction(double _costVolumeFunction) {
        this._costVolumeFunction = _costVolumeFunction;
    }
    
    @Column(name = "leaCostVolumeMedInfra")
    private double _costVolumeMedInfra = 0.0;

    public double getCostVolumeMedInfra() {
        return _costVolumeMedInfra;
    }

    public void setCostVolumeMedInfra(double _costVolumeMedInfra) {
        this._costVolumeMedInfra = _costVolumeMedInfra;
    }
    
    @Column(name = "leaCostVolumeNonMedInfra")
    private double _costVolumeNonMedInfra = 0.0;

    public double getCostVolumeNonMedInfra() {
        return _costVolumeNonMedInfra;
    }

    public void setCostVolumeNonMedInfra(double _costVolumeNonMedInfra) {
        this._costVolumeNonMedInfra = _costVolumeNonMedInfra;
    }
    
    @Column(name = "leaMiscText")
    private String _miscText = "";

    @Size(max = 255)
    public String getMiscText() {
        return _miscText;
    }

    public void setMiscText(String _miscText) {
        this._miscText = _miscText;
    }
}
