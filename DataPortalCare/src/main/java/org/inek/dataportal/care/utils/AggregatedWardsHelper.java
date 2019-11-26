package org.inek.dataportal.care.utils;

import javafx.util.Pair;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.bo.DatePair;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.common.utils.DateUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.inek.dataportal.common.utils.DateUtils.getMaxDate;

public class AggregatedWardsHelper {

    //private static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht [%s].";
    public static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht, " +
            "diese lauten: [%s]. Es sind stets alle Betten der genannten Station anzugeben. Bitte überarbeiten Sie ihre Angaben zur " +
            "Bettenanzahl der Station.";

    public static List<AggregatedWards> aggregatedWards(List<DeptWard> wards) {
        Map<String, AggregatedWards> aggregatedWards = new ConcurrentHashMap<>();

        Map<String, List<DeptWard>> wardMap = wards.stream()
                .sorted(Comparator.comparing(AggregatedWardsHelper::getKey))
                .collect(Collectors.groupingBy(AggregatedWardsHelper::getKey));

        for (List<DeptWard> deptWards : wardMap.values()) {
            List<Date> toDates = deptWards.stream().map(w -> w.getValidTo()).distinct().sorted(Date::compareTo).collect(Collectors.toList());
            List<Pair<Date, Date>> fromToDates = new ArrayList<>();
            deptWards.stream().map(w -> w.getValidFrom()).distinct().sorted(Date::compareTo).forEachOrdered(from -> {
                toDates.stream().filter(toDate -> toDate.compareTo(from) >= 0).findFirst().ifPresent(toDate -> fromToDates.add(new Pair<>(from, toDate)));
            });
            for (Pair<Date, Date> fromTo : fromToDates) {
                for (DeptWard deptWard : deptWards) {
                    if (deptWard.getValidFrom().compareTo(fromTo.getKey()) <= 0
                            && deptWard.getValidTo().compareTo(fromTo.getValue()) >= 0) {
                        String key = deptWard.getLocationCodeP21()
                                //+ "|" + ward.getLocationCodeVz() for future usage
                                + "|" + deptWard.getLocationText()
                                + "|" + deptWard.getWardName().toLowerCase().replace(" ", "")
                                + "|" + (deptWard.getDept().getDeptArea() == 3 ? "Intensiv" : "Other")
                                + "|" + DateUtils.toAnsi(fromTo.getKey())
                                + "|" + DateUtils.toAnsi(fromTo.getValue());
                        if (aggregatedWards.containsKey(key)) {
                            aggregatedWards.get(key).aggregate(deptWard, fromTo.getKey(), fromTo.getValue());
                        } else {
                            aggregatedWards.put(key, new AggregatedWards(deptWard, fromTo.getKey(), fromTo.getValue()));
                        }

                    }
                }
            }
        }
        return aggregatedWards.values().stream()
                .sorted(Comparator.comparing(AggregatedWards::obtainSortKey))
                .collect(Collectors.toList());
    }

    private static String getKey(DeptWard ward) {
        String key = ward.getLocationCodeP21()
                + "|" + ward.getLocationText()
                + "|" + ward.getWardName().toLowerCase().replace(" ", "")
                + "|" + (ward.getDept().getDeptArea() == 3 ? "Intensiv" : "Other");
        return key;
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


    public static List<AggregatedWards> generateAggregatedWardsFromWards(List<DeptWard> wards) {
        List<List<DeptWard>> lists = groupStationsByNameAndLocationCodes(wards);
        List<List<DeptWard>> lists1 = groupStationListsByValidity(lists);
        List<AggregatedWards> aggregatedWards = generateAggregatedWardsFromSortedWards(lists1);
        return aggregatedWards;
    }

    protected static List<AggregatedWards> generateAggregatedWardsFromSortedWards(List<List<DeptWard>> wardsList) {
        List<AggregatedWards> aggregatedWards = new ArrayList<>();

        for (List<DeptWard> wards : wardsList) {
            if (wards.size() > 0) {
                //aggregatedWards.add(new AggregatedWards(wards));
            }
        }

        return aggregatedWards;
    }


}
