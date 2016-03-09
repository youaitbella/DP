/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

import com.inek.begleitforschung.entities.structural.BedClass;
import com.inek.begleitforschung.entities.structural.CmiClass;
import com.inek.begleitforschung.entities.structural.SizeClass;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author vohldo
 */
@Named
@SessionScoped
public class Entities implements Serializable {

    @Inject private ApplicationData _appData;

    // <editor-fold defaultstate="collapsed" desc="C fields">
    private List<C_111_211> _c_111_211;
    private List<C_111_211> _c_111;
    private List<C_111_211> _c_111_sum;
    private List<C_112_212> _c_112_sum;
    private List<C_111_211> _c_211;
    private List<C_111_211> _c_211_sum;

    private List<C_112_212> _c_112_212;
    private List<C_112_212> _c_112;
    private List<C_112_212> _c_212;
    private List<C_112_212> _c_212_sum;

    private List<C_113_213> _c_113_213;
    private List<C_113_213> _c_113;
    private List<C_113_213> _c_213;

    private List<C_122_222> _c_122_222;
    private List<C_122_222> _c_122_A;
    private List<C_122_222> _c_122_E;
    private List<C_122_222> _c_222_A;
    private List<C_122_222> _c_222_E;

    private List<NumOperations> _numOperations;
    private List<NumOperations> _numOperationsPrimary;
    private List<NumOperations> _numOperationsSlipMc;

    private List<PartialInpatientCare> _partialInpatientCares;
    private List<PartialInpatientCare> _partialInpatientCaresPdChapter;
    private List<PartialInpatientCare> _partialInpatientCaresPdGroup;
    private List<PartialInpatientCare> _partialInpatientCaresPdCat;
    private List<PartialInpatientCare> _partialInpatientCaresProcChapter;
    private List<PartialInpatientCare> _partialInpatientCaresProcArea;
    
    private List<PrimaryDiagsProcs> _primaryDiagsProcs;
    private List<PrimaryDiagsProcs> _primaryDiagsInpatientPdSum;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsInpatientPdChapter;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsInpatientPdGroup;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsInpatientPdCat;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsInpatientProcChapter;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsInpatientProcArea;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsInpatientProcCode;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsSlipMcPdChapter;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsSlipMcPdGroup;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsSlipMcPdCat;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsSlipMcProcChapter;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsSlipMcProcArea;
    private List<PrimaryDiagsProcs> _primaryDiagsProcsSlipMcProcCode;
    
    private List<DataQuality> _dataQuality;
    
    private List<UnspecificCoding> _unspecificCoding;
    
    private List<Participation> _participation;
    
    private List<SystemRated> _systemRated;
    private List<SystemRated> _systemRatedPdLessComplex;
    private List<SystemRated> _systemRatedPdComplex;
    private List<SystemRated> _systemRatedPdFrequently;
    private List<SystemRated> _systemRatedSlipMcLessComplex;
    private List<SystemRated> _systemRatedSlipMcComplex;
    private List<SystemRated> _systemRatedSlipMcFrequently;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="C">
    public List<BedClass> getBedClasses(int dataYear) {
        List<String[]> data = _appData.readDataFile(dataYear, "B_1_KH_Bundesland_Groesse(Betten)");
        return data.stream()
                .map(d -> new BedClass(d[1], d[5], Integer.parseInt(d[2]), Double.parseDouble(d[3]), Integer.parseInt(d[4])))
                .collect(Collectors.toList());
    }

    public List<SizeClass> getSizeClasses(int dataYear) {
        List<String[]> data = _appData.readDataFile(dataYear, "B_2_KH_Traeger_Groesse(Faelle)");
        return data.stream()
                .map(d -> new SizeClass(d[1], d[6], Integer.parseInt(d[2]), Double.parseDouble(d[3]), Integer.parseInt(d[5])))
                .collect(Collectors.toList());
    }

