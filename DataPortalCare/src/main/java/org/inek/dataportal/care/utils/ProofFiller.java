/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.care.proof.entity.ProofRegulationStation;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lautenti
 */
public class ProofFiller {

    public static void createProofEntrysFromStations(ProofRegulationBaseInformation info, List<ProofRegulationStation> stationList,
                                                     int year, int quarter) {
        List<MonthInfo> monthInfosFromQuarter = getMonthInfosFromQuarter(quarter, year);

        for (ProofRegulationStation station : stationList) {
            for (MonthInfo monthInfo : monthInfosFromQuarter) {
                if (station.isValideForMonth(monthInfo.getMonth())) {
                    for (Shift shift : Shift.values()) {
                        Proof proof = new Proof();
                        proof.setBaseInformation(info);
                        proof.setMonth(monthInfo.getMonth());
                        proof.setCountShift(monthInfo.getDaysInMonth());
                        proof.setShift(shift);
                        proof.setProofRegulationStation(station);
                        info.addProof(proof);
                    }
                }
            }
        }
    }

    private static List<MonthInfo> getMonthInfosFromQuarter(int quarter, int year) {
        List<MonthInfo> infos = new ArrayList<>();
        switch (quarter) {
            case 1:
                infos.add(new MonthInfo(Months.JANUARY, getDaysInMonth(Months.JANUARY, year)));
                infos.add(new MonthInfo(Months.FEBRUARY, getDaysInMonth(Months.FEBRUARY, year)));
                infos.add(new MonthInfo(Months.MARCH, getDaysInMonth(Months.MARCH, year)));
                break;
            case 2:
                infos.add(new MonthInfo(Months.APRIL, getDaysInMonth(Months.APRIL, year)));
                infos.add(new MonthInfo(Months.MAY, getDaysInMonth(Months.MAY, year)));
                infos.add(new MonthInfo(Months.JUNE, getDaysInMonth(Months.JUNE, year)));
                break;
            case 3:
                infos.add(new MonthInfo(Months.JULY, getDaysInMonth(Months.JULY, year)));
                infos.add(new MonthInfo(Months.AUGUST, getDaysInMonth(Months.AUGUST, year)));
                infos.add(new MonthInfo(Months.SEPTEMBER, getDaysInMonth(Months.SEPTEMBER, year)));
                break;
            case 4:
                infos.add(new MonthInfo(Months.OCTOBER, getDaysInMonth(Months.OCTOBER, year)));
                infos.add(new MonthInfo(Months.NOVEMBER, getDaysInMonth(Months.NOVEMBER, year)));
                infos.add(new MonthInfo(Months.DECEMBER, getDaysInMonth(Months.DECEMBER, year)));
                break;
            default:
                break;
        }

        return infos;
    }

    private static int getDaysInMonth(Months month, int year) {
        YearMonth yearMonthObject = YearMonth.of(year, month.getId());
        return yearMonthObject.lengthOfMonth();
    }


    private static class MonthInfo {
        private Months _month;
        private int _daysInMonth;

        MonthInfo(Months month, int daysInMonth) {
            _month = month;
            _daysInMonth = daysInMonth;
        }

        public Months getMonth() {
            return _month;
        }

        public void setMonth(Months month) {
            this._month = month;
        }

        public int getDaysInMonth() {
            return _daysInMonth;
        }

        public void setDaysInMonth(int daysInMonth) {
            this._daysInMonth = daysInMonth;
        }
    }
}
