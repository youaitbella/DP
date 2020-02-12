package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.bo.AggregatedWards;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.helper.EnvironmentInfo;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.ExceptionCollector;
import org.inek.dataportal.common.utils.DateUtils;
import org.inek.dataportal.common.utils.Period;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AggregatedWardsHelper {

    static final Logger LOGGER = Logger.getLogger("AggregatedWardsHelper");

    //private static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht [%s].";
    public static final String ERROR_MESSAGE_MULTIPLE_BEDS = "Für die Station [%s] wurden unterschiedliche Bettenangaben gemacht, " +
            "diese lauten: [%s]. Es sind stets alle Betten der genannten Station anzugeben. Bitte überarbeiten Sie ihre Angaben zur " +
            "Bettenanzahl der Station.";

    public List<AggregatedWards> aggregatedWards(List<DeptWard> wards) {
        try {
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

                List<Period> periods = new ArrayList<>();
                fromDates.stream().sorted(Date::compareTo).forEachOrdered(from -> {
                    toDates.stream().filter(toDate -> toDate.compareTo(from) >= 0).sorted(Date::compareTo).findFirst()
                            .ifPresent(toDate -> periods.add(new Period(from, toDate)));
                });
                for (Period dates : periods) {
                    deptWards.stream()
                            .filter(deptWard -> deptWard.getValidFrom().compareTo(dates.from()) <= 0
                                    && deptWard.getValidTo().compareTo(dates.to()) >= 0)
                            .sorted(Comparator.comparing(DeptWard::getFab)).forEach(deptWard -> {
                        String key = deptWard.getLocationCodeP21()
                                //+ "|" + ward.getLocationCodeVz() for future usage
                                + "|" + deptWard.getLocationText()
                                + "|" + deptWard.getWardName().toLowerCase().replace(" ", "")
                                // #332 + "|" + (deptWard.getDept().getDeptArea() == 3 ? "Intensiv" : "Other")
                                + "|" + DateUtils.toAnsi(dates.from())
                                + "|" + DateUtils.toAnsi(dates.to());
                        if (aggregatedWards.containsKey(key)) {
                            aggregatedWards.get(key).aggregate(deptWard, dates.from(), dates.to());
                        } else {
                            aggregatedWards.put(key, new AggregatedWards(deptWard, dates.from(), dates.to()));
                        }

                    });
                }
            }
            return aggregatedWards.values().stream()
                    .sorted(Comparator.comparing(AggregatedWards::obtainSortKey))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error during aggregate wards", ex);
            trySendException(ex);


            return new ArrayList<>();
        }
    }

    private void trySendException(Exception exeptionToSend) {
        try {
            Mailer mailer = Utils.getBean(SessionController.class).getMailer();
            String subject = "Exception reported by Server " + EnvironmentInfo.getLocalServerName();
            String msg = new StringBuilder()
                    .append(Utils.collectUrlInformation())
                    .append(ExceptionCollector.collect("Error during aggregate wards", exeptionToSend))
                    .toString();
            mailer.sendMail(ConfigKey.ExceptionEmail.getDefault(), subject, msg);
        } catch (Exception ex) {
            // in case of missing SessionController or anything else we do not send. just log the cause.
            LOGGER.log(Level.WARNING, "Error during trySendException", ex);
        }
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