    public List<CmiClass> getCmiClasses(int dataYear) {
        List<String[]> data = _appData.readDataFile(dataYear, "B_3_KH_Groesse(Betten)_CMI");
        return data.stream()
                .map(d -> new CmiClass(d[1], d[5], Integer.parseInt(d[2]), Double.parseDouble(d[3]), Integer.parseInt(d[0])))
                .collect(Collectors.toList());
    }

    public List<C_111_211> getC_111(int dataYear) {
        if (_c_111 == null) {
            _c_111 = getC_111_211(dataYear).stream().filter(c -> (c.getType() == 1)).collect(Collectors.toList());
        }
        return _c_111;
    }

    public List<C_111_211> getC_211(int dataYear) {
        if (_c_211 == null) {
            _c_211 = getC_111_211(dataYear).stream().filter((c) -> (c.getType() == 2)).collect(Collectors.toList());
        }
        return _c_211;
    }

    private List<C_111_211> getC_111_211(int dataYear) {
        if (_c_111_211 == null) {
            _c_111_211 = mapC_111_211(_appData.readDataFile(dataYear, "C_111_211"));
        }
        return _c_111_211;
    }

    public List<C_112_212> getC_112(int dataYear) {
        if (_c_112 == null) {
            _c_112 = getC_112_212(dataYear).stream().filter((c) -> (c.getType() == 1)).collect(Collectors.toList());
        }
        return _c_112;
    }

    public List<C_112_212> getC_212(int dataYear) {
        if (_c_212 == null) {
            _c_212 = getC_112_212(dataYear).stream().filter((c) -> (c.getType() == 2)).collect(Collectors.toList());
        }
        return _c_212;
    }

    private List<C_112_212> getC_112_212(int dataYear) {
        if (_c_112_212 == null) {
            _c_112_212 = mapC_112_212(_appData.readDataFile(dataYear, "C_112_212"));
        }
        return _c_112_212;
    }

    public List<C_113_213> getC_113(int dataYear) {
        if (_c_113 == null) {
            _c_113 = getC_113_213(dataYear).stream().filter((c) -> (c.getType() == 1)).collect(Collectors.toList());
        }
        return _c_113;
    }

    public List<C_113_213> getC_213(int dataYear) {
        if (_c_213 == null) {
            _c_213 = getC_113_213(dataYear).stream().filter((c) -> (c.getType() == 2)).collect(Collectors.toList());
        }
        return _c_213;
    }

    private List<C_113_213> getC_113_213(int dataYear) {
        if (_c_113_213 == null) {
            _c_113_213 = mapC_113_213(_appData.readDataFile(dataYear, "C_113_213"));
        }
        return _c_113_213;
    }

    public List<C_121_221_State_Size> getC_121_221_State_Size(int dataYear) {
        return mapC_121_221_State_Size(_appData.readDataFile(dataYear, "C_121_221_Bundesland_Groesse(Betten)_FZ_VWD_CMI"));
    }

    public List<C_122_222> getC_122_A(int dataYear) {
        if (_c_122_A == null) {
            _c_122_A = getC_122_222(dataYear).stream().filter(c -> (c.getType() == 1 && c.getType2() == 'A')).collect(Collectors.toList());
        }
        return _c_122_A;
    }

    public List<C_122_222> getC_122_E(int dataYear) {
        if (_c_122_E == null) {
            _c_122_E = getC_122_222(dataYear).stream().filter(c -> (c.getType() == 1 && c.getType2() == 'E')).collect(Collectors.toList());
        }
        return _c_122_E;
    }

    public List<C_122_222> getC_222_A(int dataYear) {
        if (_c_222_A == null) {
            _c_222_A = getC_122_222(dataYear).stream().filter(c -> (c.getType() == 2 && c.getType2() == 'A')).collect(Collectors.toList());
        }
        return _c_222_A;
    }

    public List<C_122_222> getC_222_E(int dataYear) {
        if (_c_222_E == null) {
            _c_222_E = getC_122_222(dataYear).stream().filter(c -> (c.getType() == 2 && c.getType2() == 'E')).collect(Collectors.toList());
        }
        return _c_222_E;
    }

