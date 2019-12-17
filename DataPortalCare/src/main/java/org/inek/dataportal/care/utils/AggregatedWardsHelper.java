package org.inek.dataportal.care.utils;

import javafx.util.Pair;
import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.common.utils.DateUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AggregatedWardsHelper {

    //private static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht [%s].";
    public static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht, " +
            "diese lauten: [%s]. Es sind stets alle Betten der genannten Station anzugeben. Bitte überarbeiten Sie ihre Angaben zur " +
            "Bettenanzahl der Station.";

    public List<AggregatedWards> aggregatedWards(List<DeptWard> wards) {
        Map<String, AggregatedWards> aggregatedWards = new ConcurrentHashMap<>();

        Map<String, List<DeptWard>> wardMap = wards.stream()
                .sorted(Comparator.comparing(this::determineKey))
                .collect(Collectors.groupingBy(this::determineKey));

        for (List<DeptWard> deptWards : wardMap.values()) {
            Set<Date> toDates = deptWards.stream().map(w -> w.getValidTo()).collect(Collectors.toSet());
            deptWards.stream().map(w -> DateUtils.addDays(w.getValidFrom(), -1)).forEach(toDate -> {
                toDates.add(toDate);
            });

            Set<Date> fromDates = deptWards.stream().map(w -> w.getValidFrom()).collect(Collectors.toSet());
            deptWards.stream().map(w -> DateUtils.addDays(w.getValidTo(), 1)).forEach(toDate -> {
                fromDates.add(toDate);
            });

            List<Pair<Date, Date>> fromToDates = new ArrayList<>();
            fromDates.stream().sorted(Date::compareTo).forEachOrdered(from -> {
                toDates.stream().filter(toDate -> toDate.compareTo(from) >= 0).sorted(Date::compareTo).findFirst()
                        .ifPresent(toDate -> fromToDates.add(new Pair<>(from, toDate)));
            });
            for (Pair<Date, Date> fromTo : fromToDates) {
                deptWards.stream()
                        .filter(deptWard -> deptWard.getValidFrom().compareTo(fromTo.getKey()) <= 0
                                && deptWard.getValidTo().compareTo(fromTo.getValue()) >= 0)
                        .sorted(Comparator.comparing(DeptWard::getFab)).forEach(deptWard -> {
                    String key = deptWard.getLocationCodeP21()
                            //+ "|" + ward.getLocationCodeVz() for future usage
                            + "|" + deptWard.getLocationText()
                            + "|" + deptWard.getWardName().toLowerCase().replace(" ", "")
                            // #332 + "|" + (deptWard.getDept().getDeptArea() == 3 ? "Intensiv" : "Other")
                            + "|" + DateUtils.toAnsi(fromTo.getKey())
                            + "|" + DateUtils.toAnsi(fromTo.getValue());
                    if (aggregatedWards.containsKey(key)) {
                        aggregatedWards.get(key).aggregate(deptWard, fromTo.getKey(), fromTo.getValue());
                    } else {
                        aggregatedWards.put(key, new AggregatedWards(deptWard, fromTo.getKey(), fromTo.getValue()));
                    }

                });
            }
        }
        return aggregatedWards.values().stream()
                .sorted(Comparator.comparing(AggregatedWards::obtainSortKey))
                .collect(Collectors.toList());
    }

    private String determineKey(DeptWard ward) {
        String key = ward.getLocationCodeP21()
                + "|" + ward.getLocationText()
                + "|" + ward.getWardName().toLowerCase().replace(" ", "");
        // #332 + "|" + (ward.getDept().getDeptArea() == 3 ? "Intensiv" : "Other");
        return key;
    }


    protected static Boolean stringsAreEqual(String value1, String value2) {
        String value1Formatted = value1.trim().replaceAll("\\s", "").toUpperCase();
        String value2Formatted = value2.trim().replaceAll("\\s", "").toUpperCase();

        return value1Formatted.equals(value2Formatted);
    }



}
