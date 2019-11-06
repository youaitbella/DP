package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.bo.DatePair;
import org.inek.dataportal.care.entities.DeptWard;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AggregatedWardsHelper {

    //private static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht [%s].";
    private static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht, " +
            "diese lauten: [%s]. Es sind stets alle Betten der genannten Station anzugeben. Bitte überarbeiten Sie ihre Angaben zur " +
            "Bettenanzahl der Station.";

    public static List<AggregatedWards> aggregatedWards(List<DeptWard> wards) {
        Map<String, AggregatedWards> aggregatedWards = new ConcurrentHashMap<>();
        for (DeptWard ward : wards) {
            String key = ward.getLocationCodeP21()
                    + "|" + ward.getLocationCodeVz()
                    + "|" + ward.getWardName().toLowerCase().replace(" ", "")
                    + "|" + ward.getValidFrom()
                    + "|" + ward.getValidTo();
            if (aggregatedWards.containsKey(key)) {
                aggregatedWards.get(key).aggregate(ward);
            } else {
                aggregatedWards.put(key, new AggregatedWards(ward));
            }
        }
        return new ArrayList<>(aggregatedWards.values());
    }

    public static List<String> checkBedCountForWards(List<DeptWard> wards) {
        List<String> errorMessages = new ArrayList<>();
        List<List<DeptWard>> lists = groupStationsByNameAndLocationCodes(wards);

        for (List<DeptWard> stations : lists) {
            if (stations.stream().mapToInt(DeptWard::getBedCount).distinct().count() != 1) {
                errorMessages.add(buildErrorMessageForMultipleBedCounts(stations));
            }
        }
        return errorMessages;
    }

    protected static String buildErrorMessageForMultipleBedCounts(List<DeptWard> stations) {
        String stationName = stations.get(0).getWardName();
        String bedCounts = stations.stream().mapToInt(DeptWard::getBedCount)
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

    protected static List<List<DeptWard>> groupStationListsByValidity(List<List<DeptWard>> lists) {
        List<List<DeptWard>> newList = new ArrayList<>();

        for (List<DeptWard> list : lists) {
            Set<DatePair> allValidityRanges = findAllValidityRanges(list);

            for (DatePair dateRange : allValidityRanges) {
                List<DeptWard> stationsInDatePairRange = findStationsInDatePairRange(list, dateRange);
                newList.add(stationsInDatePairRange);
            }
        }

        return newList;
    }

    protected static Set<DatePair> findAllValidityRanges(List<DeptWard> stations) {
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
                Optional<DeptWard> min = stations.stream()
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

    public static Date getMaxDate() {
        return createDate(31, Month.DECEMBER, 2050, 0, 0, 0);
    }

    public static Date createDate(int day, Month month, int year, int hour, int minute, int second) {
        LocalDateTime datetime = LocalDateTime.of(year, month, day, hour, minute, second);
        return java.util.Date.from(datetime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }


    protected static Date findNextDateInList(Date date, List<DeptWard> object) {
        Date tmpMinFrom = null;

        Optional<DeptWard> minFromValue = object.stream()
                .filter(c -> c.getValidFrom().after(date))
                .min(Comparator.comparing(c -> c.getValidFrom().getTime()));

        if (minFromValue.isPresent()) {
            tmpMinFrom = minFromValue.get().getValidFrom();
        }

        Date tmpMinTo = null;

        Optional<DeptWard> minToValue = object.stream()
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


    protected static List<DeptWard> findStationsInDatePairRange(List<DeptWard> stations, DatePair pair) {
        List<DeptWard> collect = stations.stream().filter(c -> (c.getValidFrom().getTime() <= pair.getDate1().getTime()) &&
                (c.getValidTo().getTime() >= pair.getDate2().getTime()))
                .collect(Collectors.toList());
        return collect;
    }


    protected static List<List<DeptWard>> groupStationsByNameAndLocationCodes(List<DeptWard> wards) {
        List<List<DeptWard>> deptsStations = new ArrayList<>();

        for (DeptWard ward : wards) {
            if (!wardIsInAnyList(deptsStations, ward)) {
                List<DeptWard> stations = wards.stream().filter(c -> stringsAreEqual(c.getWardName(), ward.getWardName())
                        && c.getLocationCodeP21() == ward.getLocationCodeP21()
                        && c.getLocationCodeVz() == ward.getLocationCodeVz())
                        .collect(Collectors.toList());

                deptsStations.add(stations);
            }
        }

        return deptsStations;
    }

    protected static Boolean wardIsInAnyList(List<List<DeptWard>> deptsStations, DeptWard ward) {
        for (List<DeptWard> deptsStation : deptsStations) {
            if (deptsStation.contains(ward)) {
                return true;
            }
        }
        return false;
    }

}