    private List<C_122_222> getC_122_222(int dataYear) {
        if (_c_122_222 == null) {
            _c_122_222 = mapC_122_222(_appData.readDataFile(dataYear, "C_122_222"));
        }
        return _c_122_222;
    }

    public List<NumOperations> getNumOperationsPrimary(int dataYear) {
        if (_numOperationsPrimary == null) {
            _numOperationsPrimary = getNumOperations(dataYear).stream().filter(c -> (c.getType() == 1)).collect(Collectors.toList());
        }
        return _numOperations;
    }

    public List<NumOperations> getNumOperationsSlipMc(int dataYear) {
        if (_numOperationsSlipMc == null) {
            _numOperationsSlipMc = getNumOperations(dataYear).stream().filter(c -> (c.getType() == 2)).collect(Collectors.toList());
        }
        return _numOperationsSlipMc;
    }

    private List<NumOperations> getNumOperations(int dataYear) {
        if (_numOperations == null) {
            _numOperations = mapNumOperations(_appData.readDataFile(dataYear, "C_123_223"));
        }
        return _numOperations;
    }

    public List<PartialInpatientCare> getPartialInpatientCaresPdChapter(int dataYear) {
        if (_partialInpatientCaresPdChapter == null) {
            _partialInpatientCaresPdChapter = getInpatientCares(dataYear).stream().filter(c -> (c.getType() == 1)).collect(Collectors.toList());
        }
        return _partialInpatientCaresPdChapter;
    }

    public List<PartialInpatientCare> getPartialInpatientCaresPdGroup(int dataYear) {
        if (_partialInpatientCaresPdGroup == null) {
            _partialInpatientCaresPdGroup = getInpatientCares(dataYear).stream().filter(c -> (c.getType() == 2)).collect(Collectors.toList());
        }
        return _partialInpatientCaresPdGroup;
    }

    public List<PartialInpatientCare> getPartialInpatientCaresPdCat(int dataYear) {
        if (_partialInpatientCaresPdCat == null) {
            _partialInpatientCaresPdCat = getInpatientCares(dataYear).stream().filter(c -> (c.getType() == 3)).collect(Collectors.toList());
        }
        return _partialInpatientCaresPdCat;
    }

    public List<PartialInpatientCare> getPartialInpatientCaresProcChapter(int dataYear) {
        if (_partialInpatientCaresProcChapter == null) {
            _partialInpatientCaresProcChapter = getInpatientCares(dataYear).stream().filter(c -> (c.getType() == 4)).collect(Collectors.toList());
        }
        return _partialInpatientCaresProcChapter;
    }

    public List<PartialInpatientCare> getPartialInpatientCaresProcArea(int dataYear) {
        if (_partialInpatientCaresProcArea == null) {
            _partialInpatientCaresProcArea = getInpatientCares(dataYear).stream().filter(c -> (c.getType() == 5)).collect(Collectors.toList());
        }
        return _partialInpatientCaresProcArea;
    }

