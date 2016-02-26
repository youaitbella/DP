/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<C_111_211> _c_211;

    private List<C_112_212> _c_112_212;
    private List<C_112_212> _c_112;
    private List<C_112_212> _c_212;

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

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="C">
    
        public List<BedClass> getBedClasses(int dataYear) {
        return mapBedClass(_appData.readDataFile(dataYear, "B_1_KH_Bundesland_Groesse(Betten)"));
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

    // <editor-fold defaultstate="collapsed" desc="sum">
    public List<C_111_211> getC_111_sum() {
        List<C_111_211> x = new ArrayList<>();
        C_111_211 sum = new C_111_211(1, "", "", 0, 0, 0, 0, 0.0, 0.0, 0.0);
        for (C_111_211 c : _c_111) {
            sum.setSumA(sum.getSumA() + c.getSumA());
            sum.setSumAw(sum.getSumAw() + c.getSumAw());
            sum.setSumAm(sum.getSumAm() + c.getSumAm());
            sum.setSumAu(sum.getSumAu() + c.getSumAu());
        }
        sum.setFractionW((float) sum.getSumAw() / sum.getSumA());
        sum.setFractionM((float) sum.getSumAm() / sum.getSumA());
        sum.setFractionU((float) sum.getSumAu() / sum.getSumA());
        x.add(sum);
        return x;
    }

    public List<C_111_211> getC_211_sum() {
        List<C_111_211> x = new ArrayList<>();
        C_111_211 sum = new C_111_211(1, "", "", 0, 0, 0, 0, 0.0, 0.0, 0.0);
        for (C_111_211 c : _c_211) {
            sum.setSumA(sum.getSumA() + c.getSumA());
            sum.setSumAw(sum.getSumAw() + c.getSumAw());
            sum.setSumAm(sum.getSumAm() + c.getSumAm());
            sum.setSumAu(sum.getSumAu() + c.getSumAu());
        }
        sum.setFractionW((float) sum.getSumAw() / sum.getSumA());
        sum.setFractionM((float) sum.getSumAm() / sum.getSumA());
        sum.setFractionU((float) sum.getSumAu() / sum.getSumA());
        x.add(sum);
        return x;
    }

    public List<C_112_212> getC_112_sum() {
        List<C_112_212> x = new ArrayList<>();
        C_112_212 sum = new C_112_212(1, "", 0, 0, 0, 0, 0.0, 0.0, 0.0, "");
        for (C_112_212 c : _c_112) {
            sum.setSumA(sum.getSumA() + c.getSumA());
            sum.setSumAw(sum.getSumAw() + c.getSumAw());
            sum.setSumAm(sum.getSumAm() + c.getSumAm());
            sum.setSumAu(sum.getSumAu() + c.getSumAu());
        }
        sum.setFractionW((float) sum.getSumAw() / sum.getSumA());
        sum.setFractionM((float) sum.getSumAm() / sum.getSumA());
        sum.setFractionU((float) sum.getSumAu() / sum.getSumA());
        x.add(sum);
        return x;
    }

    public List<C_112_212> getC_212_sum() {
        List<C_112_212> x = new ArrayList<>();
        C_112_212 sum = new C_112_212(1, "", 0, 0, 0, 0, 0.0, 0.0, 0.0, "");
        for (C_112_212 c : _c_212) {
            sum.setSumA(sum.getSumA() + c.getSumA());
            sum.setSumAw(sum.getSumAw() + c.getSumAw());
            sum.setSumAm(sum.getSumAm() + c.getSumAm());
            sum.setSumAu(sum.getSumAu() + c.getSumAu());
        }
        sum.setFractionW((float) sum.getSumAw() / sum.getSumA());
        sum.setFractionM((float) sum.getSumAm() / sum.getSumA());
        sum.setFractionU((float) sum.getSumAu() / sum.getSumA());
        x.add(sum);
        return x;
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
    private List<BedClass> mapBedClass(List<String[]> data) {
        return Collections.EMPTY_LIST; // todo
    }
    
    
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

}
