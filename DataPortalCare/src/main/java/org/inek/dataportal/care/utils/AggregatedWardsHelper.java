package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.bo.DatePair;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptStation;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AggregatedWardsHelper {

    private static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht [%s].";

    public static List<AggregatedWards> generateAggregatedWardsFromWards(List<DeptStation> wards) {
        List<List<DeptStation>> lists = groupStationsByNameAndLocationCodes(wards);
        List<List<DeptStation>> lists1 = groupStationListsByValidity(lists);
        List<AggregatedWards> aggregatedWards = generateAggregatedWardsFromSortedWards(lists1);
        return aggregatedWards;
    }

    protected static List<AggregatedWards> generateAggregatedWardsFromSortedWards(List<List<DeptStation>> wardsList) {
        List<AggregatedWards> aggregatedWards = new ArrayList<>();

        for (List<DeptStation> wards : wardsList) {
            if (wards.size() > 0) {
                aggregatedWards.add(new AggregatedWards(wards));
            }
        }

        return aggregatedWards;
    }

    public static List<String> checkBedCountForWards(List<DeptStation> wards) {
        List<String> errorMessages = new ArrayList<>();
        List<List<DeptStation>> lists = groupStationsByNameAndLocationCodes(wards);

        for (List<DeptStation> stations : lists) {
            if (stations.stream().mapToInt(DeptStation::getBedCount).distinct().count() != 1) {
                errorMessages.add(buildErrorMessageForMultipleBedCounts(stations));
            }
        }
        return errorMessages;
    }

    protected static String buildErrorMessageForMultipleBedCounts(List<DeptStation> stations) {
        String stationName = stations.get(0).getStationName();
        String bedCounts = stations.stream().mapToInt(DeptStation::getBedCount)
                .distinct()
                .mapToObj(i -> ((Integer) i).toString())
                .collect(Collectors.joining(", "));
        return String.format(ERROR_MESSAGE_MULTIPLE_BEDS, stationName, bedCounts);
    }

    protected static Boolean stringsAreEqual(String value1, String value2) {
        String value1Formatted = value1.trim().replaceAll("\\s", "").toUpperCase();
        String value2Formatted = value2.trim().replaceAll("\\s", "").toUpperCase();

        return value1Formatted.equals(value2Formatted);
    }

    protected static List<List<DeptStation>> groupStationListsByValidity(List<List<DeptStation>> lists) {
        List<List<DeptStation>> newList = new ArrayList<>();

        for (List<DeptStation> list : lists) {
            Set<DatePair> allValidityRanges = findAllValidityRanges(list);

            for (DatePair dateRange : allValidityRanges) {
                List<DeptStation> stationsInDatePairRange = findStationsInDatePairRange(list, dateRange);
                newList.add(stationsInDatePairRange);
            }
        }

        return newList;
    }

    protected static Set<DatePair> findAllValidityRanges(List<DeptStation> stations) {
        Set<DatePair> datePairSet = new HashSet<>();

        Date minDate;
        Date nextDateInList = null;

        for (int i = 0; i < stations.size(); i++) {
            if (nextDateInList == null) {
                minDate = stations.stream()
                        .min(Comparator.comparing(c -> c.getValidFrom().getTime()))
                        .get().getValidFrom();
            } else {
                Date finalNextDateInList = nextDateInList;
                Optional<DeptStation> min = stations.stream()
                        .filter(c -> c.getValidFrom().after(finalNextDateInList))
                        .min(Comparator.comparing(c -> c.getValidFrom().getTime()));

                if (min.isPresent()) {
                    minDate = min.get().getValidFrom();
                } else if (nextDateInList.equals(getMaxDate())) {
                    continue;
                } else {
                    datePairSet.add(new DatePair(nextDateInList, getMaxDate()));
                    continue;
                }
            }
            nextDateInList = findNextDateInList(minDate, stations);
            datePairSet.add(new DatePair(minDate, nextDateInList));
        }
        return datePairSet;
    }

    private static Date getMaxDate() {
        LocalDateTime datetime = LocalDateTime.of(2050, Month.DECEMBER, 31, 1, 1, 1);
        return java.util.Date.from(datetime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    protected static Date findNextDateInList(Date date, List<DeptStation> object) {
        Date tmpMinFrom = null;

        Optional<DeptStation> minFromValue = object.stream()
                .filter(c -> c.getValidFrom().after(date))
                .min(Comparator.comparing(c -> c.getValidFrom().getTime()));

        if (minFromValue.isPresent()) {
            tmpMinFrom = minFromValue.get().getValidFrom();
        }

        Date tmpMinTo = null;

        Optional<DeptStation> minToValue = object.stream()
                .filter(c -> c.getValidTo().after(date))
                .min(Comparator.comparing(c -> c.getValidTo().getTime()));

        if (minToValue.isPresent()) {
            tmpMinTo = minToValue.get().getValidTo();
        }

        if (tmpMinFrom == null && tmpMinTo != null) {
            return tmpMinTo;
        }

        if (tmpMinTo == null && tmpMinFrom != null) {
            return tmpMinFrom;
        }

        if (tmpMinTo == null && tmpMinFrom == null) {
            return getMaxDate();
        }

        if (tmpMinFrom.before(tmpMinTo)) {
            return tmpMinFrom;
        } else {
            return tmpMinTo;
        }
    }


    protected static List<DeptStation> findStationsInDatePairRange(List<DeptStation> stations, DatePair pair) {
        List<DeptStation> collect = stations.stream().filter(c -> (c.getValidFrom().getTime() <= pair.getDate1().getTime()) &&
                (c.getValidTo().getTime() >= pair.getDate2().getTime()))
                .collect(Collectors.toList());
        return collect;
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