    private List<PartialInpatientCare> getInpatientCares(int dataYear) {
        if (_partialInpatientCares == null) {
            _partialInpatientCares = mapPartialInpatientCares(_appData.readDataFile(dataYear, "D"));
        }
        return _partialInpatientCares;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsInpatientPdChapter(int dataYear) {
        if(_primaryDiagsProcsInpatientPdChapter == null) {
            _primaryDiagsProcsInpatientPdChapter = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 1 && c.getType2() == 1)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsInpatientPdChapter;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsInpatientPdGroup(int dataYear) {
        if(_primaryDiagsProcsInpatientPdGroup == null) {
            _primaryDiagsProcsInpatientPdGroup = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 2 && c.getType2() == 1)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsInpatientPdGroup;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsInpatientPdCat(int dataYear) {
        if(_primaryDiagsProcsInpatientPdCat == null) {
            _primaryDiagsProcsInpatientPdCat = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 3 && c.getType2() == 1)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsInpatientPdCat;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsInpatientProcChapter(int dataYear) {
        if(_primaryDiagsProcsInpatientProcChapter == null) {
            _primaryDiagsProcsInpatientProcChapter = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 1 && c.getType2() == 2)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsInpatientProcChapter;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsInpatientProcArea(int dataYear) {
        if(_primaryDiagsProcsInpatientProcArea == null) {
            _primaryDiagsProcsInpatientProcArea = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 2 && c.getType2() == 2)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsInpatientProcArea;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsInpatientProcCode(int dataYear) {
        if(_primaryDiagsProcsInpatientProcCode == null) {
            _primaryDiagsProcsInpatientProcCode = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 3 && c.getType2() == 2)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsInpatientProcCode;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsSlipMcPdChapter(int dataYear) {
        if(_primaryDiagsProcsSlipMcPdChapter == null) {
            _primaryDiagsProcsSlipMcPdChapter = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 1 && c.getType2() == 3)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsSlipMcPdChapter;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsSlipMcPdGroup(int dataYear) {
        if(_primaryDiagsProcsSlipMcPdGroup == null) {
            _primaryDiagsProcsSlipMcPdGroup = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 2 && c.getType2() == 3)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsSlipMcPdGroup;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsSlipMcPdCat(int dataYear) {
        if(_primaryDiagsProcsSlipMcPdCat == null) {
            _primaryDiagsProcsSlipMcPdCat = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 3 && c.getType2() == 3)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsSlipMcPdCat;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsSlipMcProcChapter(int dataYear) {
        if(_primaryDiagsProcsSlipMcProcChapter == null) {
            _primaryDiagsProcsSlipMcProcChapter = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 1 && c.getType2() == 4)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsSlipMcProcChapter;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsSlipMcProcArea(int dataYear) {
        if(_primaryDiagsProcsSlipMcProcArea == null) {
            _primaryDiagsProcsSlipMcProcArea = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 2 && c.getType2() == 4)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsSlipMcProcArea;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsSlipMcProcCode(int dataYear) {
        if(_primaryDiagsProcsSlipMcProcCode == null) {
            _primaryDiagsProcsSlipMcProcCode = getPrimaryDiagsProcs(dataYear).stream().filter(c -> (c.getType() == 3 && c.getType2() == 4)).collect(Collectors.toList());
        }
        return _primaryDiagsProcsSlipMcProcCode;
    }
    
    private List<PrimaryDiagsProcs> getPrimaryDiagsProcs(int dataYear) {
        if(_primaryDiagsProcs == null) {
            _primaryDiagsProcs = mapPrimaryDiagsProcs(_appData.readDataFile(dataYear, "C_1_2abc"));
        }
        return _primaryDiagsProcs;
    }
    
    public List<PrimaryDiagsProcs> getPrimaryDiagsProcsInpatientPdSum(int dataYear) {
        if(_primaryDiagsInpatientPdSum == null) {
            _primaryDiagsInpatientPdSum = mapPartialInpatientCaresSum(_appData.readDataFile(dataYear, "C_114_sum"));
        }
        return _primaryDiagsInpatientPdSum;
    }
    
    public List<DataQuality> getDataQuality(int dataYear) {
        if(_dataQuality == null) {
            _dataQuality = mapDataQuality(_appData.readDataFile(dataYear, "A_2_Datenqualitaet"));
        }
        return _dataQuality;
    }
    
    public List<UnspecificCoding> getUnspecificCoding(int dataYear) {
        if(_unspecificCoding == null) {
            _unspecificCoding = mapUnspecificCoding(_appData.readDataFile(dataYear, "A_3_Unspezif_Kodierung"));
        }
        return _unspecificCoding;
    }
    
    public List<Participation> getParticipation(int dataYear) {
        if(_participation == null) {
            _participation = mapParticipation(_appData.readDataFile(dataYear, "A_1_KH"));
        }
        return _participation;
    }
    
    public List<SystemRated> getSystemRatedPdLessComplex(int dataYear) {
        if(_systemRatedPdLessComplex == null) {
            _systemRatedPdLessComplex = getSystemRated(dataYear).stream().filter(c -> (c.getType() == 1)).collect(Collectors.toList());
        }
        return _systemRatedPdLessComplex;
    }
    
    public List<SystemRated> getSystemRatedPdComplex(int dataYear) {
        if(_systemRatedPdComplex == null) {
            _systemRatedPdComplex = getSystemRated(dataYear).stream().filter(c -> (c.getType() == 2)).collect(Collectors.toList());
        }
        return _systemRatedPdComplex;
    }
    
    public List<SystemRated> getSystemRatedPdFrequently(int dataYear) {
        if(_systemRatedPdFrequently == null) {
            _systemRatedPdFrequently = getSystemRated(dataYear).stream().filter(c -> (c.getType() == 3)).collect(Collectors.toList());
        }
        return _systemRatedPdFrequently;
    }
    
    public List<SystemRated> getSystemRatedSlipMcLessComplex(int dataYear) {
        if(_systemRatedSlipMcLessComplex == null) {
            _systemRatedSlipMcLessComplex = getSystemRated(dataYear).stream().filter(c -> (c.getType() == 4)).collect(Collectors.toList());
        }
        return _systemRatedSlipMcLessComplex;
    }
    
    public List<SystemRated> getSystemRatedSlipMcComplex(int dataYear) {
        if(_systemRatedSlipMcComplex == null) {
            _systemRatedSlipMcComplex = getSystemRated(dataYear).stream().filter(c -> (c.getType() == 5)).collect(Collectors.toList());
        }
        return _systemRatedSlipMcComplex;
    }
    
    public List<SystemRated> getSystemRatedSlipMcFrequently(int dataYear) {
        if(_systemRatedSlipMcFrequently == null) {
            _systemRatedSlipMcFrequently = getSystemRated(dataYear).stream().filter(c -> (c.getType() == 6)).collect(Collectors.toList());
        }
        return _systemRatedSlipMcFrequently;
    }
    
    private List<SystemRated> getSystemRated(int dataYear) {
        if(_systemRated == null) {
            _systemRated = mapSystemRated(_appData.readDataFile(dataYear, "E"));
        }
        return _systemRated;
    }

    // <editor-fold defaultstate="collapsed" desc="sum">
    public List<C_111_211> getC_111_sum(int dataYear) {
        if(_c_111_sum == null) {
            _c_111_sum = mapC_111_211_sum(_appData.readDataFile(dataYear, "C_111_sum"));
        }
        return _c_111_sum;
    }

    public List<C_111_211> getC_211_sum(int dataYear) {
        if(_c_211_sum == null) {
            _c_211_sum = mapC_111_211_sum(_appData.readDataFile(dataYear, "C_211_sum"));
        }
        return _c_211_sum;
    }

    public List<C_112_212> getC_112_sum(int dataYear) {
        if(_c_112_sum == null) {
            _c_112_sum = map_C_112_212_sum(_appData.readDataFile(dataYear, "C_112_sum"));
        }
        return _c_112_sum;
    }

    public List<C_112_212> getC_212_sum(int dataYear) {
        if(_c_212_sum == null) {
            _c_212_sum = map_C_112_212_sum(_appData.readDataFile(dataYear, "C_212_sum"));
        }
        return _c_212_sum;
    }

    public List<C_113_213> getC_113_sum() {
        List<C_113_213> x = new ArrayList<>();
        C_113_213 sum = new C_113_213(1, "", "", 0, 0.0, 0.0, 0, 0.0, 0, 0.0);
        for (C_113_213 c : _c_113) {
            sum.setSumA(sum.getSumA() + c.getSumA());
            sum.setAvgLos(sum.getAvgLos() + c.getAvgLos());
            sum.setAvgStdDeviation(sum.getAvgStdDeviation() + c.getAvgStdDeviation());
            sum.setSumKla(sum.getSumKla() + c.getSumKla());
            sum.setSumLla(sum.getSumLla() + c.getSumLla());
            sum.setFractionKla(sum.getSumKla() + c.getFractionKla());
            sum.setFractionLla(sum.getSumLla() + c.getSumLla());
        }
        sum.setAvgLos(sum.getAvgLos() / _c_113.size());
        sum.setAvgStdDeviation(sum.getAvgStdDeviation() / _c_113.size());
        sum.setFractionKla((float) sum.getSumKla() / sum.getSumA());
        sum.setFractionLla((float) sum.getSumLla() / sum.getSumA());
        x.add(sum);
        return x;
    }

    public List<C_113_213> getC_213_sum() {
        List<C_113_213> x = new ArrayList<>();
        C_113_213 sum = new C_113_213(1, "", "", 0, 0.0, 0.0, 0, 0.0, 0, 0.0);
        for (C_113_213 c : _c_213) {
            sum.setSumA(sum.getSumA() + c.getSumA());
            sum.setAvgLos(sum.getAvgLos() + c.getAvgLos());
            sum.setAvgStdDeviation(sum.getAvgStdDeviation() + c.getAvgStdDeviation());
            sum.setSumKla(sum.getSumKla() + c.getSumKla());
            sum.setSumLla(sum.getSumLla() + c.getSumLla());
            sum.setFractionKla(sum.getSumKla() + c.getFractionKla());
            sum.setFractionLla(sum.getSumLla() + c.getSumLla());
        }
        sum.setAvgLos(sum.getAvgLos() / _c_213.size());
        sum.setAvgStdDeviation(sum.getAvgStdDeviation() / _c_213.size());
        sum.setFractionKla((float) sum.getSumKla() / sum.getSumA());
        sum.setFractionLla((float) sum.getSumLla() / sum.getSumA());
        x.add(sum);
        return x;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="mapper">
    private List<C_111_211> mapC_111_211(List<String[]> data) {
        List<C_111_211> list = new ArrayList<>();
        for (String[] x : data) {
            C_111_211 y = new C_111_211(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]),
                    Integer.parseInt(x[4]), Integer.parseInt(x[5]), Integer.parseInt(x[6]), Double.parseDouble(x[7]), Double.parseDouble(x[8]), Double.parseDouble(x[9]));
            list.add(y);
        }
        return list;
    }

    private List<C_112_212> mapC_112_212(List<String[]> data) {
        List<C_112_212> list = new ArrayList<>();
        for (String[] x : data) {
            C_112_212 y = new C_112_212(Integer.parseInt(x[0]), x[1], Integer.parseInt(x[2]), Integer.parseInt(x[3]),
                    Integer.parseInt(x[4]), Integer.parseInt(x[5]), Double.parseDouble(x[6]), Double.parseDouble(x[7]), Double.parseDouble(x[8]), x[9]);
            list.add(y);
        }
        return list;
    }

    private List<C_113_213> mapC_113_213(List<String[]> data) {
        List<C_113_213> list = new ArrayList<>();
        for (String[] x : data) {
            C_113_213 y = new C_113_213(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]), Double.parseDouble(x[4]),
                    Double.parseDouble(x[5]), Integer.parseInt(x[6]), Double.parseDouble(x[7]), Integer.parseInt(x[8]), Double.parseDouble(x[9]));
            list.add(y);
        }
        return list;
    }

    private List<C_121_221_State_Size> mapC_121_221_State_Size(List<String[]> data) {
        List<C_121_221_State_Size> list = new ArrayList<>();
        for (String[] x : data) {
            C_121_221_State_Size y = new C_121_221_State_Size(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]), Double.parseDouble(x[4]),
                    Double.parseDouble(x[5]), Double.parseDouble(x[6]), Double.parseDouble(x[7]), x[8], x[9]);
            list.add(y);
        }
        return list;
    }

    private List<C_122_222> mapC_122_222(List<String[]> data) {
        List<C_122_222> list = new ArrayList<>();
        for (String[] x : data) {
            C_122_222 y = new C_122_222(Integer.parseInt(x[0]), x[1].charAt(0), x[2], Integer.parseInt(x[3]), Integer.parseInt(x[4]));
            list.add(y);
        }
        return list;
    }

    private List<NumOperations> mapNumOperations(List<String[]> data) {
        List<NumOperations> list = new ArrayList<>();
        for (String[] x : data) {
            NumOperations y = new NumOperations(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]), Integer.parseInt(x[4]), Double.parseDouble(x[5]),
                    Double.parseDouble(x[6]), Double.parseDouble(x[7]), Double.parseDouble(x[8]), Double.parseDouble(x[9]), Double.parseDouble(x[10]));
            list.add(y);
        }
        return list;
    }

    private List<PartialInpatientCare> mapPartialInpatientCares(List<String[]> data) {
        List<PartialInpatientCare> list = new ArrayList<>();
        for (String[] x : data) {
            PartialInpatientCare y = new PartialInpatientCare(Integer.parseInt(x[0]), x[1], x[2], Integer.parseInt(x[3]), Integer.parseInt(x[4]));
            list.add(y);
        }
        return list;
    }
    
    private List<PrimaryDiagsProcs> mapPrimaryDiagsProcs(List<String[]> data) {
        List<PrimaryDiagsProcs> list = new ArrayList<>();
        for(String[] x : data) {
            PrimaryDiagsProcs y = new PrimaryDiagsProcs(Integer.parseInt(x[0]), Integer.parseInt(x[1]),
                    x[2], x[3], Integer.parseInt(x[4]), Double.parseDouble(x[5]), Double.parseDouble(x[6]),
                    Integer.parseInt(x[7]), Integer.parseInt(x[8]), Integer.parseInt(x[9]), Integer.parseInt(x[10]),
                    Integer.parseInt(x[11]), Integer.parseInt(x[12]), Integer.parseInt(x[13]), Integer.parseInt(x[14]),
                    Integer.parseInt(x[15]), Integer.parseInt(x[16]), Integer.parseInt(x[17]), Integer.parseInt(x[18]),
                    Integer.parseInt(x[19]), Integer.parseInt(x[20]), Integer.parseInt(x[21]), Integer.parseInt(x[22]),
                    Integer.parseInt(x[23]), Integer.parseInt(x[24]), Integer.parseInt(x[25]), Integer.parseInt(x[26]),
                    Double.parseDouble(x[27]), Double.parseDouble(x[28]), Double.parseDouble(x[29]), Double.parseDouble(x[30]),
                    Double.parseDouble(x[31]), Double.parseDouble(x[32]), Double.parseDouble(x[33]), Double.parseDouble(x[34]),
                    Double.parseDouble(x[35]), Double.parseDouble(x[36]), Double.parseDouble(x[37]), Double.parseDouble(x[38]),
                    Double.parseDouble(x[39]), Double.parseDouble(x[40]), Double.parseDouble(x[41]), Double.parseDouble(x[42]),
                    Double.parseDouble(x[43]), Double.parseDouble(x[44]), Double.parseDouble(x[45]), Double.parseDouble(x[46]));
            list.add(y);
        }
        return list;
    }
    
    private List<DataQuality> mapDataQuality(List<String[]> data) {
        List<DataQuality> list = new ArrayList<>();
        for(String[] x : data) {
            DataQuality y = new DataQuality(x[0], Integer.parseInt(x[1]), Double.parseDouble(x[2])); 
            list.add(y);
        }
        return list;
    }
    
    private List<UnspecificCoding> mapUnspecificCoding(List<String[]> data) {
        List<UnspecificCoding> list = new ArrayList<>();
        for(String[] x : data) {
            UnspecificCoding y = new UnspecificCoding(x[0], Integer.parseInt(x[1]), Integer.parseInt(x[2]), Double.parseDouble(x[3]), Integer.parseInt(x[4]), Integer.parseInt(x[5]), Double.parseDouble(x[6]));
            list.add(y);
        }
        return list;
    }
    
    private List<Participation> mapParticipation(List<String[]> data) {
        List<Participation> list = new ArrayList<>();
        for(String[] x : data) {
            Participation y = new Participation(x[0], Integer.parseInt(x[1]), Integer.parseInt(x[2]), x[3]);
            list.add(y);
        }
        return list;
    }
    
    private List<SystemRated> mapSystemRated(List<String[]> data) {
        List<SystemRated> list = new ArrayList<>();
        for(String[] x : data) {
            SystemRated y = new SystemRated(Integer.parseInt(x[0]), x[1], x[2], Double.parseDouble(x[3]), Integer.parseInt(x[4]), Double.parseDouble(x[5]));
            list.add(y);
        }
        return list;
    }
    
    private List<C_111_211> mapC_111_211_sum(List<String[]> data) {
        List<C_111_211> list = new ArrayList<>();
        for(String[] x : data) {
            C_111_211 y = new C_111_211(1, "", "", Integer.parseInt(x[0]), Integer.parseInt(x[1]), Integer.parseInt(x[2]), Integer.parseInt(x[3]), Double.parseDouble(x[4]), Double.parseDouble(x[5]), Double.parseDouble(x[6]));
            list.add(y);
        }
        return list;
    }
    
    private List<C_112_212> map_C_112_212_sum(List<String[]> data) {
        List<C_112_212> list = new ArrayList<>();
        for(String[] x : data) {
            C_112_212 y = new C_112_212(1, "", Integer.parseInt(x[0]), Integer.parseInt(x[1]), Integer.parseInt(x[2]), Integer.parseInt(x[3]), Double.parseDouble(x[4]), Double.parseDouble(x[5]), Double.parseDouble(x[6]),"");
            list.add(y);
        }
        return list;
    }
    
    private List<PrimaryDiagsProcs> mapPartialInpatientCaresSum(List<String[]> data) {
        List<PrimaryDiagsProcs> list = new ArrayList<>();
        for(String[] x : data) {
            PrimaryDiagsProcs y = new PrimaryDiagsProcs(1, 1,
                    "", "", Integer.parseInt(x[0]), Double.parseDouble(x[1]), Double.parseDouble(x[2]),
                    Integer.parseInt(x[3]), Integer.parseInt(x[4]), Integer.parseInt(x[5]), Integer.parseInt(x[6]),
                    Integer.parseInt(x[7]), Integer.parseInt(x[8]), Integer.parseInt(x[9]), Integer.parseInt(x[10]),
                    Integer.parseInt(x[11]), Integer.parseInt(x[12]), Integer.parseInt(x[13]), Integer.parseInt(x[14]),
                    Integer.parseInt(x[15]), Integer.parseInt(x[16]), Integer.parseInt(x[17]), Integer.parseInt(x[18]),
                    Integer.parseInt(x[19]), Integer.parseInt(x[20]), Integer.parseInt(x[21]), Integer.parseInt(x[22]),
                    Double.parseDouble(x[23]), Double.parseDouble(x[24]), Double.parseDouble(x[25]), Double.parseDouble(x[26]),
                    Double.parseDouble(x[27]), Double.parseDouble(x[28]), Double.parseDouble(x[29]), Double.parseDouble(x[30]),
                    Double.parseDouble(x[31]), Double.parseDouble(x[32]), Double.parseDouble(x[33]), Double.parseDouble(x[34]),
                    Double.parseDouble(x[35]), Double.parseDouble(x[36]), Double.parseDouble(x[37]), Double.parseDouble(x[38]),
                    Double.parseDouble(x[39]), Double.parseDouble(x[40]), Double.parseDouble(x[41]), Double.parseDouble(x[42]));
            list.add(y);
        }
        return list;
    }
}
