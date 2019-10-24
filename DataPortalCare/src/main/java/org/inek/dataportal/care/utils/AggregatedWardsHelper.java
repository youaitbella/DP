package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptStation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AggregatedWardsHelper {

    public static List<AggregatedWards> generateAggregatedWardsFromWards(List<DeptStation> wards) {
        List<AggregatedWards> aggregatedWards = new ArrayList<>();
        List<List<DeptStation>> lists = groupStationsByNameAndLocationCodes(wards);


        return aggregatedWards;
    }

    protected static Boolean stringsAreEqual(String value1, String value2) {
        String value1Formatted = value1.trim().replaceAll("\\s", "").toUpperCase();
        String value2Formatted = value2.trim().replaceAll("\\s", "").toUpperCase();

        return value1Formatted.equals(value2Formatted);
    }

    protected static List<List<DeptStation>> groupStationListsByValidity(List<List<DeptStation>> lists) {
        List<List<DeptStation>> newList = new ArrayList<>();

        for (List<DeptStation> list : lists) {
            List<List<DeptStation>> tmpNewList = new ArrayList<>();

            for (DeptStation station : list) {

            }
        }

        return newList;
    }

    protected static void findAllValidityRanges(List<DeptStation> stations) {

    }

    protected static boolean listsContainsListWithValidity(List<List<DeptStation>> lists, DeptStation station) {
        return true;
    }

    protected static List<List<DeptStation>> groupStationsByNameAndLocationCodes(List<DeptStation> wards) {
        List<List<DeptStation>> deptsStations = new ArrayList<>();

        for (DeptStation ward : wards) {
            if (!wardIsInAnyList(deptsStations, ward)) {
                List<DeptStation> stations = wards.stream().filter(c -> stringsAreEqual(c.getStationName(), ward.getStationName())
                        && c.getLocationCodeP21() == ward.getLocationCodeP21()
                        && c.getLocationCodeVz() == ward.getLocationCodeVz())
                        .collect(Collectors.toList());

                deptsStations.add(stations);
            }
        }

        return deptsStations;
    }

    protected static Boolean wardIsInAnyList(List<List<DeptStation>> deptsStations, DeptStation ward) {
        for (List<DeptStation> deptsStation : deptsStations) {
            if (deptsStation.contains(ward)) {
                return true;
            }
        }
        return false;
    }

}
